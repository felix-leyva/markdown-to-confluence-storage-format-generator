@echo off
echo Building Confluence Writer with JVM toolchain...
echo This will automatically download and use JDK 17 if needed.
call gradlew :confluence-ui:build

if %ERRORLEVEL% EQU 0 (
    echo Build successful! Starting application...
    call gradlew :confluence-ui:run
) else (
    echo Build failed. Please check the error messages above.
    exit /b 1
)
