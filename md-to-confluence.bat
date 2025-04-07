@echo off

REM Get the directory where the script is located
set SCRIPT_DIR=%~dp0

REM Path to the JAR file (assuming it's in the build/libs directory)
set JAR_PATH=%SCRIPT_DIR%confluence-md-cli\build\libs\confluence-md-cli.jar

REM Check if the JAR exists
if not exist "%JAR_PATH%" (
    echo Error: JAR file not found at %JAR_PATH%
    echo Please run './build_jar.bat' first to build the JAR.
    exit /b 1
)

REM Check if any arguments were provided
if "%~1"=="" (
    REM No arguments, show help
    java -jar "%JAR_PATH%" --help
) else (
    REM Pass all arguments to the JAR
    java -jar "%JAR_PATH%" %*
)