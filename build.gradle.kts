buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
    }
}

plugins {
    alias(libs.plugins.nexus.publish)
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

val isSnapshot = project.hasProperty("snapshot")

group = "com.gu.android"
version = "0.4.0" + if (isSnapshot) "-SNAPSHOT" else ""

nexusPublishing {
    repositories {
        sonatype {
            username.set(properties["ossrhUsername"] as? String)
            password.set(properties["ossrhPassword"] as? String)
        }
    }
}