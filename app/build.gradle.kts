import java.util.Properties

val secretsProps = Properties().apply {
    val file = rootProject.file("secrets.properties")
    if (file.exists()) {
        load(file.inputStream())
    }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android.plugin)

}

android {
    namespace = "com.sword.cats"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sword.cats"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "API_KEY",
            "\"${secretsProps["API_KEY"] ?: ""}\""
        )

        buildConfigField(
            "String",
            "BASE_URL",
            "\"${secretsProps["BASE_URL"] ?: ""}\""
        )
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
    kotlin {
        jvmToolchain(21)
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }
}

dependencies {
    //Base
    implementation(libs.androidx.core.ktx)

    //Compose
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.ui)
    debugImplementation(libs.androidx.ui.tooling)

    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    //OkHttp
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    //Retrofit
    implementation(platform(libs.retrofit.bom))
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)

    //Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //Tests
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(kotlin("test"))
}