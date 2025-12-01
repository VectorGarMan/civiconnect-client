# CiviConnect Cliente

Una aplicación de escritorio Java Swing para reportar y gestionar problemas cívicos en tu comunidad.

## 📋 Descripción

CiviConnect Cliente es una aplicación de escritorio que permite a los ciudadanos reportar problemas en su comunidad (baches, alumbrado público, basura, etc.) y hacer seguimiento de los mismos. La aplicación se conecta a un servidor remoto para sincronizar los datos.

## 🚀 Características

- 🔐 Sistema de autenticación de usuarios
- 📝 Crear y gestionar reportes de problemas cívicos
- 📍 Selección de ubicación por Estado, Municipio y Colonia
- 💬 Sistema de comentarios en reportes
- 👍 Votación en reportes para priorización
- 🖼️ Soporte para evidencias fotográficas
- 🔄 Sincronización en tiempo real con el servidor

## 📦 Requisitos Previos

- **Java Development Kit (JDK) 21** o superior
- **Maven 3.6+** (para compilar desde el código fuente)
- Conexión a internet (para conectarse al servidor)

### Verificar Java

```bash
java -version
```

Deberías ver algo como:
```
java version "21" 2024-XX-XX
Java(TM) SE Runtime Environment (build 21+XX)
```

### Verificar Maven

```bash
mvn -version
```

## 🛠️ Compilación

### Clonar el Repositorio

```bash
git clone https://github.com/VectorGarMan/civiconnect-client.git
cd civiconnect-client
```

### Compilar el Proyecto

Para generar el archivo JAR ejecutable:

```bash
mvn clean package
```

Este comando:
1. Limpia compilaciones anteriores (`clean`)
2. Compila el código fuente
3. Ejecuta las pruebas
4. Empaqueta la aplicación en un JAR ejecutable (`package`)

El archivo JAR se generará en:
```
civiconnect-client/target/civiconnect-client.jar
```

### Compilar sin Ejecutar Pruebas

Si deseas compilar más rápido omitiendo las pruebas:

```bash
mvn clean package -DskipTests
```

## ▶️ Ejecución

### Ejecutar desde el JAR Compilado

```bash
java -jar target/civiconnect-client.jar
```

### Ejecutar con Maven

```bash
mvn exec:java -Dexec.mainClass="com.vectorgarman.Main"
```

### Ejecutar con Más Memoria (Opcional)

Si la aplicación necesita más memoria:

```bash
java -Xmx512m -jar target/civiconnect-client.jar
```

## 📁 Estructura del Proyecto

```
civiconnect-client/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── vectorgarman/
│   │   │           ├── Main.java                          # Punto de entrada
│   │   │           ├── api/
│   │   │           │   └── ClienteAPI.java                # Cliente HTTP para API REST
│   │   │           ├── dto/                               # Objetos de transferencia de datos
│   │   │           │   ├── ActualizarNombreUsuarioRequest.java
│   │   │           │   ├── ApiResponse.java
│   │   │           │   ├── CambioContrasenaRequest.java
│   │   │           │   ├── Colonia.java
│   │   │           │   ├── Comentario.java
│   │   │           │   ├── ComentarioRequest.java
│   │   │           │   ├── CrearReporteRequest.java
│   │   │           │   ├── EditarComentarioRequest.java
│   │   │           │   ├── EditarReporteRequest.java
│   │   │           │   ├── EliminarComentarioRequest.java
│   │   │           │   ├── Estado.java
│   │   │           │   ├── Evidencia.java
│   │   │           │   ├── ItemReporte.java
│   │   │           │   ├── LoginRequest.java
│   │   │           │   ├── Municipio.java
│   │   │           │   ├── ReporteView.java
│   │   │           │   ├── ReporteViewDto.java
│   │   │           │   ├── TipoUsuario.java
│   │   │           │   ├── Ubicacion.java
│   │   │           │   ├── Usuario.java
│   │   │           │   ├── UsuarioRequest.java
│   │   │           │   └── VotarReporteRequest.java
│   │   │           ├── utils/
│   │   │           │   └── SessionManager.java            # Gestión de sesión y tokens
│   │   │           └── views/                             # Interfaces gráficas (Swing)
│   │   │               ├── CambiarContrasena.form         # Diseño UI cambio contraseña
│   │   │               ├── CambiarContrasena.java         # Lógica cambio contraseña
│   │   │               ├── Login.form                     # Diseño UI login
│   │   │               ├── Login.java                     # Lógica login
│   │   │               ├── Perfil.java                    # Vista perfil usuario
│   │   │               ├── Registro.form                  # Diseño UI registro
│   │   │               ├── Registro.java                  # Lógica registro
│   │   │               ├── Reportes.form                  # Diseño UI reportes
│   │   │               └── Reportes.java                  # Lógica gestión reportes
│   │   └── resources/
│   │       └── assets/
│   │           └── CiviConnectCut.png                     # Logo de la aplicación
│   └── test/
│       └── java/                                          # Directorio para pruebas unitarias
├── target/                                                # Archivos compilados (generado)
│   └── civiconnect-client.jar                             # JAR ejecutable
├── pom.xml                                                # Configuración de Maven
├── .gitignore                                             # Archivos ignorados por Git
└── README.md                                              # Documentación del proyecto
```

