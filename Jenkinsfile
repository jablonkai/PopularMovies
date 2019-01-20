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
    stage('') {
      steps {
        sh 'wget --quiet --output-document=android-wait-for-emulator https://raw.githubusercontent.com/travis-ci/travis-cookbooks/0f497eb71291b52a703143c5cd63a217c8766dc9/community-cookbooks/android-sdk/files/default/android-wait-for-emulator'
      }
    }
  }
}