pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "prabeshdevops/student-app"
        DOCKER_TAG = "latest"
        DOCKER_CREDENTIALS_ID = "dockerhub-credentials"
        SONARQUBE_ENV = "sonarqube-server"
        JAR_NAME = "student-app-0.0.1-SNAPSHOT.jar"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/prabeshbuilds/Student_ManagementSystem-Devops.git'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE_ENV}") {
                    sh '''
                        mvn sonar:sonar \
                        -Dsonar.projectKey=student-app \
                        -Dsonar.projectName=StudentApp \
                        -Dsonar.host.url=$SONAR_HOST_URL \
                        -Dsonar.login=$SONAR_AUTH_TOKEN
                    '''
                }
            }
        }

        // OPTIONAL QUALITY GATE
        // stage('Quality Gate') {
        //     steps {
        //         timeout(time: 5, unit: 'MINUTES') {
        //             waitForQualityGate abortPipeline: true
        //         }
        //     }
        // }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t $DOCKER_IMAGE:$DOCKER_TAG ."
            }
        }

        stage('Login to DockerHub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: "docker-credentials",
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                sh "docker push $DOCKER_IMAGE:$DOCKER_TAG "
            }
        }

        stage('Deploy (Optional)') {
            steps {
                echo "🚀 Deploy step can be added (AWS EC2 / Kubernetes)"
            }
        }
    }

    post {
        success {
            echo '✅ Spring Boot Build, Test, Scan & Push Successful!'
        }
        failure {
            echo '❌ Pipeline Failed!'
        }
    }
}