#!/bin/bash

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo "========================================"
echo "  CiviConnect Cliente"
echo "  Instalador y Ejecutor para macOS"
echo "========================================"
echo ""

# Funcion para verificar el ultimo comando
check_error() {
    if [ $? -ne 0 ]; then
        echo -e "${RED}X Error: $1${NC}"
        exit 1
    fi
}

# [1/2] Verificar Java
echo -e "${BLUE}[1/2] Verificando instalacion de Java...${NC}"

if command -v java &> /dev/null; then
    echo -e "${GREEN}OK Java ya esta instalado.${NC}"
    java -version
    echo ""
else
    echo -e "${YELLOW}X Java no esta instalado en este sistema.${NC}"
    echo ""
    echo "Instalando Java automaticamente..."
    echo ""
    
    # Verificar si Homebrew esta instalado
    if ! command -v brew &> /dev/null; then
        echo "Instalando Homebrew (administrador de paquetes)..."
        echo "Por favor, sigue las instrucciones en pantalla."
        /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
        check_error "No se pudo instalar Homebrew"
        
        # Agregar Homebrew al PATH
        if [[ $(uname -m) == 'arm64' ]]; then
            echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile
            eval "$(/opt/homebrew/bin/brew shellenv)"
        else
            echo 'eval "$(/usr/local/bin/brew shellenv)"' >> ~/.zprofile
            eval "$(/usr/local/bin/brew shellenv)"
        fi
    fi
    
    echo "Instalando OpenJDK 21..."
    brew install openjdk@21
    check_error "No se pudo instalar Java"
    
    # Crear enlace simbolico
    echo "Configurando Java en el sistema..."
    if [[ $(uname -m) == 'arm64' ]]; then
        sudo ln -sfn /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk
    else
        sudo ln -sfn /usr/local/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk
    fi
    
    # Agregar Java al PATH
    if [[ $(uname -m) == 'arm64' ]]; then
        echo 'export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"' >> ~/.zprofile
        export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"
    else
        echo 'export PATH="/usr/local/opt/openjdk@21/bin:$PATH"' >> ~/.zprofile
        export PATH="/usr/local/opt/openjdk@21/bin:$PATH"
    fi
    
    echo -e "${GREEN}OK Java instalado correctamente.${NC}"
    echo ""
fi

echo -e "${BLUE}[2/2] Iniciando CiviConnect Cliente...${NC}"
echo ""

# Verificar que el JAR existe
if [ ! -f "civiconnect-client.jar" ]; then
    echo -e "${RED}X Error: No se encontro el archivo civiconnect-client.jar${NC}"
    echo ""
    echo "Asegurate de que este script este en la misma carpeta que el archivo JAR."
    echo ""
    exit 1
fi

echo "========================================"
echo "  Iniciando aplicacion..."
echo "========================================"
echo ""

java -jar civiconnect-client.jar

if [ $? -ne 0 ]; then
    echo ""
    echo -e "${RED}X Error al ejecutar la aplicacion.${NC}"
    echo ""
    exit 1
fi

# Made with Bob
