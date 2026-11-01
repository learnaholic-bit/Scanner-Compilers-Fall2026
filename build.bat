@echo off
setlocal enabledelayedexpansion

echo ===================================================
echo   TINY Scanner - Windows EXE Build Script
echo ===================================================

cd /d "%~dp0"

REM 1. Verify JDK tools exist in PATH
where javac >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] 'javac' was not found in your PATH.
    echo Please install JDK 14 or higher and add its bin directory to PATH.
    pause
    exit /b 1
)

where jpackage >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] 'jpackage' was not found in your PATH.
    echo Please make sure you are using JDK 14 or higher with jpackage available.
    pause
    exit /b 1
)

REM 2. Clean previous build artifacts
echo [1/4] Cleaning previous builds...
if exist bin rmdir /s /q bin
if exist staging rmdir /s /q staging
if exist TinyScanner rmdir /s /q TinyScanner
if exist Executable rmdir /s /q Executable
if exist tiny-scanner.jar del /f /q tiny-scanner.jar

REM 3. Compile Java source files
echo [2/4] Compiling Java source files...
mkdir bin
javac -d bin src\main\java\org\project\*.java
if %errorlevel% neq 0 (
    echo [ERROR] Java compilation failed!
    pause
    exit /b %errorlevel%
)

REM 4. Package compiled classes into an isolated JAR in staging
echo [3/4] Packaging into JAR file...
mkdir staging
jar --create --file staging\tiny-scanner.jar --main-class org.project.Main -C bin .
if %errorlevel% neq 0 (
    echo [ERROR] JAR packaging failed!
    pause
    exit /b %errorlevel%
)

REM Keep a copy of the JAR in the root folder as well
copy /y staging\tiny-scanner.jar tiny-scanner.jar >nul

REM 5. Generate native Windows Executable using jpackage
REM FIXES APPLIED:
REM 1. --input points to 'staging', NOT '.' (prevents recursive folder copy loop).
REM 2. --main-class is 'org.project.Main' (matching the Java source package).
REM 3. --win-console attaches console subsystem so terminal stdout/stderr are visible.
echo [4/4] Generating native Windows Executable via jpackage...
echo (Bundling minimal Java runtime, please wait...)
jpackage --type app-image --name TinyScanner --input staging --main-jar tiny-scanner.jar --main-class org.project.Main --dest . --win-console
if %errorlevel% neq 0 (
    echo [ERROR] jpackage execution failed!
    if exist staging rmdir /s /q staging
    pause
    exit /b %errorlevel%
)

REM Remove temporary staging directory
if exist staging rmdir /s /q staging

echo ===================================================
echo [SUCCESS] Build completed successfully!
echo.
echo Executable generated:
echo   - TinyScanner\TinyScanner.exe
echo.
echo You can run it via:
echo   TinyScanner\TinyScanner.exe test_files\test1.txt output.txt
echo ===================================================
pause
