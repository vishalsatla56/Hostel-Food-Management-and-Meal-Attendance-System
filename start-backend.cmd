@echo off
title Hostel Food Management - Backend
echo ========================================================
echo Starting Spring Boot Backend (Port 8080)
echo ========================================================

set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

:: Load environment variables from root .env if it exists
if exist "%~dp0.env" (
    echo Loading environment variables from .env...
    for /f "usebackq tokens=1,* delims==" %%A in ("%~dp0.env") do (
        set "line=%%A"
        if not "!line:~0,1!"=="#" (
            set "%%A=%%B"
        )
    )
)

cd /d "%~dp0backend"
if exist "target\hostel-food-management-1.0.0.jar" (
    java -jar "target\hostel-food-management-1.0.0.jar"
) else (
    echo Building backend with Maven...
    call "..\tools\apache-maven-3.9.6\bin\mvn.cmd" package -DskipTests
    java -jar "target\hostel-food-management-1.0.0.jar"
)
pause
