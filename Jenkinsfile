pipeline {
    agent { dockerfile true }
    stages {
        stage("Check code quality") {
            steps {
                sh 'mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=030722-VA-SRE_team-epimetheus'
            }
        }
        stage("Maven clean package") {
            steps {
                sh 'mvn clean package'
            }
        }
        stage("Docker build") {
            steps {
                sh 'docker build -t cunananan/spellbook:latest .'
            }
        }
        stage("Push image to DockerHub") {
            steps {

            }
        }
        stage("Await approval") {
            steps {

            }
        }
        stage("Deploy to Kubernetes") {
            steps {

            }
        }
    }
    // post {
    //     always {

    //     }
    //     success {

    //     }
    //     failure {

    //     }
    // }
}