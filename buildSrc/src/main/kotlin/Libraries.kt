object Libraries {

    object Kotlin {
        val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.stdLib}"
        val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
        val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    }

    object AndroidTools {
        val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
        val desugarJdkLibs = "com.android.tools:desugar_jdk_libs:${Versions.desugarJdkLibs}"
    }

    object Android {
        val material = "com.google.android.material:material:${Versions.material}"
    }

    object AndroidX {
        val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        val preferenceKtx = "androidx.preference:preference-ktx:${Versions.preferenceKtx}"
        val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
        val viewPager = "androidx.viewpager2:viewpager2:1.1.0-alpha01"
        val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    }

    object Navigation {
        val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        val uiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    }

    object Room {
        val runtime = "androidx.room:room-runtime:${Versions.room}"
        val ktx = "androidx.room:room-ktx:${Versions.room}"
        val compiler = "androidx.room:room-compiler:${Versions.room}"
        val testing = "androidx.room:room-testing:${Versions.room}"
        val stetho = "com.facebook.stetho:stetho:${Versions.stetho}"

    }

    object Dagger {
        val core = "com.google.dagger:dagger:${Versions.dagger}"
        val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
        val android = "com.google.dagger:dagger-android:${Versions.dagger}"
        val androidSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
        val androidProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
    }

    object AdapterDelegates {
        val layoutContainer =
            "com.hannesdorfmann:adapterdelegates4-kotlin-dsl-layoutcontainer:${Versions.adapterDelegates}"
        val adapterDelegates = "com.hannesdorfmann:adapterdelegates4-kotlin-dsl:${Versions.adapterDelegates}"
        val viewBinding = "com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:${Versions.adapterDelegates}"
    }

    object Glide {
        val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    }

    object Firebase {
        val firebaseCore = "com.google.firebase:firebase-core:${Versions.firebaseCore}"
        val firebaseUiAuth = "com.firebaseui:firebase-ui-auth:${Versions.firebaseUiAuth}"
        val firebaseAuth = "com.google.firebase:firebase-auth:${Versions.firebaseAuth}"
    }

    object Test {
        val junit = "junit:junit:${Versions.junit}"
        val mockito = "org.mockito:mockito-core:${Versions.mockito}"
        val coreTesting = "androidx.arch.core:core-testing:${Versions.coreTesting}"
        val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    }

    object MaterialDialog {
        val materialDialogsCore = "com.afollestad.material-dialogs:core:${Versions.materialDialog}"
        val materialDialogsColor = "com.afollestad.material-dialogs:color:${Versions.materialDialog}"
    }

    object ViewBinding {
        val viewBindingPropertyDelegate =
            "com.github.kirich1409:viewbindingpropertydelegate-noreflection:${Versions.viewBindingPropertyDelegate}"
    }

    object RxJava {
        val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
        val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
        val roomRxJava = "androidx.room:room-rxjava2:${Versions.room}"
    }

    object Koin {
        val koinAndroid = "org.koin:koin-android:${Versions.koin}"
        val koinAndroidxScope = "org.koin:koin-androidx-scope:${Versions.koin}"
        val koinAndroidxViewmodel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    }
}
