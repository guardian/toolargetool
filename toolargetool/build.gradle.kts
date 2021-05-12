import java.net.URI

plugins {
    id("com.android.library")
    kotlin("android")
    id("signing")
    id("maven-publish")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(30)
        versionCode(1)
        versionName("1.0")
        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
        consumerProguardFile("proguard-rules.pro")
    }
}

dependencies {
    val kotlinVersion: String by project
    val appcompatVersion: String by project
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    androidTestImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.0")
    androidTestImplementation("androidx.test:rules:1.1.1")
    androidTestImplementation("org.mockito:mockito-core:2.24.0")
    androidTestImplementation("com.nhaarman:mockito-kotlin-kt1.1:1.5.0")
}

val sourcesJar by tasks.creating(Jar::class) {
    from(android.sourceSets["main"].java.srcDirs)
    archiveClassifier.set("sources")
}

val javadocJar by tasks.creating(Jar::class) {
    // There are no docs here
    archiveClassifier.set("javadoc")
}

afterEvaluate {
    publishing {
        publications {
            val release by creating(MavenPublication::class) {
                from(components["release"])
                artifact(sourcesJar)
                artifact(javadocJar)

                groupId = "com.gu.android"
                artifactId = "toolargetool"
                version = "0.3.0"

                pom {
                    name.set("toolargetool")
                    description.set("A tool for debugging TransactionTooLargeException on Android")
                    url.set("https://github.com/guardian/toolargetool")
                    licenses {
                        license {
                            name.set("The MIT License (MIT)")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/guardian/toolargetool.git")
                        developerConnection.set("scm:git:ssh://github.com/guardian/toolargetool.git")
                        url.set("https://github.com/guardian/toolargetool")

                    }
                    developers {
                        developer {
                            id.set("maxspencer")
                            name.set("Max Spencer")
                            email.set("max.spencer@guardian.co.uk")
                            url.set("https://github.com/maxspencer")
                            organization.set("The Guardian")
                            organizationUrl.set("https://theguardian.com")
                        }
                    }
                }
            }
        }

        repositories {
            val ossrhUsername = properties["ossrhUsername"] as? String
                    ?: System.getenv("OSSRH_USERNAME")
            val ossrhPassword = properties["ossrhPassword"] as? String
                    ?: System.getenv("OSSRH_PASSWORD")
            maven {
                name = "snapshot"
                url = URI.create("https://oss.sonatype.org/content/repositories/snapshots/")
                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
            maven {
                name = "staging"
                url = URI.create("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = ossrhUsername
                    password = ossrhPassword
                }
            }
        }
    }

    signing {
        sign(publishing.publications["release"])
    }
}
