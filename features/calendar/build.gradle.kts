plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.KOTLIN_PARCELIZE)
}

android {
    namespace = "com.petapp.capybara.calendar"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 33
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

dependencies {
    // Project
    implementation(project(Modules.core))
    implementation(project(Modules.uicomponents))

    // Lifecycle
    implementation(Libraries.AndroidX.viewModelKtx)

    // Compose
    val composeBom = platform(Libraries.Compose.bom)
    implementation(composeBom)
    implementation(Libraries.Compose.materialDesign)
    implementation(Libraries.Compose.toolingPreview)
    implementation(Libraries.Compose.composeCalendar)
    implementation(Libraries.Compose.themeAdapter)

    // Dagger
    implementation(Libraries.Dagger.core)
    implementation(Libraries.Dagger.android)
    implementation(Libraries.Dagger.androidSupport)
    kapt(Libraries.Dagger.compiler)
    kapt(Libraries.Dagger.androidProcessor)

    // Material
    implementation(Libraries.Android.material)
}
