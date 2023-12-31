package com.example.polls.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.polls.model.Video;
import com.example.polls.service.UploadMediaService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.*;


@RestController
@RequestMapping("/api/media")
public class MediaUploaderController {

    @Value("${server.compression.mime-types}")
    private List<String> contentVideos;

    @Autowired
    private UploadMediaService service;

    @PostMapping(
            value = "/upload/video",
            consumes = {
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<?> createVideo(
            @RequestPart("content") @Valid @NotNull @NotEmpty MultipartFile file) throws IOException {
        String contentType = file.getContentType();

        if (!contentVideos.contains(contentType)) {
            
            return badRequest().body("File bukan video!");
        }

        Video metaData = service.createFile(file);
        return ok(metaData);
    }

    @GetMapping("/download/{id}/thumbnail")
    public ResponseEntity<?> getThumbnailById(@PathVariable("id") @NotNull @NotEmpty Long id) {
        Optional<Video> mediaOptional = service.findById(id);
        if (!mediaOptional.isPresent()) {
            return noContent().build();
        }

        Video media = mediaOptional.get();
        if (!media.isThumbnail()) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

        String path = media.getThumbnailPath();
        try {
            byte[] file = service.getFile(path);
            String encodeString = Base64.getEncoder().encodeToString(file);
            return ok(encodeString);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="/download/{id}/video/original")
    public ResponseEntity<?> getVideoOriginal(@PathVariable("id") @NotNull @NotEmpty Long id) {
        Optional<Video> mediaOptional = service.findById(id);
        if (!mediaOptional.isPresent()) {
            return noContent().build();
        }

        Video media = mediaOptional.get();
        if (!media.isWrite()) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

        String path = media.getOriginalFilePath();
        try {
            byte[] file = service.getFile(path);
            return ok(file);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{id}/video/compressed")
    public ResponseEntity<?> getVideoCompressed(@PathVariable("id") @NotNull @NotEmpty Long id) {
        Optional<Video> mediaOptional = service.findById(id);
        if (!mediaOptional.isPresent()) {
            return noContent().build();
        }

        Video media = mediaOptional.get();
        if (!media.isCompressed()) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

        String path = media.getCompressedFilePath();
        try {
            byte[] file = service.getFile(path);
            return ok(file);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}