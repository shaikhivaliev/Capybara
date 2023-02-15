object Libraries {

    object Kotlin {
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.stdLib}"
    }

    object AndroidTools {
        const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
        const val desugarJdkLibs = "com.android.tools:desugar_jdk_libs:${Versions.desugarJdkLibs}"
    }

    object Android {
        const val material = "com.google.android.material:material:${Versions.material}"
        const val guava = "com.google.guava:guava:${Versions.guava}"
    }

    object AndroidX {
        val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
        const val fragment = "androidx.fragment:fragment-ktx:1.5.5"
    }

    object Room {
        const val runtime = "androidx.room:room-runtime:${Versions.room}"
        const val ktx = "androidx.room:room-ktx:${Versions.room}"
        const val compiler = "androidx.room:room-compiler:${Versions.room}"
        const val testing = "androidx.room:room-testing:${Versions.room}"
        const val stetho = "com.facebook.stetho:stetho:${Versions.stetho}"

    }

    object Dagger {
        const val core = "com.google.dagger:dagger:${Versions.dagger}"
        const val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
        const val android = "com.google.dagger:dagger-android:${Versions.dagger}"
        const val androidSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
        const val androidProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
    }

    object Firebase {
        const val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebaseCore}"
        const val firebaseUiAuth = "com.firebaseui:firebase-ui-auth:${Versions.firebaseUiAuth}"
        const val firebaseAuth = "com.google.firebase:firebase-auth:${Versions.firebaseAuth}"
    }

    object Coroutines {
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    }

    object Compose {
        const val bom = "androidx.compose:compose-bom:${Versions.composeBom}"
        const val materialDesign = "androidx.compose.material:material"
        const val navigation = "androidx.navigation:navigation-compose:2.5.3"
        const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:"
        const val uiTooling = "androidx.compose.ui:ui-tooling"
        const val uiTest = "androidx.compose.ui:ui-test-junit4"
        const val uiTestManifest = "androidx.compose.ui:ui-test-manifest"
        const val themeAdapter = "com.google.accompanist:accompanist-themeadapter-material:${Versions.themeAdapter}"
        const val glide = "com.github.bumptech.glide:compose:${Versions.glideCompose}"
        const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
        const val composeCalendar =
            "io.github.boguszpawlowski.composecalendar:composecalendar:${Versions.composeCalendar}"
        const val composeCalendarDateTime =
            "io.github.boguszpawlowski.composecalendar:kotlinx-datetime:${Versions.composeCalendar}"
    }
}
