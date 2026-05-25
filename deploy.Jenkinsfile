pipeline {
    agent any

    environment {
        IMAGE_NAME = "prabeshdevops/student-app"
        IMAGE_TAG  = "latest"

        DEPLOY_SERVER = "54.86.69.222"
        DEPLOY_USER   = "ubuntu"
        DEPLOY_PORT   = "22"

        APP_NAME = "student-app"
        APP_PORT = "9099"

        ENV_FILE = "/home/ubuntu/.spring.env"
    }

    stages {

        stage('📥 Pull Docker Image') {
            steps {
                sshagent(['deployment-server-ssh']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no -p ${DEPLOY_PORT} ${DEPLOY_USER}@${DEPLOY_SERVER} \
                    docker pull ${IMAGE_NAME}:${IMAGE_TAG}
                    """
                }
            }
        }

        stage('🛑 Clean Old Container & Free Port') {
            steps {
                sshagent(['deployment-server-ssh']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no -p ${DEPLOY_PORT} ${DEPLOY_USER}@${DEPLOY_SERVER} '
                    docker rm -f ${APP_NAME} || true &&
                    sudo fuser -k ${APP_PORT}/tcp || true
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
                    --network host \
                    --env-file ${ENV_FILE} \
                    --restart unless-stopped \
                    ${IMAGE_NAME}:${IMAGE_TAG}
                    '
                    """
                }
            }
        }

        stage('🔍 Health Check') {
            steps {
                sshagent(['deployment-server-ssh']) {
                    sh """
                     ssh -o StrictHostKeyChecking=no -p ${DEPLOY_PORT} ${DEPLOY_USER}@${DEPLOY_SERVER} '
                            echo "Waiting for application..."
                            sleep 20

                            if curl -f http://localhost:${APP_PORT}/students/health; then
                                echo "Application is healthy"
                            else
                                echo "Application failed"
                                docker logs ${APP_NAME} --tail 100
                                exit 1
                            fi
                            '
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Java App Deployment Successful!'
        }

        failure {
            echo '❌ Deployment Failed! Check logs.'
        }
    }
}