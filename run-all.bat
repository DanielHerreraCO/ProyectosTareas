@echo off
REM Script para ejecutar backend y frontend

REM 1. Ejecutar backend (Spring Boot)
cd /d %~dp0..
start "Backend" cmd /k "java -jar build\libs\ProyectosTareas-0.0.1-SNAPSHOT.jar"

REM 2. Ejecutar frontend (servidor estatico)
cd frontend
if not exist node_modules (
    echo Instalando dependencias de frontend...
    npm install
)
if not exist dist (
    echo Generando build de frontend...
    npm run build
)

npx serve -s dist
