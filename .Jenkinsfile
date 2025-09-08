pipeline {
    agent any

    environment {
        GIT_CREDENTIALS_ID = 'git-credentials'   // твои Git credentials
        INFLUXDB_TARGET = 'influxdb'            // имя target из Jenkins → Configure System → InfluxDB
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: env.GIT_CREDENTIALS_ID,
                    url: 'https://github.com/iarut/Jenkins_Graphana.git'
            }
        }

        stage('Build') {
            steps {
                // Используем Maven внутри Docker с OpenJDK 17
                script {
                    docker.image('maven:3.9.3-openjdk-17').inside {
                        sh 'mvn -Dmaven.test.failure.ignore=true clean package'
                    }
                }
            }
        }

        stage('Run') {
            steps {
                script {
                    // Запускаем jar после сборки
                    sh 'java -jar target/PostmanApplication-0.0.1-SNAPSHOT.jar &'
                }
            }
        }

        stage('Report') {
            steps {
                script {
                    influxDbPublisher(
                        selectedTarget: env.INFLUXDB_TARGET,
                        customData: null,
                        customDataMap: null,
                        customPrefix: null,
                        jenkinsEnvParameterField: null,
                        measurementName: null,
                        replaceDashWithUnderscore: true,
                        showRawBuildParameters: false
                    )
                }
            }
        }
    }

    post {
        failure {
            script {
                influxDbPublisher(selectedTarget: env.INFLUXDB_TARGET)
            }
        }
    }
}