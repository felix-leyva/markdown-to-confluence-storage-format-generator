#!/bin/bash

# Build for current OS
echo "Building ConfluenceWriter for current OS..."
./gradlew :confluence-ui:packageDistributionsForCurrentOS

echo "Build complete. Check the build/compose/binaries directory for outputs."
