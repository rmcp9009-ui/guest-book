pipeline {
    environment {
        DOCKERHUB_CREDENTIALS = credentials('jenkins-dockerhub')
        JOB_URL = '10.0.2.4'
    }
    agent any
    
    tools {
        maven "MyMaven"
    }
    stages {
        stage('Build') {
            steps {
                git branch: 'main',
                url: 'https://github.com/rmcp9009-ui/guest-book.git'
            }
        }
        stage('Docker push') {
            steps {
                script {
                    sh 'echo "$DOCKERHUB_CREDENTIALS_PSW" | docker login -u "$DOCKERHUB_CREDENTIALS_USR" --password-stdin'
                    sh 'docker image build --tag rmcp9009/gbook:2.0 .'
                    sh 'docker push rmcp9009/gbook:2.0'
                }
            }
        }
        stage('Docker pull') {
            steps {
                sshagent( credentials: ['server-02'] ) {
                    // 원격 서버 안에서 안전하게 실행되도록 작은따옴표 구조 개선
                    sh '''
                    ssh -o StrictHostKeyChecking=no lastcoder@$JOB_URL '
                        docker stop guest-book || true
                        docker rm -f guest-book || true
                        docker image rm -f rmcp9009/gbook:2.0 || true
                        docker run --name="guest-book" -d -p 8080:8080 rmcp9009/gbook:2.0
                    '
                    '''
                }								
            }
        }
    }
}