buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        val  kotlinVersion  = "1.4.0"
        classpath ("com.android.tools.build:gradle:4.0.1")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath ("com.google.gms:google-services:4.3.3")
        classpath ("android.arch.navigation:navigation-safe-args-gradle-plugin:1.0.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io")}
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
