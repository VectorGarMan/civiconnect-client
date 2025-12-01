# Scripts de Instalación - CiviConnect Cliente

Esta carpeta contiene todos los scripts necesarios para instalar y ejecutar CiviConnect Cliente en diferentes escenarios.

## 📁 Contenido de la Carpeta

### Para Usuarios Finales (con JAR precompilado)

Estos scripts son para usuarios que reciben el archivo `civiconnect-client.jar` ya compilado:

- **`ejecutar-windows.bat`** - Script principal para Windows
  - Verifica e instala Java automáticamente si es necesario
  - Ejecuta el archivo JAR
  - Usar: Colocar junto al JAR y hacer doble clic

- **`ejecutar-mac.sh`** - Script principal para macOS
  - Verifica e instala Java automáticamente si es necesario
  - Ejecuta el archivo JAR
  - Usar: Colocar junto al JAR y ejecutar `./ejecutar-mac.sh`

- **`README-USUARIO.md`** - Guía completa para usuarios finales
  - Instrucciones detalladas paso a paso
  - Solución de problemas comunes
  - Preguntas frecuentes

### Para Desarrolladores (con código fuente)

Estos scripts son para desarrolladores que tienen el código fuente completo:

- **`install-windows.bat`** - Instalador completo para Windows
  - Instala Java y Maven automáticamente
  - Compila el proyecto con Maven
  - Ejecuta la aplicación

- **`install-mac.sh`** - Instalador completo para macOS
  - Instala Java y Maven automáticamente
  - Compila el proyecto con Maven
  - Ejecuta la aplicación

- **`run-windows.bat`** - Ejecutor rápido para Windows
  - Solo ejecuta el JAR ya compilado
  - Usar después de compilar con Maven

- **`run-mac.sh`** - Ejecutor rápido para macOS
  - Solo ejecuta el JAR ya compilado
  - Usar después de compilar con Maven

## 🚀 Guía Rápida de Uso

### Escenario 1: Usuario Final (solo tiene el JAR)

**Archivos necesarios:**
```
mi-carpeta/
├── civiconnect-client.jar
└── ejecutar-windows.bat  (o ejecutar-mac.sh)
```

**Windows:**
1. Coloca `ejecutar-windows.bat` en la misma carpeta que el JAR
2. Haz doble clic en `ejecutar-windows.bat`

**macOS:**
1. Coloca `ejecutar-mac.sh` en la misma carpeta que el JAR
2. Abre Terminal en esa carpeta
3. Ejecuta: `./ejecutar-mac.sh`

### Escenario 2: Desarrollador (tiene código fuente)

**Windows:**
1. Coloca `install-windows.bat` en la raíz del proyecto
2. Haz doble clic en `install-windows.bat`
3. El script compilará y ejecutará automáticamente

**macOS:**
1. Coloca `install-mac.sh` en la raíz del proyecto
2. Abre Terminal en la raíz del proyecto
3. Ejecuta: `./install-mac.sh`

## 📋 Requisitos

### Automáticos (instalados por los scripts)
- Java 21 (OpenJDK)
- Maven 3.6+ (solo para desarrolladores)

### Manuales
- Conexión a Internet (para instalar dependencias)
- Permisos de administrador (para instalar software)

## 🔧 Personalización

### Cambiar la URL del Servidor

Si necesitas cambiar la URL del servidor preconfigurada, edita el archivo:
```
src/main/java/com/vectorgarman/api/ClienteAPI.java
```

Busca la línea:
```java
private static final String BASE_URL = "https://civiconnect-api.onrender.com/api";
```

Y cámbiala por tu URL.

## 📞 Soporte

Para más información, consulta:
- **Usuarios Finales:** `README-USUARIO.md` en esta carpeta
- **Desarrolladores:** `INSTALACION.md` en la raíz del proyecto

---

**Última actualización:** Diciembre 2024