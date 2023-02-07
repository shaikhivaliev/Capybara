buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(Libraries.AndroidTools.gradle)
        classpath(kotlin("gradle-plugin", version = Versions.stdLib))
        classpath("com.google.gms:google-services:4.3.8")
    }
}

plugins {
    checkDependencyUpdates
    detekt
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
    }

    // Enable to use Experimental APIs
    project.tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
        // kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn" // flatMapMerge
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime"
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
        "--disabled_rules=final-newline,import-ordering,no-wildcard-imports,Missing newline after"
    ) + allprojects.map { it.file("src").path + "/**/*.kt" }
}
