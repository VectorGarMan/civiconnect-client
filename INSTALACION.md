# CiviConnect Cliente - Guía de Instalación

Esta guía te ayudará a instalar y ejecutar la aplicación de escritorio CiviConnect en **Windows** y **macOS**.

**CiviConnect Cliente** es una aplicación de escritorio Java Swing que se conecta a un servidor remoto. Cuando ejecutes la aplicación por primera vez, se te pedirá que ingreses la URL del servidor donde está alojada la API de CiviConnect.

## Tabla de Contenidos
1. [Requisitos Previos](#requisitos-previos)
2. [Instalación](#instalación)
3. [Ejecutar la Aplicación](#ejecutar-la-aplicación)
4. [Usar la Aplicación](#usar-la-aplicación)
5. [Solución de Problemas](#solución-de-problemas)

---

## Requisitos Previos

### Software Requerido

Solo necesitas tener **Java 25** o superior instalado en tu computadora.

---

## Instalación

### Paso 1: Instalar Java

#### Instalación en Windows

**Opción A: Usando el Instalador (Recomendado)**

1. Descarga JDK 25 de Oracle:
   - Visita: https://www.oracle.com/java/technologies/downloads/
   - Haz clic en la pestaña "Windows"
   - Descarga "x64 Installer" (por ejemplo, `jdk-25_windows-x64_bin.exe`)

2. Instala Java
   - Haz doble clic en el archivo `.exe` descargado
   - Sigue el asistente de instalación
   - Acepta la ruta de instalación predeterminada

3. Verifica la instalación:
   - Abre el Símbolo del sistema (presiona `Win + R`, escribe `cmd`, presiona Enter)
   - Escribe: `java -version`
   - Deberías ver algo como:
     ```
     java version "25" 2025-XX-XX
     Java(TM) SE Runtime Environment (build 25+XX)
     ```

**Opción B: Usando Chocolatey**

Si tienes el administrador de paquetes Chocolatey instalado:

```powershell
# Abre PowerShell como Administrador
choco install openjdk --version=25.0.0
```

#### Instalación en macOS

**Opción A: Usando Homebrew (Recomendado)**

1. Instala Homebrew si no lo tienes:
   ```bash
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   ```

2. Instala Java:
   ```bash
   # Instala OpenJDK 25
   brew install openjdk@25
   
   # Vincúlalo al sistema
   sudo ln -sfn /opt/homebrew/opt/openjdk@25/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-25.jdk
   ```

3. Verifica la instalación:
   ```bash
   java -version
   ```

**Opción B: Usando el Instalador**

1. Descarga JDK 25 de Oracle:
   - Visita: https://www.oracle.com/java/technologies/downloads/
   - Haz clic en la pestaña "macOS"
   - Descarga el instalador (archivo `.dmg`)

2. Instala Java:
   - Haz doble clic en el archivo `.dmg` descargado
   - Sigue las instrucciones de instalación

3. Verifica la instalación:
   - Abre Terminal
   - Escribe: `java -version`

---

### Paso 2: Descargar CiviConnect Cliente

1. Descarga la última versión de `civiconnect-client.jar` desde:
   - **Enlace directo**: https://github.com/VectorGarMan/civiconnect-client/tree/main/target
   
2. Guarda el archivo en una ubicación que puedas encontrar fácilmente, como:
   - **Windows**: `C:\Users\TuUsuario\Downloads\` o `C:\CiviConnect\`
   - **macOS**: `~/Downloads/` o `~/Applications/CiviConnect/`

---

## Ejecutar la Aplicación

### Windows

**Método 1: Doble Clic (Más Fácil)**

1. Navega a donde guardaste `civiconnect-client.jar`
2. Haz doble clic en el archivo JAR
3. La aplicación debería iniciarse

Si el doble clic no funciona, prueba el Método 2.

**Método 2: Usando el Símbolo del Sistema**

1. Abre el Símbolo del sistema:
   - Presiona `Win + R`
   - Escribe `cmd` y presiona Enter

2. Navega a la carpeta que contiene el archivo JAR:
   ```cmd
   cd C:\Users\TuUsuario\Downloads
   ```
   *(Reemplaza con tu ruta actual)*

3. Ejecuta la aplicación:
   ```cmd
   java -jar civiconnect-client.jar
   ```

**Método 3: Crear un Acceso Directo**

1. Haz clic derecho en tu escritorio → Nuevo → Acceso directo
2. En el campo de ubicación, ingresa:
   ```
   "C:\Program Files\Java\jdk-25\bin\java.exe" -jar "C:\Users\TuUsuario\Downloads\civiconnect-client.jar"
   ```
   *(Ajusta las rutas según tu instalación)*
3. Haz clic en Siguiente, nómbralo "CiviConnect" y haz clic en Finalizar
4. Haz doble clic en el acceso directo para ejecutar la aplicación

---

### macOS

**Método 1: Doble Clic (Más Fácil)**

1. Navega a donde guardaste `civiconnect-client.jar`
2. Haz doble clic en el archivo JAR
3. Si se te solicita, haz clic en "Abrir" para permitir que la aplicación se ejecute

Si el doble clic no funciona, prueba el Método 2.

**Método 2: Usando Terminal**

1. Abre Terminal:
   - Presiona `Cmd + Espacio`
   - Escribe "Terminal" y presiona Enter

2. Navega a la carpeta que contiene el archivo JAR:
   ```bash
   cd ~/Downloads
   ```
   *(Reemplaza con tu ruta actual)*

3. Ejecuta la aplicación:
   ```bash
   java -jar civiconnect-client.jar
   ```

**Método 3: Crear un Lanzador de Aplicación**

1. Abre Terminal y crea un script:
   ```bash
   nano ~/Applications/CiviConnect/start-civiconnect.sh
   ```

2. Agrega el siguiente contenido:
   ```bash
   #!/bin/bash
   cd ~/Applications/CiviConnect
   java -jar civiconnect-client.jar
   ```

3. Guarda y sal (Ctrl+X, luego Y, luego Enter)

4. Hazlo ejecutable:
   ```bash
   chmod +x ~/Applications/CiviConnect/start-civiconnect.sh
   ```

5. Haz doble clic en el script para ejecutar la aplicación

---

## Usar la Aplicación

### Configuración Inicial

Cuando ejecutes CiviConnect por primera vez:

1. **Ingresa la URL del Servidor**
   - Al iniciar por primera vez, la aplicación te pedirá que ingreses la URL del servidor
   - Ingresa la URL proporcionada por tu administrador (por ejemplo, `https://api.civiconnect.com`)
   - Asegúrate de incluir `https://` o `http://` al principio
   - Haz clic en "Conectar" para continuar
   - La aplicación guardará esta URL para uso futuro

2. **Registrar una nueva cuenta** (si no tienes una):
   - Haz clic en el botón **"Registro"**
   - Completa el formulario con tu información:
     - Dirección de correo electrónico
     - Contraseña (elige una contraseña segura)
     - Nombre de usuario (nombre para mostrar)
     - Tipo de usuario (Ciudadano para usuario regular)
     - Ubicación (Estado → Municipio → Colonia)
   - Haz clic en **"Registrar"** para crear tu cuenta

3. **Iniciar Sesión**:
   - Ingresa tu correo electrónico y contraseña
   - Haz clic en **"Iniciar Sesión"**
   - Serás llevado a la pantalla principal de reportes



## Solución de Problemas

### La Aplicación No Inicia

**Problema**: Hacer doble clic en el archivo JAR no hace nada

**Soluciones**:

1. **Verifica que Java esté instalado**:
   
   **Windows**:
   ```cmd
   java -version
   ```
   
   **macOS**:
   ```bash
   java -version
   ```
   
   Si ves un error, Java no está instalado o no está en tu PATH. Reinstala Java siguiendo las instrucciones anteriores.

2. **Ejecuta desde la línea de comandos** para ver mensajes de error:
   
   **Windows**:
   ```cmd
   cd C:\Users\TuUsuario\Downloads
   java -jar civiconnect-client.jar
   ```
   
   **macOS**:
   ```bash
   cd ~/Downloads
   java -jar civiconnect-client.jar
   ```

3. **Verifica la asociación de archivos**:
   - Haz clic derecho en el archivo JAR
   - Selecciona "Abrir con" → "Java(TM) Platform SE binary"
   - Marca "Usar siempre esta aplicación"

---

### No se Puede Conectar al Servidor

**Problema**: La aplicación muestra errores de conexión o "No se puede alcanzar el servidor"

**Soluciones**:

1. **Verifica la URL del servidor**:
   - Asegúrate de haber ingresado la URL correcta del servidor
   - Verifica que incluiste `https://` o `http://` al principio
   - Confirma la URL con tu administrador
   - Intenta volver a ingresar la URL en la aplicación

2. **Verifica tu conexión a internet**:
   - Asegúrate de estar conectado a internet
   - Intenta abrir un sitio web en tu navegador

3. **Verifica si el servidor es accesible**:
   
   **Windows**:
   ```cmd
   ping api.civiconnect.com
   ```
   
   **macOS**:
   ```bash
   ping api.civiconnect.com
   ```
   *(Reemplaza con la dirección real de tu servidor)*

4. **Configuración del firewall**:
   - Asegúrate de que tu firewall permita que Java acceda a internet
   - **Windows**: Configuración → Seguridad de Windows → Firewall y protección de red → Permitir una aplicación a través del firewall → Agregar Java
   - **macOS**: Preferencias del Sistema → Seguridad y Privacidad → Firewall → Opciones del Firewall → Agregar Java

5. **Contacta a tu administrador**:
   - El servidor podría estar en mantenimiento
   - Podría haber restricciones de red en tu organización

---

### Problemas de Inicio de Sesión

**Problema**: No puedes iniciar sesión con las credenciales correctas

**Soluciones**:

1. **Verifica las credenciales**:
   - Revisa si hay errores tipográficos en el correo electrónico y la contraseña
   - Las contraseñas distinguen entre mayúsculas y minúsculas
   - Asegúrate de que Bloq Mayús esté desactivado

2. **Restablece la contraseña**:
   - Haz clic en "Olvidé mi contraseña" si está disponible
   - O contacta a tu administrador

3. **Verifica el estado de la cuenta**:
   - Tu cuenta podría necesitar verificación
   - Contacta a tu administrador si eres empleado gubernamental

---

### Error de Versión de Java

**Problema**: Mensaje de error sobre versión de Java no compatible

**Solución**:

La aplicación requiere Java 25 o superior. Actualiza tu instalación de Java:

1. Desinstala la versión antigua de Java:
   - **Windows**: Configuración → Aplicaciones → Buscar Java → Desinstalar
   - **macOS**: `sudo rm -rf /Library/Java/JavaVirtualMachines/jdk-version-antigua.jdk`

2. Instala Java 25 siguiendo las instrucciones en la sección [Instalación](#instalación)

3. Verifica la nueva versión:
   ```bash
   java -version
   ```

---

### Problemas de Visualización

**Problema**: Los elementos de la interfaz son demasiado pequeños o demasiado grandes

**Soluciones**:

1. **Ajusta la escala de visualización**:
   - **Windows**: Clic derecho en el escritorio → Configuración de pantalla → Escala y diseño
   - **macOS**: Preferencias del Sistema → Pantallas → Resolución

2. **Ejecuta con opciones de escala**:
   
   **Windows**:
   ```cmd
   java -Dsun.java2d.uiScale=1.5 -jar civiconnect-client.jar
   ```
   
   **macOS**:
   ```bash
   java -Dsun.java2d.uiScale=1.5 -jar civiconnect-client.jar
   ```
   
   Ajusta el valor de escala (1.0, 1.5, 2.0) según sea necesario.

---

### La Aplicación se Bloquea o Congela

**Problema**: La aplicación deja de responder

**Soluciones**:

1. **Cierra y reinicia**:
   - Fuerza el cierre de la aplicación
   - **Windows**: Ctrl+Alt+Supr → Administrador de tareas → Finalizar tarea
   - **macOS**: Cmd+Opción+Esc → Forzar salida

2. **Verifica los recursos del sistema**:
   - Cierra otras aplicaciones para liberar memoria
   - Reinicia tu computadora

3. **Ejecuta con más memoria**:
   ```bash
   java -Xmx512m -jar civiconnect-client.jar
   ```

4. **Busca actualizaciones**:
   - Descarga la última versión de la aplicación
   - Contacta a tu administrador

---

## Requisitos del Sistema

### Requisitos Mínimos
- **Sistema Operativo**: 
  - Windows 10 o posterior
  - macOS 12 (Monterey) o posterior
- **Java**: Versión 25 o superior
- **RAM**: 512 MB disponibles
- **Espacio en Disco**: 100 MB de espacio libre
- **Internet**: Conexión a internet activa requerida

### Requisitos Recomendados
- **RAM**: 1 GB o más
- **Espacio en Disco**: 500 MB de espacio libre
- **Internet**: Conexión de banda ancha para mejor rendimiento

---

## Preguntas Frecuentes

### P: ¿Necesito instalar algo más además de Java?
**R**: No, Java es el único requisito. La aplicación es un archivo JAR autocontenido (aplicación de escritorio Java Swing).

### P: ¿Cuál es la URL del servidor que debo ingresar?
**R**: Contacta a tu administrador para obtener la URL correcta del servidor. Se verá algo como `https://api.civiconnect.com` o `http://tu-servidor.com`. Asegúrate de incluir el prefijo `https://` o `http://`.

### P: ¿Puedo usar la aplicación sin conexión?
**R**: No, la aplicación requiere una conexión a internet para comunicarse con el servidor.

### P: ¿Cómo actualizo la aplicación?
**R**: Descarga la última versión del archivo JAR y reemplaza el antiguo. Tus credenciales de inicio de sesión y datos están almacenados en el servidor, por lo que no perderás nada.

### P: ¿Mis datos están seguros?
**R**: Sí, toda la comunicación con el servidor está encriptada. Tu contraseña nunca se almacena en tu computadora.

### P: ¿Puedo instalar esto en varias computadoras?
**R**: Sí, puedes descargar y ejecutar la aplicación en cualquier computadora. Solo inicia sesión con tus credenciales.

### P: La aplicación está en español. ¿Puedo cambiar el idioma?
**R**: Actualmente, la aplicación solo está disponible en español. Contacta a tu administrador si necesitas soporte para otros idiomas.

### P: ¿A quién contacto para soporte?
**R**: Contacta a tu administrador del sistema o al equipo de soporte de CiviConnect para asistencia.

---

## Referencia Rápida

### Iniciar la Aplicación

**Windows**:
```cmd
java -jar civiconnect-client.jar
```

**macOS**:
```bash
java -jar civiconnect-client.jar
```

### Verificar Versión de Java

```bash
java -version
```

### Ubicaciones Comunes

**Windows**:
- Instalación de Java: `C:\Program Files\Java\jdk-25\`
- Aplicación: `C:\Users\TuUsuario\Downloads\civiconnect-client.jar`

**macOS**:
- Instalación de Java: `/Library/Java/JavaVirtualMachines/openjdk-25.jdk/`
- Aplicación: `~/Downloads/civiconnect-client.jar`

---

## Ayuda Adicional

### Obtener la Aplicación

- **Descarga**: Contacta a tu administrador para el enlace de descarga
- **GitHub**: https://github.com/VectorGarMan/civiconnect-client/releases
- **Soporte**: Contacta a tu administrador del sistema

### Reportar Problemas

Si encuentras problemas:
1. Anota el mensaje de error exacto
2. Consulta esta guía de solución de problemas
3. Contacta a tu administrador con:
   - Descripción del problema
   - Mensajes de error (si los hay)
   - Tu sistema operativo y versión de Java
   - Pasos para reproducir el problema

---

## Información de Versión

- **Aplicación**: CiviConnect Cliente v1.0
- **Java Requerido**: 25 o superior
- **Plataformas Soportadas**: Windows 10+, macOS 12+

---

**Última Actualización**: 2025-11-29  
**Versión**: 1.0.0

---

## Licencia

Derechos reservados 2025

## Contacto

Para soporte y preguntas, contacta a tu administrador del sistema o visita:
- GitHub: https://github.com/VectorGarMan/civiconnect-client