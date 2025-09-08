node {
    // Определяем переменные окружения
    env.GIT_CREDENTIALS_ID = 'git-credentials'  // ID твоих git credentials
    env.INFLUXDB_TARGET = 'influxdb'             // имя target из Jenkins → Configure System → InfluxDB

    def mvnHome

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
            mvnHome = tool 'maven3'
        }

        stage('Build') {
            dir('service-1') {
                sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore=true clean package"
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