import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.petapp.capybara"
        minSdkVersion(23)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.2.0")

    // ui
    implementation("androidx.constraintlayout:constraintlayout:2.0.2")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.fragment:fragment:1.3.0-beta01")

    // navigation
    val navigation = "2.3.0"
    implementation("android.arch.navigation:navigation-fragment-ktx:$navigation")
    implementation("android.arch.navigation:navigation-ui-ktx:$navigation")

    // rx
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.13")

    // di
    val koin = "2.1.6"
    implementation("org.koin:koin-android:$koin")
    implementation("org.koin:koin-androidx-scope:$koin")
    implementation("org.koin:koin-androidx-viewmodel:$koin")

    // recycler adapter
    implementation("com.hannesdorfmann:adapterdelegates4-kotlin-dsl-layoutcontainer:4.3.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")

    // database
    val room = "2.2.5"
    implementation("androidx.room:room-runtime:$room")
    implementation("androidx.room:room-rxjava2:$room")
    kapt("androidx.room:room-compiler:$room")

    // database debug
    implementation("com.facebook.stetho:stetho:1.5.1")

    // image
    val glide  = "4.11.0"
    implementation("com.github.bumptech.glide:glide:$glide")
    kapt("com.github.bumptech.glide:compiler:$glide")

    // material dialog
    implementation("com.afollestad.material-dialogs:core:3.1.1")

    // firebase core
    implementation("com.google.firebase:firebase-core:17.5.1")

    // firebase auth
    implementation("com.firebaseui:firebase-ui-auth:4.3.1")
    implementation("com.google.firebase:firebase-auth:19.4.0")

    // firebase analytics
    implementation("com.google.firebase:firebase-analytics:17.6.0")

    // calendar
    implementation("com.github.kizitonwose:CalendarView:0.3.5")

    // test
    testImplementation("junit:junit:4.13")
    testImplementation("org.mockito:mockito-core:3.5.10")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
