GitHub Actions workflow
=======================

This repository now includes a GitHub Actions workflow at .github/workflows/ci.yml.

Required GitHub secrets:
- DOCKERHUB_USERNAME
- DOCKERHUB_TOKEN

The workflow will:
- build and test the Spring Boot application with Maven
- build the Docker image
- push the image to Docker Hub on pushes to main/master
