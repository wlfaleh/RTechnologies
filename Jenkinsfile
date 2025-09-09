pipeline {
    agent any

    environment {
        IMAGE_NAME = "localhost:8080/demo/rtechno-backend" // Projet Harbor
        IMAGE_TAG = "latest"
        DOCKER_CREDENTIALS = credentials('harbor-creds') // Harbor
        SONAR_TOKEN = credentials('sonar-token')        // SonarQube
        GIT_CREDENTIALS = 'github-ssh-key'             // GitHub SSH Key
    }

    stages {
        stage('📥 Récupération du code') {
            steps {
                git branch: 'master',
                    url: 'git@github.com:wlfaleh/RTechnologies.git',
                    credentialsId: "${GIT_CREDENTIALS}"
            }
        }

        stage('⚙️ Compilation Maven') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('🧪 Tests unitaires') {
            steps {
                sh 'mvn test surefire-report:report'
            }
        }

        stage('📦 Build JAR') {
            steps {
                sh 'mvn package -DskipTests=false'
            }
        }

        stage('🐳 Build image Docker') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }

        stage('📤 Push image Docker vers Harbor') {
            steps {
                withDockerRegistry([credentialsId: "${DOCKER_CREDENTIALS}", url: 'http://localhost:8080']) {
                    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('🔍 Analyse SonarQube') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh 'mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}'
                }
            }
        }

        stage('🚀 Déploiement via Helm') {
            steps {
                sh """
                helm upgrade --install rtechno-backend ./rtechno-chart \
                    --set image.repository=${IMAGE_NAME} \
                    --set image.tag=${IMAGE_TAG}
                """
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo '✅ Pipeline terminé avec succès !'
        }
        failure {
            echo '❌ Pipeline échoué. Vérifier les logs.'
        }
    }
}
