pipeline {
  agent any
  stages {
    stage('update') {
      steps {
        sh '''        sh \'./gradlew clean\'
        sh \'./gradlew --refresh-dependencies\''''
      }
    }
    stage('build') {
      steps {
        sh '''        sh \'./gradlew assembleDebug\'
'''
      }
    }
    stage('test') {
      parallel {
        stage('test') {
          steps {
            sh '''        sh \'./gradlew testDebugUnitTest\'
'''
          }
        }
        stage('lint') {
          steps {
            sh '''        sh \'./gradlew lintDebug\'
'''
          }
        }
      }
    }
  }
}