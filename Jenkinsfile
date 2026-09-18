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
                    // 표준 로그인 방식
                    sh 'echo "$DOCKERHUB_CREDENTIALS_PSW" | docker login -u "$DOCKERHUB_CREDENTIALS_USR" --password-stdin'
                    
                    // 이미지 빌드 및 푸시 (실제 레포지토리인 rmcp9009/gbook 사용)
                    sh 'docker image build --tag rmcp9009/gbook:2.0 .'
                    sh 'docker push rmcp9009/gbook:2.0'
                }
            }
        }
        stage('Docker pull') {
            steps {
                sshagent( credentials: ['server-02'] ) {
                    // 호스트 키 검증 생략 옵션 추가 및 문법 오류 수정 완료
                    sh '''
                    ssh -o StrictHostKeyChecking=no lastcoder@$JOB_URL "
                        docker stop guest-book || true
                        docker container rm -f \$(docker container ls -af 'name=guest-book' -q) || true
                        docker image rm -f \$(docker image ls --filter reference='rmcp9009/gbook' -q) || true
                        docker run --name='guest-book' -d -p 8080:8080 rmcp9009/gbook:2.0
                    "
                    '''
                }								
            }
        }
    }
}