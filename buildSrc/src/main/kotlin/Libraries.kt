object Libraries {

    object AndroidTools {
        const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
        const val desugarJdkLibs = "com.android.tools:desugar_jdk_libs:${Versions.desugarJdkLibs}"
    }

    object Android {
        const val material = "com.google.android.material:material:${Versions.material}"
    }

    object AndroidX {
        val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    }

    object Room {
        const val runtime = "androidx.room:room-runtime:${Versions.room}"
        const val ktx = "androidx.room:room-ktx:${Versions.room}"
        const val compiler = "androidx.room:room-compiler:${Versions.room}"
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

    object Compose {
        const val bom = "androidx.compose:compose-bom:${Versions.composeBom}"
        const val materialDesign = "androidx.compose.material:material"
        const val navigation = "androidx.navigation:navigation-compose:2.5.3"
        const val themeAdapter = "com.google.accompanist:accompanist-themeadapter-material:${Versions.themeAdapter}"
        const val glide = "com.github.bumptech.glide:compose:${Versions.glideCompose}"
        const val activity = "androidx.activity:activity-compose:${Versions.activityCompose}"
        const val colorPicker = "io.github.vanpra.compose-material-dialogs:color:${Versions.colorPicker}"
        const val composeCalendar =
            "io.github.boguszpawlowski.composecalendar:composecalendar:${Versions.composeCalendar}"
    }
}
