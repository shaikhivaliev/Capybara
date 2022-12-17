import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id(ANDROID_APPLICATION_PLUGIN)
    id(GOOGLE_SERVICES)
    id(CHECK_DEPENDENCY_UPDATES_PLUGIN)
    id(KOTLIN_ANDROID_PLUGIN)
    id(KOTLIN_KAPT_PLUGIN)
    id(KOTLIN_PARCELIZE_PLUGIN)
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.petapp.capybara"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"

        vectorDrawables.useSupportLibrary = true

        @Suppress("UnstableApiUsage")
        buildFeatures {
            viewBinding = true
        }

        @Suppress("UnstableApiUsage")
        compileOptions {
            // Flag to enable support for the new language APIs
            isCoreLibraryDesugaringEnabled = true

            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("keystore/my_app_keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("RELEASE_SIGN_KEY_ALIAS")
            keyPassword = System.getenv("RELEASE_SIGN_KEY_PASSWORD")

        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }
}

tasks.named("check").dependsOn("ktlintCheck")

dependencies {
    // Desugar JdkLibs
    coreLibraryDesugaring(Libraries.AndroidTools.desugarJdkLibs)

    // Duplicate class com.google.common.util.concurrent.ListenableFuture
    implementation(Libraries.Android.guava)

    // Kotlin
    implementation(Libraries.Kotlin.stdLib)

    // Android X
    implementation(Libraries.AndroidX.appcompat)
    implementation(Libraries.AndroidX.coreKtx)
    implementation(Libraries.AndroidX.preferenceKtx)
    implementation(Libraries.AndroidX.constraintLayout)
    implementation(Libraries.AndroidX.viewPager)

    // Navigation
    implementation(Libraries.Navigation.fragmentKtx)
    implementation(Libraries.Navigation.uiKtx)

    // Lifecycle
    implementation(Libraries.AndroidX.viewModelKtx)
    implementation(Libraries.AndroidX.lifecycle)

    // Material
    implementation(Libraries.Android.material)

    // ViewBinding property delegate
    implementation(Libraries.ViewBinding.viewBindingPropertyDelegate)

    // Dagger
    implementation(Libraries.Dagger.core)
    implementation(Libraries.Dagger.android)
    implementation(Libraries.Dagger.androidSupport)
    kapt(Libraries.Dagger.compiler)
    kapt(Libraries.Dagger.androidProcessor)

    // Recycler
    implementation(Libraries.AdapterDelegates.layoutContainer)
    implementation(Libraries.AdapterDelegates.adapterDelegates)
    implementation(Libraries.AdapterDelegates.viewBinding)

    // Room
    implementation(Libraries.Room.runtime)
    implementation(Libraries.Room.ktx)
    implementation(Libraries.Room.compiler)
    implementation(Libraries.Room.testing)
    implementation(Libraries.Room.stetho)

    // Image
    implementation(Libraries.Glide.glide)

    // Firebase
    implementation(Libraries.Firebase.firebaseCore)
    implementation(Libraries.Firebase.firebaseAuth)
    implementation(Libraries.Firebase.firebaseUiAuth)

    // Material dialogs
    implementation(Libraries.MaterialDialog.materialDialogsCore)
    implementation(Libraries.MaterialDialog.materialDialogsColor)

    // Tests
    testImplementation(Libraries.Test.junit)
    testImplementation(Libraries.Test.mockito)
    testImplementation(Libraries.Test.coreTesting)
    androidTestImplementation(Libraries.Test.coreTesting)
    androidTestImplementation(Libraries.Test.espressoCore)

    // RxJava
    implementation(Libraries.RxJava.rxJava)
    implementation(Libraries.RxJava.rxAndroid)
    implementation(Libraries.RxJava.roomRxJava)
}
