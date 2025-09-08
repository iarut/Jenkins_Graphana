node {
    // Определяем переменные окружения
    env.GIT_CREDENTIALS_ID = 'git-credentials'  // ID твоих git credentials
    env.INFLUXDB_TARGET = 'influxdb'             // имя target из Jenkins → Configure System → InfluxDB


    try {
        stage('Checkout') {
            checkout([
                $class: 'GitSCM',
                branches: [[name: '*/main']],
                userRemoteConfigs: [[
                    url: 'https://github.com/iarut/Jenkins_Graphana.git',
                    credentialsId: env.GIT_CREDENTIALS_ID
                ]]
            ])

        }

        stage('Build') {
            dir('.') { // убедись, что это корень проекта с pom.xml
                sh "mvn -Dmaven.test.failure.ignore=true clean package"

                // Выводим содержимое папки target для отладки
                sh "ls -l target"

                // Проверяем, что jar действительно создан
                if (!fileExists('target/PostmanApplication-0.0.1-SNAPSHOT.jar')) {
                    error "Jar file target/PostmanApplication-0.0.1-SNAPSHOT.jar not found! Build failed."
                } else {
                    echo "Jar file successfully created."
                }
            }
        }

        stage('Run') {
            dir('.') {
                // Запускаем jar только если он существует
                if (fileExists('target/PostmanApplication-0.0.1-SNAPSHOT.jar')) {
                    sh "java -jar target/PostmanApplication-0.0.1-SNAPSHOT.jar"
                } else {
                    error "Cannot run: jar file does not exist."
                }
            }
        }

        stage('Report') {
            if (currentBuild.currentResult == 'UNSTABLE') {
                currentBuild.result = "UNSTABLE"
            } else {
                currentBuild.result = "SUCCESS"
            }

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
    } catch (Exception e) {
        currentBuild.result = "FAILURE"
        influxDbPublisher(
            selectedTarget: env.INFLUXDB_TARGET
        )
    }
}