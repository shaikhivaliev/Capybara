plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.KOTLIN_PARCELIZE)
}

android {
    namespace = "com.petapp.capybara.core"
    compileSdk = 33
}

dependencies {
    // Material
    implementation(Libraries.Android.material)

    // Dagger
    implementation(Libraries.Dagger.core)
    implementation(Libraries.Dagger.android)
    kapt(Libraries.Dagger.compiler)
    kapt(Libraries.Dagger.androidProcessor)

    // Room
    implementation(Libraries.Room.runtime)
    annotationProcessor(Libraries.Room.compiler)
    kapt(Libraries.Room.compiler)
    implementation(Libraries.Room.ktx)

    // Compose
    val composeBom = platform(Libraries.Compose.bom)
    implementation(composeBom)
    implementation(Libraries.Compose.materialDesign)
    implementation(Libraries.Compose.navigation)
}
