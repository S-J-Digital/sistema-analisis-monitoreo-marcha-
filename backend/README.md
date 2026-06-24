<div align="center">

# 🧠 API Red Neuronal — Sistema de Análisis de Marcha

### Detección de patologías de riesgo de caída en adultos mayores mediante sensores IMU y aprendizaje automático

<br/>

[![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2023.0.4-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)](https://github.com/features/actions)
[![License](https://img.shields.io/badge/Licencia-MIT-yellow?style=for-the-badge)](LICENSE)

<br/>

> **Plataforma de salud digital** que combina sensores inerciales, microservicios Spring Boot y una red neuronal Neuroph para detectar alteraciones en la marcha de adultos mayores en tiempo real.

</div>

---

## 📋 Tabla de Contenidos

- [Descripción del Sistema](#-descripción-del-sistema)
- [Arquitectura del Sistema](#-arquitectura-del-sistema)
- [Microservicios](#-microservicios)
- [Base de Datos](#-base-de-datos)
- [Red Neuronal y Pipeline de Señales](#-red-neuronal-y-pipeline-de-señales)
- [Seguridad y Autenticación](#-seguridad-y-autenticación)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Ejecución del Sistema](#-ejecución-del-sistema)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Uso y Ejemplos](#-uso-y-ejemplos)
- [Frontend — Interfaz Visual](#-frontend--interfaz-visual)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [CI/CD](#-cicd)
- [Contribución](#-contribución)
- [Licencia](#-licencia)
- [Contacto](#-contacto)

---

## 🎯 Descripción del Sistema

**API Red Neuronal** es un backend basado en microservicios diseñado para la **gestión, monitoreo y análisis de datos inerciales** provenientes de sensores corporales (acelerómetro, giroscopio y magnetómetro) capturados desde dispositivos móviles Android.

El sistema procesa las señales recogidas, extrae características del patrón de marcha y aplica un modelo de **red neuronal (Neuroph 2.98)** para detectar posibles patologías relacionadas con caídas en adultos mayores. Los resultados quedan disponibles para profesionales médicos a través de una API REST documentada con Swagger.

### ✨ Características Principales

| Característica | Detalle |
|----------------|---------|
| 🏗️ **Microservicios** | 9 servicios independientes con Netflix Eureka y Spring Cloud Gateway |
| 🔐 **Seguridad JWT** | Autenticación stateless con roles `SUPER_ADMIN`, `ADMIN`, `APP_USER` |
| 🧠 **Red Neuronal** | Entrenamiento y predicción en tiempo real con Neuroph 2.98 |
| 📡 **Sensores IMU** | Acelerómetro, giroscopio y magnetómetro a frecuencias configurables |
| 📊 **Swagger UI** | Documentación interactiva en cada microservicio |
| 🗂️ **AOP Logging** | Auditoría automática de todas las operaciones en tabla `logs` |
| 🧪 **Perfiles Maven** | `dev` (PostgreSQL) y `test` (H2 in-memory) |
| ⚡ **CI/CD** | Compilación automática con GitHub Actions en cada push |

---

## 🏗 Arquitectura del Sistema

El sistema implementa una **arquitectura híbrida** compuesta por dos subsistemas:

1. **App Móvil Android** — Monolítica modular: recolección de datos, configuración de sensores, gestión de roles y comunicación con la API.
2. **API Backend** — Microservicios independientes, escalables y desplegables de forma autónoma.

### Diagrama General

```
┌──────────────────────────────────────────────────────────────────┐
│                     App Móvil (Android)                          │
│   [ Acelerómetro ]  [ Giroscopio ]  [ Magnetómetro ]            │
│   Frecuencia: 10/50/100 Hz   Rango: 2g/4g/8g/16g               │
└───────────────────────────┬──────────────────────────────────────┘
                            │ HTTPS  +  JWT Bearer Token
                            ▼
┌──────────────────────────────────────────────────────────────────┐
│                  🚪 API Gateway  :8081                           │
│         Spring Cloud Gateway · Enrutamiento · CORS               │
└─────┬────────┬───────────┬────────────┬─────────────┬───────────┘
      │        │           │            │             │
      ▼        ▼           ▼            ▼             ▼
   :8085    :8086       :8087        :8088         :8089
 👤 Usuario 👥 Participante 📋 Dato  🏥 Patología  📡 Señal
  Auth/JWT   Pacientes    Sesiones   Catálogo    IMU+Features
                                                    │
                                          OpenFeign │
                                                    ▼
                                                 :8092
                                           🧠 Red Neuronal
                                          Neuroph · WAR · Inferencia

              ┌────────────────────────────────┐
              │  🔍 Eureka Server  :8761        │  ← Service Discovery
              │  ⚙️  Config Server              │  ← Configuración Centralizada
              └────────────────────────────────┘
```

### Comunicación entre Servicios

Los servicios se comunican de forma **sincrónica** mediante **OpenFeign** y se descubren automáticamente a través de **Eureka**:

```
microservicio-participante  ──Feign──▶  microservicio-dato
microservicio-dato          ──Feign──▶  microservicio-sennal
microservicio-sennal        ──Feign──▶  microservicio-dato
microservicio-sennal        ──Feign──▶  microservicio-patologia
microservicio-sennal        ──Feign──▶  microservicio-redneuronal
```

> 💡 `microservicio-sennal` actúa como **orquestador** del flujo de análisis: recibe datos IMU crudos, orquesta la extracción de características y coordina la clasificación con la red neuronal.

---

## ⚙️ Microservicios

### Mapa de Servicios

| Servicio | Puerto | Paquete Base | Descripción |
|----------|--------|-------------|-------------|
| `microservicio-eureka` | **8761** | `com.example.eureka` | Registro y descubrimiento (Netflix Eureka) |
| `microservicio-config` | — | — | Configuración centralizada (Spring Cloud Config) |
| `microservicio-gateway` | **8081** | `com.microservicio.gateway` | Enrutamiento, CORS, balanceo de carga |
| `microservicio-usuario` | **8085** | `com.example.usuario` | Autenticación, JWT, gestión de usuarios y roles |
| `microservicio-participante` | **8086** | `com.example.participante` | CRUD de participantes/pacientes |
| `microservicio-dato` | **8087** | `com.example.microserviciodato` | Sesiones de recolección de datos |
| `microservicio-patologia` | **8088** | `com.example.patologia` | Catálogo y clasificación de patologías |
| `microservicio-sennal` | **8089** | `com.example.sennal` | Señales IMU y extracción de características |
| `microservicio-redneuronal` | **8092** | `com.example.redneuronal` | Entrenamiento e inferencia neural (WAR) |

---

### 🔐 microservicio-usuario `:8085`

Gestiona el ciclo completo de autenticación y autorización:

- Emite y valida tokens **JWT** (`java-jwt 4.4.0`) a través de `TokenService`
- El filtro `SecurityFilter` intercepta cada request, valida el token y puebla el `SecurityContext`
- Contraseñas validadas en la BD mediante el trigger `tr_comprobar_contrasenna`
- Sesiones **stateless** (sin estado en servidor), CSRF deshabilitado

**Roles disponibles:**

| Rol | Descripción |
|-----|-------------|
| `SUPER_ADMIN` | Acceso total, gestión de todos los usuarios |
| `ADMIN` | Gestión de participantes, patologías y datos |
| `APP_USER` | Envío de datos desde la app, consulta de resultados |

---

### 👥 microservicio-participante `:8086`

- CRUD completo de participantes (nombre, CI, sexo, teléfono)
- Entidad de relación `UsuarioParticipante` que vincula médico-paciente
- Consulta de sesiones del participante vía `DatoClient` (Feign)

---

### 📋 microservicio-dato `:8087`

- Almacena metadatos de cada sesión de captura: participante, fecha, hora, edad, flag de patología detectada
- Recupera señales asociadas a la sesión vía `SennalClient` (Feign)

---

### 📡 microservicio-sennal `:8089` _(Orquestador del Pipeline)_

Servicio central del análisis de marcha:

1. **Almacena** las muestras IMU individuales en la tabla `sennal`
2. **Invoca** `ObtenerCaracteristicas` + `SennalPreccesor` para la extracción de features
3. **Llama** al `ModeloClient` (red neuronal) para inferencia
4. **Registra** la patología detectada a través de `PatologiaClient`

**Estructura de una muestra IMU:**

| Campo | Sensor | Unidad |
|-------|--------|--------|
| `acelerometroX/Y/Z` | Acelerómetro | m/s² |
| `giroscopioX/Y/Z` | Giroscopio | °/s |
| `magnetometroX/Y/Z` | Magnetómetro | µT |
| `rango` | Configuración | 2g / 4g / 8g / 16g |
| `frecuencia` | Muestreo | Hz (10 / 50 / 100) |

---

### 🏥 microservicio-patologia `:8088`

- Catálogo de patologías registradas (tabla `patologia`)
- Trigger `tr_patologia_existente` previene duplicados (case-insensitive)
- Tabla `dato_patologia` asigna una o más patologías a una sesión de datos

---

### 🧠 microservicio-redneuronal `:8092`

Núcleo de inteligencia artificial:

- **Biblioteca:** Neuroph 2.98 — JARs locales en `libs/neuroph-2.98/` con scope `system`
- **Empaquetado:** WAR (desplegable en Tomcat embebido o externo)
- **Persistencia del modelo:** serializado en la entidad `RedNeuronalEntity` (JPA)

**Endpoints del modelo:**

| Endpoint | Acción |
|----------|--------|
| `POST /modelo/crear-inicial` | Crea y entrena el modelo con datos existentes en BD |
| `POST /modelo/entrenar` | Reentrena el modelo con nuevos datos etiquetados |
| `POST /modelo/predecir` | Ejecuta inferencia y retorna la clasificación |

---

## 🗄 Base de Datos

**Motor:** PostgreSQL 16 — Base de datos: `sensores_inteligentes`

### Diagrama Entidad-Relación

```
  ┌─────────────┐         ┌──────────────────────┐
  │   usuario   │         │     participante      │
  ├─────────────┤         ├──────────────────────┤
  │ id_usuario  │─────┐   │ id_participante       │
  │ nombre_usu. │     │   │ nombre                │
  │ contrasenna │     │   │ ci                    │
  │ rol         │     │   │ sexo                  │
  └─────────────┘     │   │ telefono              │
                      │   └──────────┬────────────┘
                      │              │ 1
              ┌───────┘              │
              │  usuario_participante│               ┌─────────────┐
              │  (relación)         ▼ N              │  patologia  │
              │              ┌────────────────┐      ├─────────────┤
              │              │datoparticipante│      │ id_patologia│
              │              ├────────────────┤      │ nombre      │
              └──────────────│ id_dato        │      └──────┬──────┘
                             │ id_participante│             │
                             │ fecha / hora   │      ┌──────▼──────────┐
                             │ edad           │      │ dato_patologia   │
                             │ patologia(bool)│◀─────│ id_dato (FK)    │
                             └───────┬────────┘      │ id_patologia(FK)│
                                     │ 1             │ fecha_evaluacion│
                                     │               └─────────────────┘
                                     ▼ N
                             ┌────────────────┐      ┌─────────────────┐
                             │    sennal      │      │      logs       │
                             ├────────────────┤      ├─────────────────┤
                             │ id_sennal      │      │ timestamp       │
                             │ id_dato (FK)   │      │ accion          │
                             │ acelerometroX  │      │ usuario         │
                             │ acelerometroY  │      │ ip              │
                             │ acelerometroZ  │      │ resultado       │
                             │ giroscopioX    │      └─────────────────┘
                             │ giroscopioY    │
                             │ giroscopioZ    │
                             │ magnetometroX  │
                             │ magnetometroY  │
                             │ magnetometroZ  │
                             │ rango          │
                             │ frecuencia     │
                             └────────────────┘
```

### Stored Procedures

| Procedimiento | Descripción |
|--------------|-------------|
| `sennal_insert(...)` | Inserta una nueva muestra de señal IMU |
| `datoparticipante_insert(...)` | Registra una sesión de captura de datos |
| `datoparticipante_update(...)` | Actualiza metadatos de una sesión |
| `datoparticipante_delete(id)` | Elimina sesión y sus señales en cascada |
| `participante_insert(...)` | Registra un nuevo participante/paciente |
| `participante_update(...)` | Actualiza datos del participante |
| `participante_delete(id)` | Elimina participante y sus registros |
| `patologia_insert(nombre)` | Agrega una patología al catálogo |
| `patologia_delete(id)` | Elimina una patología del catálogo |
| `dato_patologia_insert(...)` | Vincula una sesión con una patología detectada |

### Triggers

| Trigger | Tabla | Evento | Descripción |
|---------|-------|--------|-------------|
| `tr_comprobar_contrasenna` | `usuario` | `BEFORE INSERT` | Valida que la contraseña cumpla el formato requerido |
| `tr_patologia_existente` | `patologia` | `BEFORE INSERT` | Previene nombres de patología duplicados (insensible a mayúsculas) |

### Auditoría con AOP

Todos los controladores tienen un aspecto AOP (`ControllerAspecto`) que registra automáticamente en la tabla `logs`:

- **Timestamp** exacto de la operación
- **Método** y endpoint invocado
- **Usuario** autenticado (del SecurityContext)
- **IP** del cliente
- **Resultado** (éxito, error o excepción)

---

## 🧬 Red Neuronal y Pipeline de Señales

### Flujo Completo de Análisis

```
 📱 App Móvil
      │
      │ POST /api/v1/sennal/create  (muestras IMU crudas)
      ▼
 ┌─────────────────────────────────────────────────────┐
 │            SennalPreccesor.java                     │
 │  • Normalización según rango (2g/4g/8g/16g)         │
 │  • Filtrado de ruido y valores atípicos             │
 └────────────────────┬────────────────────────────────┘
                      ▼
 ┌─────────────────────────────────────────────────────┐
 │          ObtenerCaracteristicas.java                │
 │  • Media aritmética por eje                         │
 │  • Desviación estándar y varianza                   │
 │  • Valores mínimo y máximo                          │
 │  • Análisis de cruces por cero                      │
 │  • Correlación entre ejes                           │
 │  • Vector de características normalizado [0, 1]     │
 └────────────────────┬────────────────────────────────┘
                      ▼
 ┌─────────────────────────────────────────────────────┐
 │           Red Neuronal Neuroph                      │
 │  Capa entrada → Capas ocultas → Capa salida         │
 │  • Entrenamiento: backpropagation supervisado       │
 │  • Función de activación: sigmoidal                 │
 └────────────────────┬────────────────────────────────┘
                      ▼
 ┌─────────────────────────────────────────────────────┐
 │               Prediccion.java                       │
 │  Interpreta la salida de la red neuronal            │
 │  Retorna: tipo de patología | "marcha normal"       │
 └─────────────────────────────────────────────────────┘
```

### Configuración de Sensores (App Móvil)

| Parámetro | Opciones |
|-----------|---------|
| Frecuencia de muestreo | 10 Hz · 50 Hz · 100 Hz |
| Rango acelerómetro | 2g · 4g · 8g · 16g |
| Rango giroscopio | 250 · 500 · 1000 · 2000 °/s |

### Ciclo de Vida del Modelo

```
1. crear-inicial  → construye y entrena el modelo con los datos históricos en BD
2. entrenar       → reentrena el modelo cuando se acumulan nuevos datos etiquetados
3. predecir       → carga el modelo serializado y ejecuta inferencia sobre nuevas señales
```

---

## 🔒 Seguridad y Autenticación

### Flujo de Autenticación JWT

```
Cliente (App / Web)            microservicio-usuario :8085
       │                                │
       │── POST /api/login ────────────▶│
       │   { username, password }       │ Valida en tabla 'usuario' (BD)
       │                                │ Genera token JWT firmado
       │◀── { "token": "eyJ..." } ──────│
       │
       │── GET /api/v1/participante/all ─▶  API Gateway :8081
       │   Authorization: Bearer eyJ...        │
       │                                 SecurityFilter.java
       │                                  ├─ Extrae token del header
       │                                  ├─ TokenService.getSubject()
       │                                  ├─ Valida firma y expiración
       │                                  └─ Puebla SecurityContext
       │
       │◀── 200 OK + [ participantes ] ──────────
```

### Roles y Permisos

| Rol | Usuarios | Participantes | Datos IMU | Patologías | Modelo NN |
|-----|----------|--------------|-----------|-----------|-----------|
| `SUPER_ADMIN` | ✅ Total | ✅ Total | ✅ Total | ✅ Total | ✅ Total |
| `ADMIN` | 🔒 Limitado | ✅ Total | ✅ Total | ✅ Total | ✅ Total |
| `APP_USER` | ❌ | ✅ Solo lectura | ✅ Crear/Leer | ✅ Consultar | ✅ Predecir |

### Configuración Spring Security

```java
// Sesiones stateless — sin estado en servidor
SessionCreationPolicy.STATELESS

// CSRF deshabilitado (API REST)
csrf → disabled

// JWT validado ANTES del filtro de Spring
SecurityFilter → antes de UsernamePasswordAuthenticationFilter
```

---

## 🛠 Tecnologías Utilizadas

### Backend

| Categoría | Tecnología | Versión |
|-----------|-----------|---------|
| Lenguaje | Java | 17 |
| Framework | Spring Boot | 3.3.6 |
| Microservicios | Spring Cloud | 2023.0.4 |
| Service Discovery | Netflix Eureka | — |
| API Gateway | Spring Cloud Gateway | — |
| Comunicación | OpenFeign | — |
| Seguridad | Spring Security + JWT | `java-jwt` 4.4.0 |
| ORM | Spring Data JPA + Hibernate | 6.4.2 |
| BD Producción | PostgreSQL | 16 |
| BD Tests | H2 in-memory | 2.4.240 |
| Red Neuronal | Neuroph | 2.98 |
| Documentación API | SpringDoc OpenAPI (Swagger) | 2.5.0 |
| Serialización | Gson | 2.11.0 |
| Monitoreo | Micrometer + Prometheus | — |
| Resiliencia | Resilience4j | — |
| Utilidades | Lombok | 1.18.22 |
| Build | Apache Maven | 3.8+ |

### Frontend

| Tecnología | Versión | Uso |
|-----------|---------|-----|
| React | 19 | Framework UI |
| Vite | Última | Bundler y dev server |
| Tailwind CSS | 4 | Estilos utility-first |
| Bootstrap | 5 | Componentes UI |

### Infraestructura y DevOps

| Herramienta | Uso |
|------------|-----|
| GitHub Actions | Pipeline CI/CD automatizado |
| Eureka Server | Registro y descubrimiento de servicios |
| Spring Cloud Config | Configuración centralizada por entorno |

---

## 📦 Requisitos Previos

| Herramienta | Versión mínima | Verificar |
|------------|---------------|-----------|
| Java JDK | **17** | `java -version` |
| Apache Maven | **3.8+** | `mvn -version` |
| PostgreSQL | **16** | `psql --version` |
| Node.js _(solo frontend)_ | **18+** | `node -version` |
| npm _(solo frontend)_ | **9+** | `npm -version` |
| Git | 2.x | `git --version` |

---

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/<usuario>/API-RedNeuronal-master.git
cd API-RedNeuronal-master
```

### 2. Configurar la base de datos

```bash
# Crear la base de datos
psql -U postgres -c "CREATE DATABASE sensores_inteligentes WITH ENCODING = 'UTF8';"

# Cargar esquema completo: tablas, stored procedures y triggers
psql -U postgres -d sensores_inteligentes -f sensor_inteligente.sql
```

### 3. Compilar todos los módulos

```bash
# Sin ejecutar tests (recomendado para la primera compilación)
mvn clean install -DskipTests

# Con tests (requiere PostgreSQL activo)
mvn clean install
```

### 4. Instalar dependencias del frontend

```bash
cd interfaz_visual
npm install
cd ..
```

---

## 🔧 Configuración

### Variables de Entorno

| Variable | Descripción | Valor por defecto (dev) |
|----------|-------------|------------------------|
| `SensoresBDURL` | URL JDBC de la BD principal | `jdbc:postgresql://localhost:5432/sensores_inteligentes` |
| `RedNeuronalBDURL` | URL JDBC para el servicio de red neuronal | `jdbc:postgresql://localhost:5432/sensores_inteligentes` |
| `DB_USER` | Usuario de PostgreSQL | `postgres` |
| `BD_PASS` | Contraseña de PostgreSQL | `0000` |

> ⚠️ **Importante:** Cambia las credenciales antes de desplegar en producción.

### Perfiles Maven

```bash
# Perfil de desarrollo (PostgreSQL por defecto)
mvn spring-boot:run

# Perfil de test (H2 in-memory, sin necesidad de PostgreSQL)
mvn test -P test
```

---

## ▶️ Ejecución del Sistema

> **Orden obligatorio de arranque:** Eureka → Config → Gateway → Servicios de negocio

### Paso 1 — Eureka (Service Discovery)

```bash
mvn -pl microservicio-eureka spring-boot:run
# Panel disponible en: http://localhost:8761
```

### Paso 2 — Config Server

```bash
mvn -pl microservicio-config spring-boot:run
```

### Paso 3 — API Gateway

```bash
mvn -pl microservicio-gateway spring-boot:run
# Gateway disponible en: http://localhost:8081
```

### Paso 4 — Servicios de Negocio _(en terminales separadas, cualquier orden)_

```bash
mvn -pl microservicio-gestion/microservicio-usuario       spring-boot:run
mvn -pl microservicio-gestion/microservicio-participante  spring-boot:run
mvn -pl microservicio-gestion/microservicio-dato          spring-boot:run
mvn -pl microservicio-gestion/microservicio-patologia     spring-boot:run
mvn -pl microservicio-gestion/microservicio-sennal        spring-boot:run
mvn -pl microservicio-gestion/microservicio-redneuronal   spring-boot:run
```

### Paso 5 — Frontend React

```bash
cd interfaz_visual
npm run dev
# Disponible en: http://localhost:3000
```

### ✅ Verificar que todos los servicios están activos

Accede a `http://localhost:8761` — todos los servicios deben aparecer con estado **UP**.

### Ejecutar Tests

```bash
# Todos los módulos
mvn test

# Un módulo específico
mvn -pl microservicio-gestion/microservicio-usuario test
```

---

## 📖 Endpoints de la API

Todos los endpoints se acceden a través del Gateway en `http://localhost:8081`.  
La documentación interactiva Swagger de cada servicio está en `http://localhost:<puerto>/swagger-ui.html`.

### Autenticación y Usuarios — `microservicio-usuario :8085`

| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| `POST` | `/api/login/` | ❌ | Iniciar sesión y obtener token JWT |
| `GET` | `/api/v1/token/` | ✅ | Validar token entre microservicios |
| `POST` | `/api/v1/usuarioApp/create` | ❌ | Registrar usuario desde la app móvil |
| `POST` | `/api/v1/usuario/create` | 🔒 Admin | Crear usuario (admin/super-admin) |
| `GET` | `/api/v1/usuario/all` | 🔒 Admin | Listar todos los usuarios |
| `PATCH` | `/api/v1/usuario/obtenerUsuario/{id}` | 🔒 Admin | Obtener usuario por ID |
| `PUT` | `/api/v1/usuario/update/{id}` | 🔒 Admin | Actualizar usuario |
| `DELETE` | `/api/v1/usuario/delete/{id}` | 🔒 Admin | Eliminar usuario |

### Participantes — `microservicio-participante :8086`

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/v1/participante/create` | Crear participante/paciente |
| `GET` | `/api/v1/participante/all` | Listar todos los participantes |
| `GET` | `/api/v1/participante/getParticipante/{id}` | Obtener por ID |
| `GET` | `/api/v1/participante/obtenerParticipanteXCi/{ci}` | Buscar por cédula de identidad |
| `GET` | `/api/v1/participante/obtenerParticipanteXUsuario/{id}` | Participantes vinculados a un usuario |
| `GET` | `/api/v1/participante/obtenerDatoXParticipante/{id}` | Sesiones de datos de un participante |
| `GET` | `/api/v1/participante/getIdbyParticipantebyCi/{ci}` | Obtener ID por cédula |
| `PUT` | `/api/v1/participante/update/{id}` | Actualizar participante |
| `DELETE` | `/api/v1/participante/delete/{id}` | Eliminar participante |

### Datos del Sensor — `microservicio-dato :8087`

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/v1/datoparticipante/create` | Crear sesión de captura |
| `GET` | `/api/v1/datoparticipante/allbyParticipante/{id}` | Sesiones de un participante |
| `PATCH` | `/api/v1/datoparticipante/obtenerDato/{id}` | Obtener sesión por ID |
| `GET` | `/api/v1/datoparticipante/findbyid/{id}` | Verificar existencia |
| `GET` | `/api/v1/datoparticipante/obtenerIdbyparticipanteAndFecha/idParticipante={id}&fecha={fecha}` | Buscar por participante y fecha |
| `PUT` | `/api/v1/datoparticipante/update/{id}` | Actualizar sesión |
| `DELETE` | `/api/v1/datoparticipante/delete/{id}` | Eliminar sesión |

### Patologías — `microservicio-patologia :8088`

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/v1/patologia/create` | Agregar patología al catálogo |
| `POST` | `/api/v1/patologia/createrelacion` | Asociar sesión ↔ patología |
| `GET` | `/api/v1/patologia/all` | Listar todas las patologías |
| `GET` | `/api/v1/patologia/obtenerPatologiaXDato/{id_dato}` | Patologías de una sesión |
| `GET` | `/api/v1/patologia/obtenerIdbyNombre/{nombre}` | Obtener ID por nombre |
| `PUT` | `/api/v1/patologia/update/{idpatologia}` | Actualizar patología |
| `DELETE` | `/api/v1/patologia/delete/{idpatologia}` | Eliminar patología |
| `DELETE` | `/api/v1/patologia/eliminarrelacion/iddato={iddato}&fecha={fecha}` | Desasociar patología de sesión |

### Señales IMU — `microservicio-sennal :8089`

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/v1/sennal/create` | Insertar muestra de señal IMU |
| `GET` | `/api/v1/sennal/findAll/{iddato}` | Obtener todas las señales de una sesión |
| `DELETE` | `/api/v1/sennal/delete/{idSennal}` | Eliminar señal |

### Red Neuronal — `microservicio-redneuronal :8092`

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/api/v1/modelo/crear-inicial` | Crear y entrenar modelo inicial |
| `POST` | `/api/v1/modelo/entrenar` | Reentrenar con nuevos datos |
| `POST` | `/api/v1/modelo/predecir` | Inferencia: predice patología de marcha |

---

## 💡 Uso y Ejemplos

Todas las peticiones van a través del Gateway en `http://localhost:8081`. La mayoría requieren `Authorization: Bearer <token>`.

### 1. Autenticarse y obtener token

```bash
curl -X POST http://localhost:8081/api/login/ \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "tu_password"}'
```

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 2. Crear un participante

```bash
curl -X POST http://localhost:8081/api/v1/participante/create \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez",
    "ci": "1234567890",
    "sexo": "M",
    "telefono": "0991234567"
  }'
```

### 3. Registrar una sesión de captura

```bash
curl -X POST http://localhost:8081/api/v1/datoparticipante/create \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"idParticipante": 1, "fecha": "2025-06-19", "hora": "10:30:00", "edad": 72}'
```

### 4. Enviar señales IMU

```bash
curl -X POST http://localhost:8081/api/v1/sennal/create \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "idDato": 1,
    "acelerometroX": 0.12,
    "acelerometroY": -0.05,
    "acelerometroZ": 9.81,
    "giroscopioX": 0.003,
    "giroscopioY": 0.001,
    "giroscopioZ": -0.002,
    "magnetometroX": 25.4,
    "magnetometroY": -10.2,
    "magnetometroZ": 40.1,
    "rango": "4g",
    "frecuencia": 50
  }'
```

### 5. Obtener predicción de patología

```bash
curl -X POST http://localhost:8081/api/v1/modelo/predecir \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"idDato": 1}'
```

```json
{
  "patologia": "marcha_parkinsoniana",
  "confianza": 0.87
}
```

---

## 💻 Frontend — Interfaz Visual

Aplicación web en React 19 ubicada en `interfaz_visual/`:

| Tecnología | Versión |
|-----------|---------|
| React | 19 |
| Vite | Última |
| Tailwind CSS | 4 |
| Bootstrap | 5 |

```bash
cd interfaz_visual

npm run dev      # Servidor de desarrollo → http://localhost:3000
npm run build    # Build de producción
npm run preview  # Vista previa del build
npm run lint     # Linting del código
```

---

## 📁 Estructura del Proyecto

```
API-RedNeuronal-master/
│
├── 📄 pom.xml                          # POM raíz — agrega todos los módulos
├── 📄 sensor_inteligente.sql           # Esquema PostgreSQL completo
├── 📄 documentacion.md                 # Requisitos funcionales del sistema
├── 📄 CLAUDE.md                        # Guía para Claude Code
│
├── 📁 .github/workflows/
│   └── github-actions-demo.yml         # Pipeline CI/CD
│
├── 📁 diagramas/
│   ├── Arquitectura app móvil.png
│   └── Diagrama de arquitectura Inicial.png
│
├── 🔍 microservicio-eureka/            # Service Discovery :8761
├── ⚙️  microservicio-config/            # Config Server
├── 🚪 microservicio-gateway/           # API Gateway :8081
│
├── 📦 microservicio-gestion/           # Agregador de servicios de negocio
│   │
│   ├── microservicio-usuario/          # Auth + JWT :8085
│   │   └── src/main/java/com/example/usuario/
│   │       ├── config/                 # SecurityConfigurations, TokenService, Swagger
│   │       ├── core/
│   │       │   ├── controller/         # LoginController, UsuarioController
│   │       │   ├── dto/                # UsuarioDto, UsuarioLogin
│   │       │   ├── entities/           # Usuario (JPA)
│   │       │   ├── repository/         # UsuarioRepository
│   │       │   ├── service/            # UsuarioService (interfaz)
│   │       │   ├── serviceimpl/        # UsuarioServiceImpl
│   │       │   └── exception/
│   │       └── aspect/                 # ControllerAspecto (AOP)
│   │
│   ├── microservicio-participante/     # Pacientes :8086
│   ├── microservicio-dato/             # Sesiones IMU :8087
│   ├── microservicio-patologia/        # Catálogo :8088
│   ├── microservicio-sennal/           # Señales + Orquestador :8089
│   │
│   └── microservicio-redneuronal/      # Red Neuronal :8092 (WAR)
│       ├── libs/neuroph-2.98/          # JARs de Neuroph (scope: system)
│       └── src/main/java/com/example/redneuronal/
│           └── core/
│               ├── controller/         # ModeloController
│               ├── model/util/
│               │   ├── SennalPreccesor.java       # Normalización/filtrado
│               │   ├── ObtenerCaracteristicas.java # Extracción de features
│               │   └── Prediccion.java            # Interpretación de salida
│               ├── service/            # ModeloService, ModeloCRUDService
│               └── repository/         # RedNeuronalRepository
│
└── 🖥️  interfaz_visual/                # Frontend React :3000
    ├── package.json
    ├── vite.config.js
    └── src/
```

### Convención de Paquetes (Servicios de Negocio)

Todos los servicios bajo `microservicio-gestion/` siguen la misma estructura interna:

```
com.example.<servicio>/
├── config/                ← Spring beans: CORS, Security, Swagger, TokenService
├── core/
│   ├── controller/        ← Endpoints REST (@RestController)
│   ├── dto/               ← Objetos de transferencia (sufijo: Dto)
│   ├── entities/          ← Entidades JPA (sin sufijo)
│   ├── repository/        ← Spring Data JPA (sufijo: Repository)
│   ├── service/           ← Interfaces de negocio (sufijo: Service)
│   ├── serviceimpl/       ← Implementaciones (sufijo: ServiceImpl)
│   ├── feingclient/       ← Clientes OpenFeign entre servicios
│   ├── exception/         ← Excepciones personalizadas
│   └── controllerException/ ← @ControllerAdvice handlers
└── aspect/                ← AOP: logging que persiste en tabla 'logs'
```

---

## 🔄 CI/CD

### Pipeline GitHub Actions

El pipeline se ejecuta automáticamente en cada push a `master`:

```yaml
# .github/workflows/github-actions-demo.yml
on:
  push:
    branches: [ master ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
      - name: Build with Maven
        run: mvn -B clean install -DskipTests
```

> ⚠️ Los tests se omiten en CI. Ejecuta `mvn test` localmente antes de hacer push.

---

## 🤝 Contribución

1. Haz un fork del repositorio
2. Crea tu rama: `git checkout -b feature/mi-funcionalidad`
3. Asegúrate de que los tests pasen: `mvn test`
4. Sube tu rama: `git push origin feature/mi-funcionalidad`
5. Abre un **Pull Request** describiendo los cambios

---

## 📄 Licencia

Distribuido bajo la licencia **MIT**. Consulta el archivo `LICENSE` para más detalles.

---

## 📬 Contacto

**Rayner Alejandro Soto Martínez**  
📧 [raynersoto01@gmail.com](mailto:raynersoto01@gmail.com)  
🔗 [github.com/raynersoto/API-RedNeuronal-master](https://github.com/raynersoto/API-RedNeuronal-master)

---

<div align="center">

**Desarrollado con ❤️ para la detección temprana de patologías en adultos mayores**

</div>