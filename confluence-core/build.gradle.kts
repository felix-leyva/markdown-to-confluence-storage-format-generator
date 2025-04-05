plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    androidTarget()

    jvm {
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

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                // CommonMark dependencies for tests
                implementation("org.commonmark:commonmark:0.21.0")
                implementation("org.commonmark:commonmark-ext-autolink:0.21.0")
                implementation("org.commonmark:commonmark-ext-gfm-strikethrough:0.21.0")
                implementation("org.commonmark:commonmark-ext-gfm-tables:0.21.0")
                implementation("org.commonmark:commonmark-ext-task-list-items:0.21.0")
            }
        }

        val androidMain by getting {
            dependencies {
                // Android specific dependencies
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }

        val jvmMain by getting {
            dependencies {
                // Desktop specific dependencies
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")

                // CommonMark dependencies
                implementation("org.commonmark:commonmark:0.21.0")
                implementation("org.commonmark:commonmark-ext-autolink:0.21.0")
                implementation("org.commonmark:commonmark-ext-gfm-strikethrough:0.21.0")
                implementation("org.commonmark:commonmark-ext-gfm-tables:0.21.0")
                implementation("org.commonmark:commonmark-ext-task-list-items:0.21.0")
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
