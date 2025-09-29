pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        FRONTEND_REPO = 'git@github.com:Pinto324/sie-ayd-frontend.git'
        COMPOSE_PROJECT_NAME = 'sie-ayd-deployment'
    }

    stages {
        stage('Checkout Deployment Repo') {
            steps {
                checkout scm
            }
        }

        stage('Checkout Frontend Code') {
            steps {
                dir('frontend') {
                    git branch: 'master', url: "${FRONTEND_REPO}", credentialsId: 'ssh-jenkins'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker compose -p ${COMPOSE_PROJECT_NAME} build --no-cache frontend'
            }
        }

        stage('Deploy Application') {
            steps {
                sh 'docker compose -p ${COMPOSE_PROJECT_NAME} stop frontend || true'
                sh 'docker compose -p ${COMPOSE_PROJECT_NAME} rm -f frontend || true'
                sh 'docker compose -p ${COMPOSE_PROJECT_NAME} up -d frontend'
            }
        }

    }
    
    post {
        always {
            sh 'docker compose -p ${COMPOSE_PROJECT_NAME} logs frontend || true'
        }
        failure {
            sh 'docker compose -p ${COMPOSE_PROJECT_NAME} logs frontend || true'
        }
        success {
            sh 'echo "Deployment successful!"'
        }
    }
}