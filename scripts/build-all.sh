#!/bin/bash

# Build for all platforms
echo "Building ConfluenceWriter for all platforms..."

# macOS
echo "Building macOS DMG..."
./gradlew :confluence-ui:packageDmg

# Windows
echo "Building Windows MSI..."
./gradlew :confluence-ui:packageMsi

# Linux
echo "Building Linux packages..."
./gradlew :confluence-ui:packageDeb
./gradlew :confluence-ui:packageRpm
./gradlew :confluence-ui:packageAppImage

echo "All builds complete. Check the build/compose/binaries directory for outputs."
