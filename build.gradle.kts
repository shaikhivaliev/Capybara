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

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.12.0")
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

val detektExcludes = listOf(
    "**/resources/**",
    "**/res/**",
    ".gradle/**",
    ".git/**",
    ".idea/**",
    ".githooks/**",
    "scripts/**",
    "config/**",
    "gradle/**"
)
val detektIncludes = listOf("**/*.kt")
val detektConfig = file("config/detekt/detekt-config.yml")

tasks.register<io.gitlab.arturbosch.detekt.Detekt>("detektAll") {
    parallel = true
    ignoreFailures = false
    setSource(files(projectDir))
    include(detektIncludes)

    exclude(detektExcludes)
    exclude(allprojects.map { it.buildDir.toRelativeString(projectDir) + "/**" })

    config.setFrom(detektConfig)

    buildUponDefaultConfig = false
    reports {
        xml.enabled = false
        txt.enabled = false

        html {
            enabled = true
            destination = File(project.buildDir, "reports/detekt.html")
        }
    }

    debug = true
}

val ktLintConfig = configurations.create("ktlint")

dependencies {
    "ktlint"("com.pinterest:ktlint:0.36.0")
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
