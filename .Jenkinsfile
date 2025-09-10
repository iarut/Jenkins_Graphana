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

//         stage('Build') {
//             steps {
//                 // Используем Maven внутри Docker с OpenJDK 17
//                 script {
//                     docker.image('maven:3.8.5-openjdk-17').inside {
//                         sh 'mvn -Dmaven.test.failure.ignore=true clean package'
//                     }
//                 }
//             }
//         }

//         stage('Install Docker Compose') {
//             steps {
//                 script {
//                     // Скачиваем бинарник
//                     sh '''
//                     curl -L "https://github.com/docker/compose/releases/download/v2.39.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
//                     chmod +x /usr/local/bin/docker-compose
//                     '''
//
//                     // Добавляем путь в PATH на случай, если Jenkins не видит бинарник
//                     env.PATH = "/usr/local/bin:${env.PATH}"
//
//                     // Проверяем установку
//                     sh 'docker-compose --version'
//                 }
//             }
//         }

        stage('Check Jenkins Permissions') {
            steps {
                script {
                    echo "=== Проверка пользователя ==="
                    def user = sh(script: 'whoami', returnStdout: true).trim()
                    echo "Jenkins выполняется от пользователя: ${user}"

                    echo "=== Проверка групп пользователя ==="
                    def groupsList = sh(script: 'groups', returnStdout: true).trim()
                    echo "Группы пользователя: ${groupsList}"

                    // Проверка доступности Docker
                    try {
                        sh 'docker --version'
                    } catch (Exception e) {
                        echo "Docker недоступен! Установите Docker или добавьте пользователя Jenkins в группу docker."
                    }

                    // Проверка доступности docker-compose
                    try {
                        sh '/usr/local/bin/docker-compose --version'
                    } catch (Exception e) {
                        echo "docker-compose недоступен! Убедитесь, что путь /usr/local/bin/docker-compose добавлен в PATH."
                    }

                    // Проверка прав на выполнение Docker команд
                    def dockerPsSuccess = sh(script: 'docker ps', returnStatus: true)
                    if (dockerPsSuccess != 0) {
                        echo "У пользователя Jenkins нет прав на выполнение Docker команд."
                        echo "Решение: добавить пользователя Jenkins в группу docker и перезапустить Jenkins контейнер."
                    } else {
                        echo "Права на выполнение Docker команд проверены успешно."
                    }
                }
            }
        }

        stage('Build 1') {
                    steps {
                        sh 'docker build -t myapp -f Dockerfile_app .'
                    }
        }

        stage('Run'){
        steps {}
                sh 'docker run -p 8081:8080 myapp'
                sh 'sleep 15'
            }

//         stage('Run') {
//             steps {
//                 script {
//                     // Запускаем jar после сборки
//                     sh 'java -jar target/*.jar &'
//                     sh 'sleep 15'
//                 }
//             }
//         }

        stage('Check') {
                    steps {
                        // Проверка доступности контроллера
                        sh 'sleep 15'
                        sh 'curl -I http://localhost:8081/api/v1/products'
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