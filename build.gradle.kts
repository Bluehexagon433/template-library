// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val artifactoryContextUrl = properties.getValue("artifactory_contextUrl") as String
    val artifactoryUser = properties.getValue("artifactory_user") as String
    val artifactoryPassword = properties.getValue("artifactory_password") as String
    repositories {
        mavenLocal()
        maven {
            url = uri("$artifactoryContextUrl/libs-release")
            credentials {
                username = artifactoryUser
                password = artifactoryPassword
            }
        }
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.android_gradle_plugin)
        classpath(Dependencies.kotlin_gradle_plugin)
        classpath(Dependencies.dokka_gradle_plugin)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType<Wrapper>().configureEach {
    distributionType = Wrapper.DistributionType.ALL
}

allprojects {
    val artifactoryContextUrl = properties.getValue("artifactory_contextUrl") as String
    val artifactoryUser = properties.getValue("artifactory_user") as String
    val artifactoryPassword = properties.getValue("artifactory_password") as String
    repositories {
        mavenLocal()
        maven {
            url = uri("$artifactoryContextUrl/libs-local")
            credentials {
                username = artifactoryUser
                password = artifactoryPassword
            }
        }
        maven {
            url = uri("$artifactoryContextUrl/libs-release")
            credentials {
                username = artifactoryUser
                password = artifactoryPassword
            }
        }
    }
}
