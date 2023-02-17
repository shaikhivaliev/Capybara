import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id(Plugins.ANDROID_APPLICATION)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.GOOGLE_SERVICES)
    id(Plugins.CHECK_DEPENDENCY_UPDATES)
}

@Suppress("UnstableApiUsage")
android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.petapp.capybara"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"

        compileOptions {
            // Flag to enable support for the new language APIs
            isCoreLibraryDesugaringEnabled = true
        }
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

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

tasks.named("check").dependsOn("ktlintCheck")

dependencies {
    // Project
    implementation(project(Modules.core))
    implementation(project(Modules.uicomponents))
    implementation(project(Modules.setting))
    implementation(project(Modules.profile))
    implementation(project(Modules.calendar))
    implementation(project(Modules.types))
    implementation(project(Modules.survey))
    implementation(project(Modules.healthdiary))

    // Desugar JdkLibs
    coreLibraryDesugaring(Libraries.AndroidTools.desugarJdkLibs)

    // Room
    implementation(Libraries.Room.stetho)

    // Firebase
    implementation(Libraries.Firebase.firebaseCore)
    implementation(Libraries.Firebase.firebaseAuth)
    implementation(Libraries.Firebase.firebaseUiAuth)

    // Compose
    val composeBom = platform(Libraries.Compose.bom)
    implementation(composeBom)
    implementation(Libraries.Compose.navigation)
    implementation(Libraries.Compose.materialDesign)
    implementation(Libraries.Compose.themeAdapter)
}
