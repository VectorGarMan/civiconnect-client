@echo off
chcp 65001 >nul

echo ========================================
echo   CiviConnect Cliente
echo   Ejecutando aplicacion...
echo ========================================
echo.

REM Verificar que el JAR existe
if not exist "target\civiconnect-client.jar" (
    echo X Error: No se encontro el archivo JAR.
    echo Por favor, ejecuta primero install-windows.bat
    echo.
    pause
    exit /b 1
)

java -jar target\civiconnect-client.jar

if %errorlevel% neq 0 (
    echo.
    echo X Error al ejecutar la aplicacion.
    pause
    exit /b 1
)

@REM Made with Bob
