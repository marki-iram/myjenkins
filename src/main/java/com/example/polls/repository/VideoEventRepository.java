package com.example.polls.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.polls.model.VideoEvent;


@Repository
public interface VideoEventRepository extends JpaRepository<VideoEvent, Long> {
}
