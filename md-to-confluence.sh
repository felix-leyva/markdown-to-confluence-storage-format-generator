#!/bin/bash

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

# Path to the JAR file (assuming it's in the build/libs directory)
JAR_PATH="$SCRIPT_DIR/confluence-md-cli/build/libs/confluence-md-cli.jar"

# Check if the JAR exists
if [ ! -f "$JAR_PATH" ]; then
    echo "Error: JAR file not found at $JAR_PATH"
    echo "Please run './build_jar.sh' first to build the JAR."
    exit 1
fi

# Check if any arguments were provided
if [ $# -eq 0 ]; then
    # No arguments, show help
    java -jar "$JAR_PATH" --help
else
    # Pass all arguments to the JAR
    java -jar "$JAR_PATH" "$@"
fi