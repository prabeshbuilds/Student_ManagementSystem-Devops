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
                    retries=0

                    until curl -fsS http://${DEPLOY_SERVER}:${APP_PORT}/students/health -o /dev/null; do
                        retries=\$((retries + 1))
                        echo "Health check attempt \$retries failed. Retrying..."
                        if [ "\$retries" -ge 12 ]; then
                            echo "Application health check failed after \$retries attempts"
                            ssh -o StrictHostKeyChecking=no -p ${DEPLOY_PORT} ${DEPLOY_USER}@${DEPLOY_SERVER} 'docker logs ${APP_NAME} --tail 100'
                            exit 1
                        fi
                        sleep 5
                    done

                    echo "Application is healthy"
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