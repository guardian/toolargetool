plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.gu.toolargetool.sample"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.gu.toolargetool.sample"
        versionCode = 1
        versionName = "1.0"
        minSdk = 21
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.appcompat)
    implementation(project(path = ":toolargetool"))
}