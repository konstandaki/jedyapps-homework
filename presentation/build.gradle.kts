plugins {
    id("com.android.library")
    kotlin("android")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.konstandaki.jedyapps.presentation"
    compileSdk = 36
    defaultConfig { minSdk = 27 }
    buildFeatures { compose = true }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(project(":domain"))
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.hilt.navigation.compose)
}