@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo ========================================
echo   CiviConnect Cliente - Instalador
echo   Instalacion Automatica para Windows
echo ========================================
echo.

REM Verificar si Java esta instalado
echo [1/4] Verificando instalacion de Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo X Java no esta instalado en este sistema.
    echo.
    echo Instalando Java automaticamente...
    echo.
    
    REM Verificar si Chocolatey esta instalado
    where choco >nul 2>&1
    if %errorlevel% neq 0 (
        echo Instalando Chocolatey (administrador de paquetes)...
        echo Por favor, acepta la solicitud de permisos de administrador.
        powershell -NoProfile -ExecutionPolicy Bypass -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))"
        
        if !errorlevel! neq 0 (
            echo.
            echo X Error al instalar Chocolatey.
            echo Por favor, instala Java manualmente desde:
            echo https://www.oracle.com/java/technologies/downloads/
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
        echo Por favor, instala Java manualmente desde:
        echo https://www.oracle.com/java/technologies/downloads/
        echo.
        pause
        exit /b 1
    )
    
    REM Refrescar variables de entorno
    call refreshenv
    
    echo.
    echo OK Java instalado correctamente.
) else (
    echo OK Java ya esta instalado.
    java -version
)

echo.
echo [2/4] Verificando Maven...

REM Verificar si Maven esta instalado
where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo X Maven no esta instalado.
    echo Instalando Maven...
    
    where choco >nul 2>&1
    if %errorlevel% neq 0 (
        echo Instalando Chocolatey primero...
        powershell -NoProfile -ExecutionPolicy Bypass -Command "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))"
        call refreshenv
    )
    
    choco install maven -y
    call refreshenv
    
    echo OK Maven instalado correctamente.
) else (
    echo OK Maven ya esta instalado.
)

echo.
echo [3/4] Compilando la aplicacion...
echo Esto puede tomar unos minutos...
echo.

call mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo.
    echo X Error al compilar la aplicacion.
    echo Por favor, verifica que todos los archivos esten presentes.
    echo.
    pause
    exit /b 1
)

echo.
echo OK Aplicacion compilada correctamente.

echo.
echo [4/4] Iniciando CiviConnect Cliente...
echo.

REM Verificar que el JAR existe
if not exist "target\civiconnect-client.jar" (
    echo X Error: No se encontro el archivo JAR compilado.
    echo Por favor, verifica la compilacion.
    pause
    exit /b 1
)

echo ========================================
echo   Iniciando aplicacion...
echo ========================================
echo.

java -jar target\civiconnect-client.jar

if %errorlevel% neq 0 (
    echo.
    echo X Error al ejecutar la aplicacion.
    pause
    exit /b 1
)

endlocal
