node {
  stage('Checkout') {
    checkout scm
  }

  def volumes = "-v ${WORKSPACE}:/opt/src -w /opt/src"
  def tag = "3-jdk-8-alpine"
  def image_name = "maven"

  def DOCKER_HOME = tool 'docker'
  def DOCKER_BIN = "${DOCKER_HOME}/bin/docker"
  def MVN = "mvn -e -B"

// TODO fix formatting
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
    echo 'NO COVERAGE YET'
  }

  stage ('Analysis') {
    def sonarProperties = "-v ${WORKSPACE}/conf:/root/sonar-scanner/conf" // doesn't appear to be working...
    def sonarParams = "-Dsonar.host.url=https://sonarcloud.io -Dsonar."
                    + "-Dsonar.projectKey=bmordue_lgm-java-no-client"
                    + "-Dsonar.organization=bmordue-github"

    if (env.BRANCH_NAME != 'master') {
        sonarParams += " -Dsonar.pullrequest.branch=${env.BRANCH_NAME}" +
            " -Dsonar.pullrequest.key=${env.CHANGE_ID}" +
            " -Dsonar.pullrequest.base=${env.CHANGE_TARGET}"
    }
    withCredentials([string(credentialsId: 'SONAR_LOGIN', variable: 'SONAR_LOGIN')]) {
      docker.image("newtmitch/sonar-scanner:3.2.0-alpine").inside("${volumes} ${sonarProperties}") {
        sh "sonar-scanner -Dsonar.login=${SONAR_LOGIN} ${sonarParams}"
      }
    }
  }

} catch (e) {
  stage ('Archive artifacts') {
    archiveArtifacts artifacts: '*log', onlyIfSuccessful: false, allowEmptyArchive: true

    throw e
  }

}


}
