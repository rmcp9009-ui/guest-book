pipeline {
    environment {
        DOCKERHUB_CREDENTIALS = credentials('jenkins-dockerhub')
        JOB_URL = '10.0.2.4' // 🔴 '10.0.2.5'에서 '10.0.2.4'로 변경
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
                    sh """
                    ssh lastcoder@$JOB_URL '
                    docker stop guest-book
                    docker container rm -f \$(docker container ls -af "name=guest-book" -q)
                    docker image rm -f \$(docker image ls --filter reference='rmcp9009/gbook' -q)
                    docker run --name="guest-book" -d -p 8080:8080 rmcp9009/gbook:2.0
                    '												
                    """
                }								
            }
        }
    }
}