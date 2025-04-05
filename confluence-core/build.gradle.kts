plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    androidTarget()

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // XML Processing
                implementation("io.github.pdvrieze.xmlutil:core:0.86.2")
                implementation("io.github.pdvrieze.xmlutil:serialization:0.86.2")

                // Markdown parser
                implementation("org.jetbrains:markdown:0.5.0")

                // For coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            }
        }

        val androidMain by getting {
            dependencies {
                // Android specific dependencies
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
            }
        }

        val desktopMain by getting {
            dependencies {
                // Desktop specific dependencies
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")
            }
        }
    }
}

android {
    namespace = "com.confluence.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        // Required when setting minSdkVersion to 23 or later
        targetSdk = 34
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// Use Java toolchain
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}
