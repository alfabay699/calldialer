@echo off
setlocal
set DIR=%~dp0
set GRADLE_USER_HOME=%DIR%\.gradle
java -classpath "%DIR%gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
