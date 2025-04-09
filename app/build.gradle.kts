plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinAndroidKsp)
}

android {
    namespace = "com.lczarny.lsnplanner"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.lczarny.lsnplanner"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.lczarny.lsnplanner.AppTestRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    ksp(libs.hilt.compiler)
    ksp(libs.room.compiler)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)

    implementation(libs.android.material)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.animation.graphics)
    implementation(libs.androidx.compose.fonts)

    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)

    implementation(libs.androidx.datastore)

    implementation(libs.bundles.compose)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt)
    implementation(libs.androidx.hilt.compose)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    kspTest(libs.hilt.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.hilt.android.testing)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.compose.test.junit4)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.mockk)

    debugImplementation(libs.androidx.compose.test.manifest)
}