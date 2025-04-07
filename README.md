# Confluence Writer KMP

A Kotlin Multiplatform application for converting Markdown to Confluence storage format XML.
A simple command line version is also available.

## Modules

- **confluence-core**: Contains the core functionality for converting Markdown to Confluence XML
  format.
- **confluence-ui**: Contains the Compose Multiplatform UI for both desktop and Android.
- **confluence-cli**: A command line interface for converting Markdown files to Confluence XML format.

## Features

- Convert Markdown files to Confluence storage format XML
- Preview the conversion before saving
- Cross-platform support for JVM desktop and Android

## Requirements

- JDK 11 or higher
- Android SDK 24 or higher (for Android)

## Building the Compose UI Project

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

## Running the CLI

- Make sure to build the `build_jar.sh` or `buid_jar.bat` script before running the CLI the first time, so that the JAR
  file is created. This script will create a JAR file for the `confluence-cli` module.
- Use the script `confluence-cli.sh` or `confluence-cli.bat` to run the command line interface.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
