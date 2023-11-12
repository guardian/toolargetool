plugins {
    id("com.android.library")
    kotlin("android")
    id("signing")
    id("maven-publish")
}

android {
    namespace = "com.gu.toolargetool.sample.lib"
    compileSdk = 33
    defaultConfig {
        minSdk = 15
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFile("proguard-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    val kotlinVersion: String by project
    val appcompatVersion: String by project
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
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
        val isSnapshot = project.hasProperty("snapshot")

        publications {
            val release by creating(MavenPublication::class) {
                from(components["release"])
                artifact(sourcesJar)
                artifact(javadocJar)

                groupId = "com.gu.android"
                artifactId = "toolargetool"
                version = "0.3.0" + if (isSnapshot) "-SNAPSHOT" else ""

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
            maven {
                name = "Sonatype"
                url = uri("https://oss.sonatype.org" + if(isSnapshot) {
                    "/content/repositories/snapshots/"
                } else {
                    "/service/local/staging/deploy/maven2/"
                })
                credentials {
                    username = properties["ossrhUsername"] as? String
                    password = properties["ossrhPassword"] as? String
                }
            }
        }
    }

    signing {
        if (project.hasProperty("useInMemoryPgpKeys")) {
            val signingKey: String? by project
            val signingKeyPassword: String? by project
            if (signingKey.isNullOrEmpty() || signingKeyPassword.isNullOrEmpty()) {
                logger.log(LogLevel.ERROR, "The useInMemoryPgpKeys property was set but signingKey and/or signingKeyPassword are missing")
            }
            useInMemoryPgpKeys(signingKey, signingKeyPassword)
        }
        sign(publishing.publications["release"])
    }
}
