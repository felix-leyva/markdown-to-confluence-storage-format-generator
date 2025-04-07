#!/bin/bash

echo "Building ConfluenceWriter JAR..."
./gradlew clean jar

echo ""
echo "JAR file created at: confluence-md-cli/build/libs/confluence-md-cli.jar"
echo ""
echo "Usage examples:"
echo "java -jar confluence-md-cli/build/libs/confluence-md-cli.jar --help"
echo "java -jar confluence-md-cli/build/libs/confluence-md-cli.jar README.md"
echo "java -jar confluence-md-cli/build/libs/confluence-md-cli.jar --mdstring \"# Hello World\" --output hello.xml"
