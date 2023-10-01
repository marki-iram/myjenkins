pipeline {
    agent any
 
    stages {
        stage('Checkout') {
            steps {
                // Checkout the source code from the Git repository
                git branch: 'main', 'https://github.com/marki-iram/myjenkins.git'
            }
        }
        
        stage('Build') {
            steps {
                // Build the Spring Boot application using Maven
                sh 'mvn clean package'
            }
        }
        
        stage('Deploy') {
            steps {
                // Deploy the Spring Boot application (e.g., to a Tomcat server)
                sh './deploy-script.sh' // Use your deployment script or commands here
            }
        }
    }
}
