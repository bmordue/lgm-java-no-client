node {
    stage('Checkout') {
        deleteDir()
        checkout scm
    }

    def volumes = "-v ${WORKSPACE}:/opt/src -w /opt/src"
    def tag = "3-jdk-8-alpine"
    def image_name = "maven"

    def DOCKER_HOME = tool 'docker'
    def DOCKER_BIN = "${DOCKER_HOME}/bin/docker"
    def MVN = "mvn -e -B"

    try {
        stage ('Build') {
            docker.image("${image_name}:${tag}").inside("${volumes}") {
                sh "${MVN} clean package -DskipTests > mvn_package.log 2>&1"
            }
        }

        stage ('Run tests') {
            docker.image("${image_name}:${tag}").inside("${volumes}") {
                sh "${MVN} test > mvn_test.log 2>&1"
            }
        }

        stage ('Coverage') {
            deleteDir()
            checkout scm
            docker.image("${image_name}:${tag}").inside("${volumes}") {
                sh "${MVN} clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=false > mvn_coverage.log 2>&1"
            }
        }

        stage ('Analysis') {
            def sonarProperties = "-v ${WORKSPACE}/conf:/root/sonar-scanner/conf" // doesn't appear to be working...
            def sonarParams = "-Dsonar.host.url=https://sonarcloud.io -Dsonar.projectKey=bmordue_lgm-java-no-client" +
                " -Dsonar.organization=bmordue-github -Dsonar.java.binaries=./target/ -Dsonar.coverage.jacoco.xmlReportPaths=target/test-results/coverage/jacoco/jacoco.xml" +
                " -Dsonar.tests=src/test -Dsonar.sources=src/main"
            def sonarExtraParams = ""

            if (env.BRANCH_NAME != 'master') {
                sonarExtraParams = "-Dsonar.pullrequest.branch=${env.BRANCH_NAME} -Dsonar.pullrequest.key=${env.CHANGE_ID} -Dsonar.pullrequest.base=${env.CHANGE_TARGET}"
            }
            withCredentials([string(credentialsId: 'SONAR_LOGIN', variable: 'SONAR_LOGIN')]) {
                docker.image("newtmitch/sonar-scanner:3.2.0-alpine").inside("${volumes} ${sonarProperties}") {
                    sh "sonar-scanner -Dsonar.login=${SONAR_LOGIN} ${sonarParams} ${sonarExtraParams} > sonar_scanner.log 2>&1"
                }
            }
        }

    } catch (e) {
        stage ('Archive artifacts') {
            archiveArtifacts artifacts: '*log,**/surefire-reports/*xml', onlyIfSuccessful: false, allowEmptyArchive: true
            junit testResults: 'target/surefire-reports/*xml', allowEmptyResults: true
            throw e
        }
    }
}
