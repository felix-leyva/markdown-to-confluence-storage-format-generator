# Confluence Writer KMP

A Kotlin Multiplatform application for converting Markdown to Confluence storage format XML.

## Modules

- **confluence-core**: Contains the core functionality for converting Markdown to Confluence XML
  format.
- **confluence-ui**: Contains the Compose Multiplatform UI for both desktop and Android.

## Features

- Convert Markdown files to Confluence storage format XML
- Preview the conversion before saving
- Cross-platform support for JVM desktop and Android

## Requirements

- JDK 11 or higher
- Android SDK 24 or higher (for Android)

## Building the Project

### From Command Line

```bash
# Build all projects
./gradlew build

# Run desktop application
./gradlew :confluence-ui:run

# Build Android APK
./gradlew :confluence-ui:assembleDebug
```

### From IDE

Open the project in IntelliJ IDEA or Android Studio and run the appropriate configuration:

- For desktop: Run the `Main.kt` file in the `confluence-ui` module
- For Android: Run the `MainActivity.kt` file in the `confluence-ui` module
