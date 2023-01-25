plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(33)
    defaultConfig {
        applicationId = "com.gu.toolargetool.sample"
        versionCode = 2
        versionName = "1.1"
        minSdk = 15
        targetSdk = 33
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        val debug by getting {
            isMinifyEnabled = false
        }
        val release by getting {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    val kotlinVersion: String by project
    val appcompatVersion: String by project
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation(project(path = ":toolargetool"))
}
