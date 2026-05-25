pipeline {
    agent any

    environment {
        IMAGE_NAME = "prabeshdevops/student-app"
        IMAGE_TAG  = "latest"

        DEPLOY_SERVER = "54.86.69.222"
        DEPLOY_USER   = "ubuntu"
        DEPLOY_PORT   = "22"

        APP_NAME = "student-app"
        APP_PORT = "8080"

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
                    echo "Waiting for app to be ready..."
                    counter=1
                    while [ \$counter -le 15 ]; do
                        echo "Health check attempt \$counter/15..."
                        if curl -f ${DEPLOY_SERVER}:${APP_PORT}/students/health; then
                            echo "✅ App is healthy!"
                            exit 0
                        fi
                        counter=\$((counter + 1))
                        sleep 10
                    done
                    echo "❌ Health check failed, showing container logs:"
                    docker logs ${APP_NAME} --tail 100
                    exit 1
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