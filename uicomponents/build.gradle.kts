plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
}

android {
    compileSdkVersion = "android-33"
    namespace = "com.petapp.capybara.uicomponents"
    defaultConfig {
        minSdk = 26
        @Suppress("UnstableApiUsage")
        targetSdk = 33

        @Suppress("UnstableApiUsage")
        vectorDrawables.useSupportLibrary = true

        @Suppress("UnstableApiUsage")
        buildFeatures {
            viewBinding = true
        }
    }
    @Suppress("UnstableApiUsage")
    buildFeatures {
        compose = true
    }

    @Suppress("UnstableApiUsage")
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
    implementation(Libraries.Compose.toolingPreview)
    implementation(Libraries.Compose.glide)
    implementation(Libraries.Compose.composeCalendar)
    implementation(Libraries.Compose.composeCalendarDateTime)
}
