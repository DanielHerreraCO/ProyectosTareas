@echo off
REM Script para ejecutar el backend Spring Boot
cd /d %~dp0
java -jar build\libs\ProyectosTareas-0.0.1-SNAPSHOT.jar
pause
