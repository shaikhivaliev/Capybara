plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.KOTLIN_PARCELIZE)
}

android {
    namespace = "com.petapp.capybara.core"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 33

        @Suppress("UnstableApiUsage")
        compileOptions {
            // Flag to enable support for the new language APIs
            isCoreLibraryDesugaringEnabled = true

            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    // Desugar JdkLibs
    coreLibraryDesugaring(Libraries.AndroidTools.desugarJdkLibs)

    // Duplicate class com.google.common.util.concurrent.ListenableFuture
    implementation(Libraries.Android.guava)

    // Kotlin
    implementation(Libraries.Kotlin.stdLib)

    // AndroidX
    implementation(Libraries.AndroidX.appcompat)
    implementation(Libraries.AndroidX.coreKtx)

    // Lifecycle
    implementation(Libraries.AndroidX.viewModelKtx)

    // Material
    implementation(Libraries.Android.material)

    // Dagger
    implementation(Libraries.Dagger.core)
    implementation(Libraries.Dagger.android)
    implementation(Libraries.Dagger.androidSupport)
    kapt(Libraries.Dagger.compiler)
    kapt(Libraries.Dagger.androidProcessor)

    // Room
    implementation(Libraries.Room.runtime)
    annotationProcessor(Libraries.Room.compiler)
    kapt(Libraries.Room.compiler)
    implementation(Libraries.Room.ktx)
    implementation(Libraries.Room.testing)
    implementation(Libraries.Room.stetho)

    // Firebase
    implementation(Libraries.Firebase.firebaseCore)
    implementation(Libraries.Firebase.firebaseAuth)
    implementation(Libraries.Firebase.firebaseUiAuth)

    // Coroutines
    implementation(Libraries.Coroutines.coroutinesCore)
    implementation(Libraries.Coroutines.coroutinesAndroid)

    // Compose
    val composeBom = platform(Libraries.Compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(Libraries.Compose.materialDesign)
    implementation(Libraries.Compose.toolingPreview)
    implementation(Libraries.Compose.navigation)
    implementation(Libraries.Compose.themeAdapter)
    implementation(Libraries.Compose.glide)
}
