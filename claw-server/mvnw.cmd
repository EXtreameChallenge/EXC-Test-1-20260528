@REM Maven Wrapper script for Windows
@REM This script downloads Maven if not present and runs the build

@echo off
setlocal

set MAVEN_PROJECTBASEDIR=%~dp0
set MAVEN_CMD="%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"

if exist "%MAVEN_PROJECTBASEDIR%mvnw.cmd" (
    set SCRIPT_PATH="%MAVEN_PROJECTBASEDIR%mvnw.cmd"
) else (
    set SCRIPT_PATH="%~dpnx0"
)

set JAVACMD=java
if defined JAVA_HOME (
    set JAVACMD="%JAVA_HOME%\bin\java.exe"
)

%JAVACMD% -jar %MAVEN_CMD% %*

endlocal