## 🔧 Configuración

### Primera Ejecución

Al ejecutar la aplicación por primera vez, se te pedirá:

1. **URL del Servidor**: Ingresa la URL donde está alojada la API de CiviConnect
   - Ejemplo: `https://api.civiconnect.com`
   - Asegúrate de incluir `https://` o `http://`

2. **Credenciales**: Inicia sesión o regístrate como nuevo usuario

### Configuración Persistente

La aplicación guarda automáticamente:
- URL del servidor configurada
- Token de sesión (mientras esté activa)

## 🧪 Desarrollo

### Ejecutar en Modo Desarrollo

```bash
mvn clean compile exec:java -Dexec.mainClass="com.vectorgarman.Main"
```

### Limpiar Archivos Compilados

```bash
mvn clean
```

### Actualizar Dependencias

```bash
mvn clean install -U
```

## 📚 Dependencias

El proyecto utiliza las siguientes dependencias principales:

- **Gson 2.13.2**: Para serialización/deserialización JSON
- **Java Swing**: Para la interfaz gráfica (incluido en JDK)

Todas las dependencias se empaquetan en el JAR final usando Maven Shade Plugin.

## 🐛 Solución de Problemas

### Error: "Java version not compatible"

Asegúrate de tener Java 21 o superior instalado:
```bash
java -version
```

### Error: "mvn: command not found"

Instala Maven:
- **macOS**: `brew install maven`
- **Windows**: Descarga desde https://maven.apache.org/download.cgi
- **Linux**: `sudo apt-get install maven` o `sudo yum install maven`

### La Aplicación No Inicia

1. Verifica que el JAR se compiló correctamente:
   ```bash
   ls -lh target/civiconnect-client.jar
   ```

2. Ejecuta con salida de depuración:
   ```bash
   java -jar target/civiconnect-client.jar
   ```

3. Verifica los logs en la consola para mensajes de error

### Problemas de Conexión al Servidor

- Verifica tu conexión a internet
- Confirma que la URL del servidor es correcta
- Verifica que el servidor esté en línea
- Revisa la configuración del firewall

## 📖 Documentación Adicional

- **[INSTALACION.md](INSTALACION.md)**: Guía completa de instalación para usuarios finales
- **API Documentation**: Consulta la documentación del servidor CiviConnect API

## 🤝 Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📝 Comandos Útiles de Maven

| Comando | Descripción |
|---------|-------------|
| `mvn clean` | Limpia archivos compilados |
| `mvn compile` | Compila el código fuente |
| `mvn test` | Ejecuta las pruebas |
| `mvn package` | Crea el JAR ejecutable |
| `mvn clean package` | Limpia y crea el JAR |
| `mvn clean install` | Instala en repositorio local |
| `mvn dependency:tree` | Muestra árbol de dependencias |
| `mvn versions:display-dependency-updates` | Verifica actualizaciones |

## 🔐 Seguridad

- Las contraseñas nunca se almacenan localmente
- Toda comunicación con el servidor debe usar HTTPS
- Los tokens de sesión expiran automáticamente
- No compartas tu archivo de configuración con credenciales

## 📄 Licencia

Derechos reservados © 2025

## 👥 Autores

- **Victor Garza** - [VectorGarMan](https://github.com/VectorGarMan)

## 📞 Soporte

Para soporte y preguntas:
- 🐛 Reporta bugs en: [GitHub Issues](https://github.com/VectorGarMan/civiconnect-client/issues)
- 📧 Email: [Contacta al administrador]
- 📖 Documentación: [Wiki del proyecto](https://github.com/VectorGarMan/civiconnect-client/wiki)


## 📊 Estado del Proyecto

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Java Version](https://img.shields.io/badge/java-21-blue)
![License](https://img.shields.io/badge/license-proprietary-red)

---

**Última actualización**: 2025-12-01
**Versión**: 1.0.0