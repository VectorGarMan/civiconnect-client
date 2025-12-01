# CiviConnect Cliente - Guía de Usuario

## 🚀 Instalación y Ejecución Rápida

Esta carpeta contiene todo lo necesario para ejecutar CiviConnect Cliente en tu computadora.

### Windows

1. **Asegúrate de tener estos archivos en la misma carpeta:**
   - `civiconnect-client.jar` (la aplicación)
   - `ejecutar-windows.bat` (el instalador/ejecutor)

2. **Ejecuta la aplicación:**
   - Haz doble clic en `ejecutar-windows.bat`
   - Si es la primera vez, el script instalará Java automáticamente
   - Acepta los permisos de administrador si se solicitan
   - La aplicación se iniciará automáticamente

3. **Ejecuciones posteriores:**
   - Simplemente haz doble clic en `ejecutar-windows.bat` cada vez que quieras usar la aplicación

---

### macOS

1. **Asegúrate de tener estos archivos en la misma carpeta:**
   - `civiconnect-client.jar` (la aplicación)
   - `ejecutar-mac.sh` (el instalador/ejecutor)

2. **Primera ejecución:**
   - Abre Terminal (Cmd + Espacio, escribe "Terminal")
   - Arrastra la carpeta donde están los archivos a la ventana de Terminal
   - Escribe: `./ejecutar-mac.sh` y presiona Enter
   - Si es la primera vez, el script instalará Java automáticamente
   - Sigue las instrucciones en pantalla
   - La aplicación se iniciará automáticamente

3. **Ejecuciones posteriores:**
   - Puedes hacer doble clic en `ejecutar-mac.sh` o ejecutarlo desde Terminal

---

## ❓ Preguntas Frecuentes

### ¿Qué hace el script automáticamente?

El script verifica si tienes Java instalado en tu computadora:
- **Si ya tienes Java:** Simplemente ejecuta la aplicación
- **Si no tienes Java:** Lo instala automáticamente y luego ejecuta la aplicación

### ¿Necesito conexión a internet?

- **Primera vez:** Sí, para instalar Java si no lo tienes
- **Después:** Sí, la aplicación necesita internet para conectarse al servidor

### ¿Qué versión de Java necesito?

El script instala automáticamente Java 21, que es compatible con la aplicación.

### ¿Puedo mover estos archivos a otra carpeta?

Sí, pero asegúrate de mantener juntos:
- El archivo JAR (`civiconnect-client.jar`)
- El script ejecutor (`ejecutar-windows.bat` o `ejecutar-mac.sh`)

### ¿Cómo actualizo la aplicación?

Simplemente reemplaza el archivo `civiconnect-client.jar` con la nueva versión. El script ejecutor no necesita actualizarse.

---

## 🔧 Solución de Problemas

### Windows: "No se puede ejecutar el script"

- Haz clic derecho en `ejecutar-windows.bat`
- Selecciona "Ejecutar como administrador"

### macOS: "Permiso denegado"

Abre Terminal y ejecuta:
```bash
chmod +x ejecutar-mac.sh
```

### "No se encuentra el archivo JAR"

Asegúrate de que:
1. El archivo `civiconnect-client.jar` está en la misma carpeta que el script
2. El nombre del archivo es exactamente `civiconnect-client.jar`

### La aplicación no se conecta al servidor

- Verifica tu conexión a internet
- Contacta al administrador del sistema

---

## 📞 Soporte

Si tienes problemas que no se resuelven con esta guía, contacta al administrador del sistema o al equipo de soporte de CiviConnect.

---

**Versión:** 1.0  
**Última actualización:** Diciembre 2024