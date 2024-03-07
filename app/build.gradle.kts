plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint") version "11.6.0"
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.ocproject.realestatemanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ocproject.realestatemanager"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes.add("META-INF/*")
        }
    }

    applicationVariants.all {
        addJavaSourceFoldersToModel(file("build/generated/ksp/$name/kotlin"))
    }
}



ktlint {
    // configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    debug.set(true)
    android.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(true)
    version.set("1.0.0")
    disabledRules.set(setOf("no-wildcard-imports"))
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui.ui)
    implementation(libs.compose.ui.ui.graphics)
    implementation(libs.compose.ui.ui.tooling.preview)
    implementation(libs.compose.navigation)
    implementation(libs.compose.material3)
//    runtimeOnly("androidx.compose.material3:material3:1.1.2")
    implementation(libs.compose.material3.window)


    implementation(libs.appcompat)
    // coil managing images
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.timber)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.ui.test.junit4)
    debugImplementation(libs.compose.ui.ui.tooling)
    debugImplementation(libs.compose.ui.ui.test.manifest)
    // Room, for linking with bdd
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
//    annotationProcessor(libs.room.compiler)

    ksp(libs.room.compiler)

    implementation(libs.koin.bom)
    implementation(libs.koin.android)
    implementation(libs.koin.annotations)
    implementation(libs.koin.compose)
    implementation(libs.koin.core)
    ksp(libs.koin.compiler)
}
