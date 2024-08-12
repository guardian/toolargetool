// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        val kotlinVersion: String by project
        classpath("com.android.tools.build:gradle:8.5.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

plugins {
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

val isSnapshot = project.hasProperty("snapshot")

group = "com.gu.android"
version = "0.3.1" + if (isSnapshot) "-SNAPSHOT" else ""

nexusPublishing {
    repositories {
        sonatype {
            username.set(properties["ossrhUsername"] as? String)
            password.set(properties["ossrhPassword"] as? String)
        }
    }
}