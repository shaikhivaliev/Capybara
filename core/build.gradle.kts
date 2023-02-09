plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.KOTLIN_PARCELIZE)
}

android {
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
    // Kotlin
    implementation(Libraries.Kotlin.stdLib)

    // AndroidX
    implementation(Libraries.AndroidX.appcompat)
    implementation(Libraries.AndroidX.coreKtx)

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

    // Coroutines
    implementation(Libraries.Coroutines.coroutinesCore)
    implementation(Libraries.Coroutines.coroutinesAndroid)
}
