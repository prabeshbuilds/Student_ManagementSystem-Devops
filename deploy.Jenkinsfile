pipeline {
    agent any

    environment {
        IMAGE_NAME = "prabeshdevops/student-app"
        IMAGE_TAG  = "latest"

        DEPLOY_SERVER = "35.174.16.28"
        DEPLOY_USER   = "ubuntu"
        DEPLOY_PORT   = "22"

        APP_NAME = "student-app"
        APP_PORT = "9090"

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
                        echo "Health checking on ${DEPLOY_SERVER}:${APP_PORT}..."
                        retries=0
                        
                        until curl -fsS http://${DEPLOY_SERVER}:${APP_PORT}/actuator/health -o /dev/null; do
                            retries=\$((retries + 1))
                            echo "Attempt \$retries/12 - waiting for app..."
                            if [ "\$retries" -ge 12 ]; then
                                echo "Health check failed after 12 attempts"
                                echo "Actuator health response:"
                                curl -sS http://${DEPLOY_SERVER}:${APP_PORT}/actuator/health || true
                                echo
                                echo "Disk usage on deployment server:"
                                df -h
                                docker logs ${APP_NAME} --tail 50
                                exit 1
                            fi
                            sleep 5
                        done
                        
                        echo "✅ Health check passed - App is running on port ${APP_PORT}"
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
