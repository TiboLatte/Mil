plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt") // <-- ajoute ceci pour Room

}

android {
    namespace = "com.tibolatte.milbadge"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.tibolatte.milbadge"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }

    composeOptions{
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
dependencies {
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.work.runtime.ktx) // version stable actuelle
    kapt("androidx.room:room-compiler:2.7.2") // annotation processor
    implementation(libs.androidx.datastore.preferences)

    // Optional - Kotlin extensions and Coroutines support
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.bom.v20240600)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Navigation Compose (unique)
    implementation(libs.androidx.navigation.compose)

    // Lottie pour animations confettis / coeurs
    implementation(libs.lottie.compose)

    // Accompanist (optionnel pour system UI, status bar)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.room.common.jvm)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}