#!/bin/bash

# Build and run the Confluence Writer desktop application

# Get directory of this script
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$SCRIPT_DIR"

echo "Building Confluence Writer with JVM toolchain..."
echo "This will automatically download and use JDK 17 if needed."
./gradlew :confluence-ui:build

if [ $? -eq 0 ]; then
    echo "Build successful! Starting application..."
    ./gradlew :confluence-ui:run
else
    echo "Build failed. Please check the error messages above."
    exit 1
fi
