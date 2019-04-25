#!groovy
def branchName = env.BRANCH_NAME
def credentialsId = 'b910c0f1-6dac-4947-9152-7e1b0b7bc5ec'
def gitRepo = 'git@github.com:Bluehexagon433/template-library.git'
def repoAlias = "origin"

node('apk-builder') {

    stage('Checkout Source Code') {
        //branch name from Jenkins environment variables
        echo "My branch is: ${env.BRANCH_NAME}"
        checkout(
                [
                        $class                           : 'GitSCM',
                        branches                         : [
                                [
                                        name: "$env.BRANCH_NAME"
                                ]
                        ],
                        browser                          : [
                                $class : 'GithubWeb',
                                repoUrl: 'https://github.com/Bluehexagon433/template-library'
                        ],
                        doGenerateSubmoduleConfigurations: false,
                        extensions                       : [
                                [
                                        $class: 'LocalBranch', localBranch: "$env.BRANCH_NAME"
                                ]
                        ],
                        submoduleCfg                     : [

                        ],
                        userRemoteConfigs                : [
                                [
                                        credentialsId: "$credentialsId", name: "$repoAlias", url: "$gitRepo"
                                ]
                        ]
                ]
        )
    }

    stage('Clean') {
        sh './gradlew :library:clean'
    }

    // Pipeline for develop and feature branches
    if (branchName == 'develop') {
        runDevelopPipeline()
    } else if (branchName == 'master') {
        runMasterPipeline()
    } else if (branchName.startsWith('feature/')) {
        runDevelopPipeline()
    }

}

def runDevelopPipeline() {
    stage('Assemble Debug') {
        sh "./gradlew :library:assembleDebug"
    }
    stage('upload to libs-snapshot-local') {
        sh "./gradlew :library:publish"
    }
}

def runMasterPipeline() {
    stage('Assemble Release') {
        sh "./gradlew :library:assembleRelease"
    }
    stage('upload to libs-release-local') {
        sh "./gradlew :library:publish"
    }
}
