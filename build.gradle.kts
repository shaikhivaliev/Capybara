import com.android.build.gradle.internal.tasks.factory.dependsOn

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        val kotlinVersion = "1.4.0"
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.gms:google-services:4.3.3")
        classpath("android.arch.navigation:navigation-safe-args-gradle-plugin:1.0.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }
}

val ktLintConfig = configurations.create("ktlint")

dependencies {
    "ktlint"("com.pinterest:ktlint:0.36.0")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.register<JavaExec>("ktlintCheck") {
    description = "Check Kotlin code style"
    classpath = ktLintConfig
    main = "com.pinterest.ktlint.Main"
    args = listOf(
        "--android",
        "--editorconfig=" + file(".editorconfig").path,
        "--reporter=plain?group_by_file",
        "--disabled_rules=final-newline,import-ordering,no-wildcard-imports"
    ) + allprojects.map { it.file("src").path + "/**/*.kt" }
}
