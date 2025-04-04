@echo off
echo Building ConfluenceWriter JAR...
call gradlew clean jar

echo.
echo JAR file created at: build\libs\ConfluenceWriter-1.0-SNAPSHOT.jar
echo.
echo Usage examples:
echo java -jar build\libs\ConfluenceWriter-1.0-SNAPSHOT.jar --help
echo java -jar build\libs\ConfluenceWriter-1.0-SNAPSHOT.jar README.md
echo java -jar build\libs\ConfluenceWriter-1.0-SNAPSHOT.jar --mdstring "# Hello World" --output hello.xml
