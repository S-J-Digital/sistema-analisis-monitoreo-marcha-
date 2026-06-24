<div align="center">

# 📱 Caminata — App Móvil Android

### Captura de señales IMU y monitoreo de marcha en adultos mayores

<br/>

[![Android](https://img.shields.io/badge/Android-SDK%2021%2B-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Java-8-007396?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org/)
[![Retrofit](https://img.shields.io/badge/Retrofit-2.x-48B983?style=for-the-badge)](https://square.github.io/retrofit/)
[![License](https://img.shields.io/badge/Licencia-MIT-yellow?style=for-the-badge)](../LICENSE)

<br/>

> **Aplicación Android nativa** que recolecta datos de los sensores inerciales del dispositivo (acelerómetro, giroscopio y magnetómetro), los envía al backend de microservicios y gestiona el ciclo completo de evaluación de marcha desde el teléfono del profesional de salud.

</div>

---

## 📋 Tabla de Contenidos

- [Descripción del Sistema](#-descripción-del-sistema)
- [Características Principales](#-características-principales)
- [Arquitectura de la App](#-arquitectura-de-la-app)
- [Módulos del Proyecto](#-módulos-del-proyecto)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Permisos Requeridos](#-permisos-requeridos)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Build](#-instalación-y-build)
- [Configuración de la API](#-configuración-de-la-api)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Flujo de Uso](#-flujo-de-uso)
- [Licencia](#-licencia)
- [Contacto](#-contacto)

---

## 🎯 Descripción del Sistema

**Caminata** es la aplicación móvil del Sistema de Análisis de Monitoreo de la Marcha. Opera como cliente del backend de microservicios y como recolector principal de datos inerciales.

El profesional de salud utiliza la app para:

1. Autenticarse contra el microservicio de usuarios.
2. Registrar y gestionar participantes/pacientes.
3. Iniciar sesiones de captura de señales IMU en tiempo real.
4. Enviar las muestras capturadas al backend para su análisis por la red neuronal.
5. Consultar los resultados de detección de patologías de marcha.

---

## ✨ Características Principales

| Característica | Detalle |
|----------------|---------|
| 📡 **Captura IMU** | Acelerómetro, giroscopio y magnetómetro a 10/50/100 Hz |
| 🔧 **Configuración de sensores** | Frecuencia de muestreo y rango del acelerómetro configurables |
| 🔐 **Autenticación JWT** | Login con token Bearer almacenado en caché local |
| 👥 **Gestión de participantes** | CRUD completo integrado con el backend |
| 📤 **Envío de datos** | Carga asíncrona de señales IMU mediante Retrofit |
| 📶 **Monitor de red** | Servicio en primer plano que detecta conectividad |
| 🔔 **Recordatorios** | Notificaciones programadas para seguimientos |
| 📲 **Bluetooth** | Integración para conexión con dispositivos externos |
| 🗑️ **Limpieza al desinstalar** | `UninstallReceiver` elimina datos locales al desinstalar |
| 🏗️ **Arquitectura modular** | Multi-módulo Gradle para máxima separación de responsabilidades |

---

## 🏗 Arquitectura de la App

La aplicación sigue una **arquitectura modular monolítica** organizada en capas `core` y `feature`:

```
┌─────────────────────────────────────────────────────┐
│                   :app (Launcher)                    │
│  MainActivity · InternetConnectionService            │
│  UninstallReceiver                                   │
└──────────────┬──────────────────────────────────────┘
               │
       ┌───────┴────────┐
       ▼                ▼
  :feature            :core
  ─────────           ──────
  enviodatos          model
  getSensores         database
  funcionalidades-    ui/principal
    interfazinsertar  ui/user
  funcionalidades-    ui/participante
    Bluetooth         ui/datos
  funcionalidades-    ui/sennales
    Config            ui/config
  funcionalidades-    ui/asignarrol
    Recordatorio      ui/interfazinsertar
  visualizar-         util/user
    Funcionalidades   util/cache
                      util/encriptacion
                      util/retroceso
                      util/utilList
```

### Comunicación con el Backend

```
App Móvil (Retrofit)
       │
       │ HTTPS  +  JWT Bearer Token
       ▼
API Gateway  :8081  (Spring Cloud Gateway)
       │
       ├── microservicio-usuario  :8085  (Auth)
       ├── microservicio-participante :8086
       ├── microservicio-dato     :8087
       ├── microservicio-sennal   :8089  (Señales IMU)
       └── microservicio-redneuronal :8092 (Predicción)
```

---

## 📦 Módulos del Proyecto

### Módulo `:app`

Punto de entrada de la aplicación. Contiene:

| Clase | Responsabilidad |
|-------|----------------|
| `MainActivity` | Activity principal, orquesta la navegación entre módulos |
| `InternetConnectionService` | Servicio en primer plano que monitorea la conectividad de red |
| `UninstallReceiver` | `BroadcastReceiver` que limpia datos locales al desinstalar la app |

---

### Módulos `:feature`

| Módulo | Descripción |
|--------|-------------|
| `enviodatos` | Lógica de comunicación con la API REST (Retrofit): envío de señales, participantes, login |
| `getSensores` | Acceso a los sensores IMU del dispositivo (acelerómetro, giroscopio, magnetómetro) |
| `funcionalidadinterfazinsertar` | Flujo de inserción de nueva sesión de captura |
| `funcionalidadesBluetooth` | Conexión y gestión de dispositivos Bluetooth externos |
| `funcionalidadesConfig` | Configuración de parámetros de los sensores (frecuencia, rango) |
| `FuncionalidadRecordatorio` | Programación y gestión de recordatorios con notificaciones |
| `visualizarFuncionalidades` | Pantallas de visualización de sesiones y resultados |

---

### Módulos `:core`

#### UI (`core:ui:*`)

| Módulo | Descripción |
|--------|-------------|
| `principal` | Pantalla principal / dashboard tras autenticación |
| `user` | Pantallas de login y gestión de cuenta de usuario |
| `participante` | Pantallas de listado y detalle de participantes |
| `datos` | Pantallas de sesiones de captura de datos IMU |
| `sennales` | Pantallas de visualización de señales registradas |
| `config` | Pantalla de configuración de sensores |
| `asignarrol` | Pantalla de asignación de roles a usuarios |
| `interfazinsertar` | UI compartida para formularios de inserción |

#### Utilidades (`core:util:*`)

| Módulo | Descripción |
|--------|-------------|
| `user` | Helpers para gestión del usuario autenticado |
| `cache` | Almacenamiento local del token JWT y datos de sesión |
| `encriptacion` | Utilidades de cifrado para proteger datos locales |
| `retroceso` | Manejo del botón de retroceso y navegación |
| `utilList` | Helpers para listas y adaptadores de RecyclerView |

#### Datos (`core:model`, `core:database`)

| Módulo | Descripción |
|--------|-------------|
| `model` | Entidades y modelos de datos compartidos entre módulos |
| `database` | Acceso a la base de datos local (Room/SQLite) |

---

## 🛠 Tecnologías Utilizadas

| Categoría | Tecnología | Versión |
|-----------|-----------|---------|
| Lenguaje | Java | 8 |
| SDK mínimo | Android | API 21 (Android 5.0) |
| SDK objetivo | Android | API 33 (Android 13) |
| SDK compilación | Android | API 34 |
| Build | Gradle + Android Gradle Plugin | 8.0.2 |
| UI | AndroidX AppCompat | 1.7.0 |
| UI | Material Design | 1.5.0 |
| UI | ConstraintLayout | 2.1.4 |
| HTTP Client | Retrofit 2 | 2.x |
| Background | WorkManager | 2.8.1 |
| Tests | JUnit | 4.13.2 |
| Tests | Mockito | 2.25.0 |
| Tests UI | Espresso | 3.6.1 |

---

## 🔑 Permisos Requeridos

| Permiso | Motivo |
|---------|--------|
| `INTERNET` | Comunicación con el API Gateway |
| `ACCESS_NETWORK_STATE` | Monitoreo de conectividad de red |
| `BLUETOOTH` / `BLUETOOTH_ADMIN` / `BLUETOOTH_CONNECT` | Conexión con sensores externos |
| `POST_NOTIFICATIONS` | Recordatorios y alertas de sesión |
| `READ_CALENDAR` / `WRITE_CALENDAR` | Gestión de recordatorios en el calendario |
| `READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE` | Exportación de datos (hasta Android 13) |

---

## 📋 Requisitos Previos

| Herramienta | Versión mínima | Notas |
|------------|---------------|-------|
| Android Studio | Flamingo+ | IDE recomendado |
| JDK | **8** | Incluido con Android Studio |
| Android SDK | **API 21+** | Instalar desde SDK Manager |
| Gradle | **8.x** | Gestionado por `gradlew` |
| Backend activo | — | API Gateway en `http://<IP>:8081` |

> ⚠️ La app se comunica con el backend mediante la IP de red local. Asegúrate de que el dispositivo y el servidor estén en la misma red.

---

## 🚀 Instalación y Build

### 1. Clonar el repositorio

```bash
git clone https://github.com/<usuario>/API-RedNeuronal-master.git
cd API-RedNeuronal-master/mobile
```

### 2. Abrir en Android Studio

```
File → Open → seleccionar la carpeta mobile/
```

Android Studio sincronizará automáticamente los módulos Gradle.

### 3. Build desde línea de comandos

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease

# Ejecutar tests unitarios
./gradlew test

# Ejecutar tests instrumentados (requiere dispositivo/emulador)
./gradlew connectedAndroidTest
```

El APK generado se encuentra en:

```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 🔧 Configuración de la API

La URL base del API Gateway se configura en el módulo `feature:enviodatos`. Ajusta la IP según tu entorno:

```java
// feature/enviodatos/src/main/java/com/example/enviodatos/retrofit/
private static final String BASE_URL = "http://<IP_DEL_SERVIDOR>:8081/";
```

> Para pruebas en emulador, usa `http://10.0.2.2:8081/` (la IP del host desde el emulador Android).

---

## 📁 Estructura del Proyecto

```
mobile/
│
├── build.gradle                    # Configuración raíz (plugins AGP)
├── settings.gradle                 # Declaración de todos los módulos
├── gradle.properties               # Propiedades JVM y AndroidX
├── gradlew / gradlew.bat           # Gradle Wrapper
│
├── app/                            # Módulo principal (launcher)
│   └── src/main/
│       ├── AndroidManifest.xml     # Permisos y declaración de componentes
│       └── java/com/example/caminata/
│           ├── MainActivity.java
│           ├── InternetConnectionService.java
│           └── UninstallReceiver.java
│
├── core/
│   ├── model/                      # Entidades y DTOs compartidos
│   ├── database/                   # Capa de persistencia local
│   ├── ui/
│   │   ├── principal/              # Dashboard principal
│   │   ├── user/                   # Login y gestión de cuenta
│   │   ├── participante/           # Pantallas de participantes
│   │   ├── datos/                  # Pantallas de sesiones IMU
│   │   ├── sennales/               # Pantallas de señales
│   │   ├── config/                 # Configuración de sensores
│   │   ├── asignarrol/             # Asignación de roles
│   │   └── interfazinsertar/       # UI de formularios de inserción
│   └── util/
│       ├── user/                   # Helpers de usuario
│       ├── cache/                  # Caché local del token JWT
│       ├── encriptacion/           # Utilidades de cifrado
│       ├── retroceso/              # Manejo de navegación
│       └── utilList/               # Helpers de listas
│
└── feature/
    ├── enviodatos/                 # Cliente Retrofit + DTOs de la API
    │   ├── Dto/                    # DTOs: Usuario, Dato, Participante, Señal, Red Neuronal
    │   ├── impl/                   # Implementaciones de envío por entidad
    │   ├── retrofit/               # Interfaces de la API y factory de Retrofit
    │   └── service/                # Servicios de background para envío
    ├── getSensores/                # Captura de datos del acelerómetro/giroscopio/magnetómetro
    ├── funcionalidadinterfazinsertar/  # Flujo de nueva sesión
    ├── funcionalidadesBluetooth/   # Gestión Bluetooth
    ├── funcionalidadesConfig/      # Configuración de sensores
    ├── FuncionalidadRecordatorio/  # Recordatorios y notificaciones
    └── visualizarFuncionalidades/  # Visualización de resultados
```

---

## 🔄 Flujo de Uso

```
1. Login
   └─▶ POST /api/login/ → recibe JWT → almacena en cache local

2. Gestión de participantes
   └─▶ Crear / buscar por CI / listar participantes del usuario autenticado

3. Iniciar sesión de captura
   └─▶ POST /api/v1/datoparticipante/create → obtiene idDato

4. Captura de señales IMU
   └─▶ getSensores activa acelerómetro + giroscopio + magnetómetro
   └─▶ POST /api/v1/sennal/create (por cada muestra)

5. Análisis y resultado
   └─▶ POST /api/v1/modelo/predecir → retorna patología detectada o "marcha normal"
```

---

## 📄 Licencia

Distribuido bajo la licencia **MIT**. Consulta el archivo `LICENSE` en la raíz del proyecto para más detalles.

---

## 📬 Contacto

**Rayner Alejandro Soto Martínez**
📧 [raynersoto01@gmail.com](mailto:raynersoto01@gmail.com)
🔗 [github.com/raynersoto](https://github.com/raynersoto)

---

<div align="center">

**Desarrollado con ❤️ para la detección temprana de patologías en adultos mayores**

</div>
