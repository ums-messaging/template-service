pipeline {
    agent any

    environment {
        AWS_REGION = 'ap-northeast-2'
        TARGET_INSTANCE_ID = 'i-05fa21db1c45f7556'
        REGISTRY = "registry.ums.local:5000"
        APP_NAME = "template-service"
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        GIT_BRANCH = "${env.BRANCH_NAME ?: 'master'}"
        SPRING_PROFILES_ACTIVE = 'test'
        KAFKA_PORT = '9092'
    }

    stages {
        stage('Checkout Eureka Server') {
            steps {
                 script {
                    def branch = env.BRANCH_NAME ?: 'master'
                    sshagent(['git']) {
                         sh """
                            if [ ! -d .git ]; then
                                git init
                                git remote add origin git@github.com:ums-messaging/template-service.git
                                git pull origin $GIT_BRANCH
                            else
                                git fetch origin $GIT_BRANCH
                                git checkout $GIT_BRANCH
                                git pull origin $GIT_BRANCH
                            fi
                         """
                    }
                 }
            }
        }

        stage('Test') {
            steps {
                sh '''
                    chmod +x gradlew
                    ./gradlew clean test --no-daemon
                '''
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Gradle Build') {
            steps {
                script {
                    sh 'nohup aws ssm start-session --target i-00464ff35252824cb --document-name AWS-StartPortForwardingSession --parameters "portNumber=8081,localPortNumber=8081" > ~/ssm-session.log 2>&1 &'
                    sh 'chmod +x gradlew'
                    sh './gradlew build --refresh-dependencies --no-daemon'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    docker build -t ${REGISTRY}/${APP_NAME}:${IMAGE_TAG} .
                    docker login ${REGISTRY} --username jang314 --password jang314
                    docker push ${REGISTRY}/${APP_NAME}:${IMAGE_TAG}
                '''
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh """
                        aws ssm send-command \
                            --instance-ids "${TARGET_INSTANCE_ID}" \
                            --targets "Key=instanceIds,Values=${TARGET_INSTANCE_ID}" \
                            --document-name "AWS-RunShellScript" \
                            --region ${AWS_REGION} \
                            --comment "Deploy ${APP_NAME}" \
                            --parameters '{"commands" : [
                               "docker login ${REGISTRY} --username jang314 --password jang314",
                               "cd /data/schedule-service",
                               "export HOST_IP=\$(hostname -i)",
                               "export IMAGE_TAG=${IMAGE_TAG}",
                               "docker stop ${APP_NAME} || true",
                               "docker rm ${APP_NAME} || true",
                               "docker-compose pull && docker-compose up -d"
                            ]}'
                    """
                 }
            }
        }


    }

    post {
        failure {
            echo "❌ 빌드 또는 배포 실패!"
        }
        success {
            echo "✅ 전체 파이프라인 완료!"
        }
    }
}