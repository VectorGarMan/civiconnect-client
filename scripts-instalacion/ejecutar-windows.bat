@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo ========================================
echo   CiviConnect Cliente
echo   Instalador y Ejecutor para Windows
echo ========================================
echo.

REM Verificar si Java esta instalado
echo [1/2] Verificando instalacion de Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo X Java no esta instalado en este sistema.
    echo.
    echo Instalando Java automaticamente...
    echo Por favor, acepta la solicitud de permisos de administrador si aparece.
    echo.
    
    REM Verificar si Chocolatey esta instalado
    where choco >nul 2>&1
    if %errorlevel% neq 0 (
        echo Instalando Chocolatey (administrador de paquetes)...
        powershell -NoProfile -ExecutionPolicy Bypass -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))"
        
        if !errorlevel! neq 0 (
            echo.
            echo X Error al instalar Chocolatey.
            echo.
            echo Por favor, instala Java manualmente desde:
            echo https://www.oracle.com/java/technologies/downloads/
            echo.
            echo Descarga e instala Java 21 o superior, luego ejecuta este script nuevamente.
            echo.
            pause
            exit /b 1
        )
        
        REM Refrescar variables de entorno
        call refreshenv
    )
    
    echo Instalando OpenJDK 21...
    choco install openjdk21 -y
    
    if !errorlevel! neq 0 (
        echo.
        echo X Error al instalar Java.
        echo.
        echo Por favor, instala Java manualmente desde:
        echo https://www.oracle.com/java/technologies/downloads/
        echo.
        echo Descarga e instala Java 21 o superior, luego ejecuta este script nuevamente.
        echo.
        pause
        exit /b 1
    )
    
    REM Refrescar variables de entorno
    call refreshenv
    
    echo.
    echo OK Java instalado correctamente.
    echo.
) else (
    echo OK Java ya esta instalado.
    java -version
    echo.
)

echo [2/2] Iniciando CiviConnect Cliente...
echo.

REM Verificar que el JAR existe
if not exist "civiconnect-client.jar" (
    echo X Error: No se encontro el archivo civiconnect-client.jar
    echo.
    echo Asegurate de que este script este en la misma carpeta que el archivo JAR.
    echo.
    pause
    exit /b 1
)

echo ========================================
echo   Iniciando aplicacion...
echo ========================================
echo.

java -jar civiconnect-client.jar

if %errorlevel% neq 0 (
    echo.
    echo X Error al ejecutar la aplicacion.
    echo.
    pause
    exit /b 1
)

endlocal

@REM Made with Bob
