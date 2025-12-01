#!/bin/bash

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo "========================================"
echo "  CiviConnect Cliente"
echo "  Ejecutando aplicacion..."
echo "========================================"
echo ""

# Verificar que el JAR existe
if [ ! -f "target/civiconnect-client.jar" ]; then
    echo -e "${RED}X Error: No se encontro el archivo JAR.${NC}"
    echo "Por favor, ejecuta primero ./install-mac.sh"
    echo ""
    exit 1
fi

java -jar target/civiconnect-client.jar

if [ $? -ne 0 ]; then
    echo ""
    echo -e "${RED}X Error al ejecutar la aplicacion.${NC}"
    exit 1
fi

# Made with Bob
