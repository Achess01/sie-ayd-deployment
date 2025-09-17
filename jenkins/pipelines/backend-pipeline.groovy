pipeline {
    agent any
    
    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        BACKEND_REPO = 'git@github.com:Achess01/sie-ayd.git'
    }
    
    stages {
        stage('Checkout Deployment Repo') {
            steps {
                checkout scm
            }
        }
        
        stage('Checkout Backend Code') {
            steps {
                dir('backend') {
                    git branch: 'main', url: "${BACKEND_REPO}", credentialsId: 'ssh-jenkins'
                }
            }
        }
        
        // stage('Build and Test Backend') {
        //     steps {
        //         dir('backend') {
        //             sh './gradlew clean test --no-daemon'
        //         }
        //     }
        // }
        
        stage('Build Docker Images') {
            steps {
                sh 'docker compose build --no-cache backend'
            }
        }
        
        stage('Deploy Application') {
            steps {
                sh 'docker compose down backend || true'
                sh 'docker compose up -d backend'
            }
        }
    }
    post {
        always {
            sh 'docker compose logs'
        }
        failure {
            sh 'docker compose down'
        }
    }
}
