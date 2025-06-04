plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //id("com.android.application")
    //id("org.jetbrains.kotlin.android")
    //id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.memooapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.memooapp"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //implementation(libs.androidx.navigation.compose.jvmstubs)
    implementation(libs.androidx.storage)

    // Room 核心库
    implementation("androidx.room:room-runtime:2.6.1")
    // Kotlin DSL (build.gradle.kts) 示例：
    implementation("androidx.compose.material:material-icons-extended:1.4.3")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    // Room 注解处理器 (KAPT)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.compose.ui:ui")
    // 可选 - Kotlin 扩展和协程支持
    implementation("androidx.room:room-ktx:2.6.1")
    //implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.runtime:runtime-livedata")
    //implementation("androidx.compose.runtime:runtime-livedata:1.6.3")
    //implementation("androidx.compose.ui:ui:1.6.3")
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))
    //implementation(androidx.navigation:navigation-compose:2.7.5)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}