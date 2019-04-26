import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.api.LibraryVariant
import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.library")
    id("org.jetbrains.dokka")
    `maven-publish`
    kotlin("android")
}
apply(from = "./customGradle/versioning.gradle")
val buildVersionCode: groovy.lang.Closure<Int?> by extra

val artifactoryContextUrl = properties.getValue("artifactory_contextUrl") as String
val artifactoryUser = properties.getValue("artifactory_user") as String
val artifactoryPassword = properties.getValue("artifactory_password") as String

val injectVariable: Map<String, String> = System.getenv()
var buildNumber = injectVariable["BUILD_NUMBER"] ?: properties.getValue("build_count") as String
var libraryPackageName = injectVariable["PACKAGE_NAME"]
        ?: properties.getValue("library_package_name") as String
var libraryVersion = injectVariable["LIBRARY_VERSION"]
        ?: properties.getValue("library_version") as String
var suffixSnapshot = injectVariable["SUFFIX_SNAPSHOT"]
        ?: properties.getValue("suffix_snapshot") as String

android {
    compileSdkVersion(AppSettings.maxSdk)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    libraryVariants.all(object : Action<LibraryVariant> {
        override fun execute(variant: LibraryVariant) {
            variant.outputs.all(object : Action<BaseVariantOutput> {
                override fun execute(output: BaseVariantOutput) {
                    val outputImpl = output as BaseVariantOutputImpl
                    val fileName: String = if (output.outputFileName.endsWith("-debug.aar")) {
                        output.outputFileName.replace("-debug", "-${defaultConfig.versionName}.$buildNumber-SNAPSHOT")
                    } else {
                        output.outputFileName.replace("-release", "-${defaultConfig.versionName}-release")
                    }
                    outputImpl.outputFileName = fileName
                }
            })
        }
    })

    defaultConfig {
        minSdkVersion(AppSettings.minSdk)
        targetSdkVersion(AppSettings.targetSdk)
        versionCode = buildVersionCode()
        println("VersionCode:  $versionCode")
        versionName = libraryVersion
        println("VersionName:  $versionName")
        consumerProguardFiles("proguard.txt")
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Dependencies.kotlin_stdlib)
    implementation(Dependencies.kotlin_reflection)
    implementation(Dependencies.appcompat_v7)

    //Testing Staff
    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.test_runner)
    androidTestImplementation(Dependencies.espresso_core)
}

publishing {
    publications {
        register(project.name, MavenPublication::class) {
            groupId = libraryPackageName
            version = if (isSnapshotUpload()) {
                "$libraryVersion.$buildNumber$suffixSnapshot"
            } else {
                libraryVersion
            }
            // Tell maven to prepare the generated "*.aar" file for publishing
            artifact("$buildDir/outputs/aar/${project.name + "-" + version}.aar")
            pom {
                name.set("Android Template Library")
                description.set("A Android template library.")
                url.set("https://github.com/Bluehexagon433/template-library")
                properties.set(mapOf(
                        "qa.level" to "basic",
                        "dev.team" to "core"
                ))
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("nduering")
                        name.set("Normen Düring")
                        email.set("developer@bluehexagon.de")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:Bluehexagon433/template-library.git")
                    developerConnection.set("scm:git@github.com:Bluehexagon433/template-library.git")
                    url.set("https://github.com/Bluehexagon433/template-library")
                }
            }
        }
    }
    repositories {
        maven {
            // change URLs to point to your repos, e.g. http://my.org/repo
            val releasesRepoUrl = uri("$artifactoryContextUrl/libs-release-local")
            val snapshotsRepoUrl = uri("$artifactoryContextUrl/libs-snapshot-local")
            url = if (isSnapshotUpload()) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = artifactoryUser
                password = artifactoryPassword
            }
        }
    }
}

/**
 * check is snapshot
 * @return true is a snapshot
 */
fun isSnapshotUpload(): Boolean {
    val directory = File("$buildDir/outputs/aar/")
    var found = false
    val files = directory.listFiles()
    files?.let { arrayOfFiles ->
        for (file in arrayOfFiles) {
            if (file.isFile && file.name.endsWith("-SNAPSHOT.aar")) {
                found = true
                break
            }
        }
    }
    return found
}
