/*
 * (©) Copyright 2019 Bluehexagon (https://bluehexagon.de/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *       Normen Düring
 *
 * Last modified 23.04.19 21:18
 *
 */

object Dependencies {
    const val android_gradle_plugin = "com.android.tools.build:gradle:${Versions.Gradle.androidGradlePlugin}"
    const val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin.kotlin}"
    const val dokka_gradle_plugin = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.Gradle.dokkaGradlePlugin}"


    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.Kotlin.kotlin}"
    const val kotlin_reflection = "org.jetbrains.kotlin:kotlin-reflect:${Versions.Kotlin.kotlin}"
    const val appcompat_v7 = "androidx.appcompat:appcompat:${Versions.Google.appcompat_v7}"

    //Testing Staff
    const val junit = "junit:junit:${Versions.Testing.junit}"
    const val ext_junit = "androidx.test.ext:junit:${Versions.Testing.extJunit}"
    const val test_runner = "androidx.test:runner:${Versions.Testing.testRunner}"
    const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.Testing.espressoCore}"
}