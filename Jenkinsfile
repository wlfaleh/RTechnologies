pipeline {
    agent any
    environment {
        DOCKER_REGISTRY = "localhost:8080"   // Harbor
        IMAGE_NAME = "rtechno-backend"
        IMAGE_TAG = "latest"
    }
    stages {
        stage('Checkout') {
            steps { git branch: 'main', url: 'https://github.com/TON_COMPTE/RTechnologies.git' }
        }
        stage('Build') {
            steps { sh 'mvn clean package -DskipTests' }
        }
        stage('Docker Build') {
            steps { sh "docker build -t ${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG} ." }
        }
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'harbor-credentials', usernameVariable: 'HARBOR_USER', passwordVariable: 'HARBOR_PASS')]) {
                    sh "echo $HARBOR_PASS | docker login ${DOCKER_REGISTRY} -u $HARBOR_USER --password-stdin"
                    sh "docker push ${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }
        stage('SonarQube Analysis') {
            environment {
                SONAR_TOKEN = credentials('sonarqube-token')
            }
            steps {
                sh "mvn sonar:sonar -Dsonar.projectKey=rtechno-backend -Dsonar.host.url=http://localhost:9000 -Dsonar.login=$SONAR_TOKEN"
            }
        }
    }
}
