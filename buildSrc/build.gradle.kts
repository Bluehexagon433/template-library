plugins {
    `kotlin-dsl`
}

repositories {
    val artifactoryContextUrl = properties.getValue("artifactory_contextUrl") as String
    val artifactoryUser = properties.getValue("artifactory_user") as String
    val artifactoryPassword = properties.getValue("artifactory_password") as String
    mavenLocal()
    maven {
        url = uri("$artifactoryContextUrl/libs-release")
        credentials {
            username = artifactoryUser
            password = artifactoryPassword
        }
    }
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}