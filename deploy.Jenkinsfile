pipeline {
    agent any

    environment {
        IMAGE_NAME = "prabeshdevops/student-app"
        IMAGE_TAG  = "latest"

        DEPLOY_SERVER = "54.225.56.218"
        DEPLOY_USER   = "ubuntu"
        DEPLOY_PORT   = "22"

        APP_NAME = "student-app"

        // FIXED: avoid Jenkins port conflict
        HOST_PORT = "9090"
        CONTAINER_PORT = "8080"

        ENV_FILE = "/home/ubuntu/.spring.env"
    }

    stages {

        stage('📥 Pull Docker Image') {
            steps {
                sshagent(['deployment-server-ssh']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no -p ${DEPLOY_PORT} ${DEPLOY_USER}@${DEPLOY_SERVER} \\
                    docker pull ${IMAGE_NAME}:${IMAGE_TAG}
                    """
                }
            }
        }

        stage('🛑 Stop Old Container') {
            steps {
                sshagent(['deployment-server-ssh']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no -p ${DEPLOY_PORT} ${DEPLOY_USER}@${DEPLOY_SERVER} '
                    docker rm -f ${APP_NAME} || true
                    '
                    """
                }
            }
        }

        stage('🚀 Run New Container') {
            steps {
                sshagent(['deployment-server-ssh']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no -p ${DEPLOY_PORT} ${DEPLOY_USER}@${DEPLOY_SERVER} '
                    docker run -d \
                    --name ${APP_NAME} \
                    -p ${HOST_PORT}:${CONTAINER_PORT} \
                    --restart unless-stopped \
                    --env-file ${ENV_FILE} \
                    ${IMAGE_NAME}:${IMAGE_TAG}
                    '
                    """
                }
            }
        }

        stage('🔍 Health Check (Robust)') {
            steps {
                sshagent(['deployment-server-ssh']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no -p ${DEPLOY_PORT} ${DEPLOY_USER}@${DEPLOY_SERVER} '
                    
                    echo "Waiting for application to start..."

                    for i in {1..12}
                    do
                      if curl -f http://localhost:${HOST_PORT}/actuator/health; then
                        echo "Application is UP"
                        exit 0
                      fi

                      echo "Attempt \$i failed... retrying"
                      sleep 5
                    done

                    echo "Health check failed"
                    exit 1
                    '
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Deployment Successful!'
        }

        failure {
            echo '❌ Deployment Failed! Check logs.'
        }
    }
}