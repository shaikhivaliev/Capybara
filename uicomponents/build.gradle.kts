plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
}

@Suppress("UnstableApiUsage")
android {
    namespace = "com.petapp.capybara.uicomponents"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

dependencies {
    // AndroidX
    implementation(Libraries.AndroidX.constraintLayout)

    // Material
    implementation(Libraries.Android.material)

    // Compose
    val composeBom = platform(Libraries.Compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(Libraries.Compose.materialDesign)
    implementation(Libraries.Compose.glide)
    implementation(Libraries.Compose.composeCalendar)
}
