pipeline {
    environment {
        DOCKERHUB_CREDENTIALS = credentials('jenkins-dockerhub')
        JOB_URL = '10.0.2.5'
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
                    // 보안 경고를 방지하기 위해 표준 stdin 로그인 방식 사용
                    sh 'echo "$DOCKERHUB_CREDENTIALS_PSW" | docker login -u "$DOCKERHUB_CREDENTIALS_USR" --password-stdin'
                    
                    // 계정명/레포지토리명 형식으로 통일 (rmcp9009)
                    sh 'docker image build --tag rmcp9009/guest-book:2.0 .'
                    sh 'docker push rmcp9009/guest-book:2.0'
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
                    docker image rm -f \$(docker image ls --filter reference='rmcp9009/guest-book' -q)
                    docker run --name="guest-book" -d -p 8080:8080 rmcp9009/guest-book:2.0
                    '												
                    """
                }								
            }
        }
    }
}