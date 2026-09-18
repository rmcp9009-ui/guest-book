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
                url: 'https://github.com/rmcp9009-ui/guest-book.git' // 1️⃣ GitHub 주소 변경
            }
        }
        stage('Docker push') {
            steps {
                sh "docker login -u $DOCKERHUB_CREDENTIALS_USR -p $DOCKERHUB_CREDENTIALS_PSW"
                sh 'docker image build --tag rmcp9009-ui/guest-book:2.0 .' // 2️⃣ 빌드 태그 변경
                sh 'docker push rmcp9009-ui/guest-book:2.0'                 // 3️⃣ 푸시 이미지 변경
            }
        }
        stage('Docker pull') {
            steps {
                sshagent( credentials: ['server-02'] ) {
                sh """
                ssh lastcoder@$JOB_URL '
                docker stop guest-book
                docker container rm -f \$(docker container ls -af "name=guest-book" -q)
                docker image rm -f \$(docker image ls --filter reference='rmcp9009-ui/guest-book' -q)  // 4️⃣ 삭제 대상 필터 변경
                docker run --name="guest-book" -d -p 8080:8080 rmcp9009-ui/guest-book:2.0          // 5️⃣ 실행 이미지 변경
                '												
                """
                }								
            }
        }
    }
}