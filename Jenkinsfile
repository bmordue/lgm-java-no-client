node {
  stage('Checkout') {
    checkout scm
  }

  def volumes = "-v ${WORKSPACE}:/opt/src -w /opt/src"
  def tag = "3-jdk-8-alpine"
  def image_name = "maven"

  def DOCKER_HOME = tool 'docker'
  def DOCKER_BIN = "${DOCKER_HOME}/bin/docker"

  stage ('Build') {
    docker.image("${image_name}:${tag}").inside("${volumes}") {
      sh "mvn clean package -DskipTests"
    }
  }
  
  stage ('Run tests') {
    docker.image("${image_name}:${tag}").inside("${volumes}") {
      sh "mvn test"
    }
  }

  stage ('Coverage') {
    echo 'NO COVERAGE YET'
  }

  stage ('Analysis') {
    def sonarProperties = "-v ${WORKSPACE}/conf:/root/sonar-scanner/conf"
    def sonarParams = ""
    if (env.BRANCH_NAME != 'master') {
        sonarParams = " -Dsonar.pullrequest.branch=${env.BRANCH_NAME}" +
            " -Dsonar.pullrequest.key=${env.CHANGE_ID}" +
            " -Dsonar.pullrequest.base=${env.CHANGE_TARGET}"
    }
    withCredentials([string(credentialsId: 'SONAR_LOGIN', variable: 'SONAR_LOGIN')]) {
      docker.image("newtmitch/sonar-scanner:3.2.0-alpine").inside("${volumes} ${sonarProperties}") {
        sh "sonar-scanner -Dsonar.login=${SONAR_LOGIN} ${sonarParams}"
      }
    }
  }

  stage ('Archive artifacts') {
//    archiveArtifacts artifacts: 'coverage/**/*,*xml', onlyIfSuccessful: true, allowEmptyArchive: true
  }
}
