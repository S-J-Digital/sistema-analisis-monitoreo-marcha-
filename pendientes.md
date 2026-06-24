# PENDIENTES — Sistema API Red Neuronal (Análisis de Marcha)

> **Archivo LOCAL — NO sube a GitHub** (registrado en `.gitignore`)
>
> ### REGLA DE ORO
> - **Cada cambio o modificación** realizado en el proyecto debe quedar registrado en la sección "Historial de Cambios" con fecha, descripción y microservicio afectado.
> - **Al iniciar sesión:** leer este archivo primero, comparar el estado real del código con lo que aquí se describe, tachar/actualizar lo resuelto, y continuar desde "Dónde nos quedamos".
> - Tanto Claude como el desarrollador pueden escribir en la sección "Espacio de Debate" para opinar sobre propuestas.

---

## ESTADO DE SESIÓN

**Última sesión:** 2026-06-22
**Dónde nos quedamos:** Config Server activado y configurado. Todas las configuraciones de todos los microservicios migradas al Config Server (carpeta `configs/` con perfiles dev/prod/test por servicio). `show-sql` corregido a `false` en todos los perfiles prod. Descubiertos 2 bugs nuevos en el gateway (`FiltroGlobal` y `TokenConnection`). Pendientes.md actualizado.
**Próximo paso al retomar:** Empezar por las 9 tareas 🔴 CRÍTICAS. Arrancar por #1 (clave JWT) y #7 (endpoint faltante) porque bloquean el sistema. El Config Server ya está listo — no es prioridad.

---

## HISTORIAL DE CAMBIOS

| Fecha | Cambio | Microservicio | Quién |
|-------|--------|--------------|-------|
| 2026-06-22 | Creación de `pendientes.md` + añadido a `.gitignore` | Global | Claude |
| 2026-06-22 | Análisis profundo archivo-por-archivo: endpoints, entidades, DTOs, repos, feign clients extraídos. Puntuaciones actualizadas. | Global | Claude |
| 2026-06-22 | Verificación de inventario completo. Descubierto frontend `interfaz_visual` (React+Vite) antes marcado como vacío por error. Añadidas secciones 12 (frontend) y 13 (diagramas). | Global | Claude |
| 2026-06-22 | Activado Config Server: añadido `@EnableConfigServer`, configurado puerto 8888, modo native, carpeta `configs/`. | microservicio-config | Claude |
| 2026-06-22 | Migradas configuraciones de todos los microservicios al Config Server (configs/ con perfiles dev/prod/test). | microservicio-config | Rayner+Claude |
| 2026-06-22 | Corregido `show-sql: false` en todos los perfiles prod (configs/ y application-prod.yml de usuario). | Global | Claude |
| 2026-06-22 | Descubiertos 2 bugs nuevos en gateway: `FiltroGlobal` retorna null y nunca valida token; `TokenConnection` tiene URL malformada con token en path. Ver sección 2. | microservicio-gateway | Claude |

> Añadir una fila aquí cada vez que se modifique algo en el proyecto.

---

# ANÁLISIS EXHAUSTIVO — MICROSERVICIO POR MICROSERVICIO

> Realizado el 2026-06-22 analizando cada archivo Java, yml y SQL del proyecto.
> Escala: 1–10. Los hallazgos incluyen archivo y línea exacta.

---

## RESUMEN DE PUNTUACIONES (actualizado tras análisis profundo)

| Microservicio / Área | Antes | **Ahora** | Cambio | Estado |
|----------------------|:----:|:----:|:----:|--------|
| microservicio-eureka | 7 | **7/10** | = | Funcional, sin autenticación |
| microservicio-gateway | 5 | **5/10** | = | Ruta `/api/**` rompe el enrutado |
| microservicio-config | 6 | **7/10** | ⬆ | **Activado y configurado. Configs de todos los servicios migradas.** |
| microservicio-usuario | 4 | **3/10** | ⬇ | Más bugs críticos: `validarToken` roto, endpoint Feign inexistente, AES en vez de BCrypt |
| microservicio-participante | 5 | **4/10** | ⬇ | `@Id` mal en relación, Feign a endpoint inexistente |
| microservicio-dato | 5 | **4/10** | ⬇ | Feign `SennalClient` con tipo de retorno incompatible |
| microservicio-patologia | 6 | **6/10** | = | El más limpio del grupo |
| microservicio-sennal | 4 | **4/10** | = | Flujo principal comentado |
| microservicio-redneuronal | 5 | **5/10** | = | Buen perfil prod, pero JARs system-scope y versión Spring Cloud distinta |
| microservicio-log | 1 | **1/10** | = | Módulo fantasma |
| **interfaz_visual (frontend)** | — | **2/10** | 🆕 | **Frontend React+Vite recién iniciado (solo "Hola Rayner")** |
| Base de datos (SQL) | 6 | **6/10** | = | Buen schema, claves primarias mal en tablas relacionales |
| CI/CD (GitHub Actions) | 3 | **3/10** | = | Solo compila, tests saltados |
| **PROMEDIO GLOBAL (backend)** | 4.7 | **4.2/10** | ⬇ | El análisis profundo reveló más fallos |

> **CORRECCIÓN respecto al análisis anterior:** En la primera pasada dije que `interfaz_visual/` estaba vacía. **Era un error** — sí contiene un proyecto frontend React + Vite, aunque apenas iniciado. Ver sección 12.

> La nota bajó porque el análisis archivo-por-archivo descubrió bugs que no eran visibles en la primera pasada: el Config Server no funciona, hay llamadas Feign a endpoints que no existen, claves primarias mal puestas en tablas relacionales, e incompatibilidades de tipos entre servicios.

---

---

## 1. microservicio-eureka — 7/10

**Puerto:** 8761
**Archivo clave:** `src/main/resources/application.yml`

**Lo que funciona:**
- Configuración estándar y correcta de Netflix Eureka.
- `register-with-eureka: false` y `fetch-registry: false` correctos para el servidor.
- Se integra con Config Server vía `optional:configserver`.

**Problemas:**
- No tiene ningún mecanismo de autenticación. Cualquiera que llegue al puerto 8761 puede ver todos los servicios registrados, sus IPs y puertos.
- No hay health check configurado más allá del defecto de Eureka.
- En el gateway, la URL de Eureka usa una propiedad `eureka.server.port` que no está en el `application.yml` del gateway (está puesta a mano `8761`), creando dependencia implícita frágil.

**Pendientes:**
- [ ] Añadir autenticación básica al dashboard de Eureka (Spring Security en el módulo Eureka).
- [ ] Limpiar la referencia cruzada de propiedades en el gateway.

---

## 2. microservicio-gateway — 5/10

**Puerto:** 8081
**Archivo clave:** `src/main/resources/application.yml`

**Lo que funciona:**
- Discovery locator habilitado con `lower-case-service-id: true`.
- Rutas definidas para todos los microservicios.
- Actuator expuesto para gestión del gateway.

**Problemas encontrados:**

**Problema G1 — Rutas con IPs hardcodeadas:**
```yaml
uri: http://localhost:8085   # microservicio-usuario
uri: http://localhost:8086   # microservicio-participante
# etc.
```
En ningún servicio se usa Eureka para resolver la URI. Las URIs deberían ser `lb://microservicio-usuario` para usar el load balancer de Spring Cloud. Si algún servicio cambia de puerto, hay que actualizar el gateway manualmente.

**Problema G2 — Ruta de usuario captura TODO:**
```yaml
- id: microservicio-usuario
  predicates:
    - Path=/api/**
```
Esta ruta atrapa CUALQUIER path que empiece por `/api/`, incluyendo las rutas de participante, dato, etc. Las rutas más específicas de abajo nunca se alcanzan porque la del usuario las absorbe primero. El orden importa en Spring Gateway.

**Problema G3 — Sin filtros de seguridad en el gateway:**
No hay ningún filtro JWT en el gateway. Cada microservicio debería validar tokens, pero el gateway no añade ninguna capa de control centralizado.

**Problema G4 — `management.endpoints.web.exposure.include: '*'`:**
Todos los actuator endpoints están expuestos públicamente, incluyendo `/actuator/env`, `/actuator/beans`, etc., que revelan configuración interna.

**BUG CRÍTICO G5 — `FiltroGlobal` retorna null (FiltroGlobal.java línea 23):**
```java
if (token != null && token.startsWith("Bearer ")) {
    // bloque vacío — no hace nada con el token
}
return null;  // ← NullPointerException garantizado
```
El filtro extrae el token del header `Authorization` pero no hace nada con él. Además retorna `null` en lugar de `chain.filter(exchange)`, lo que provoca un `NullPointerException` en cada petición. Además `FiltroGlobal` no tiene `@Component` — nunca se registra en el contexto de Spring.

**BUG CRÍTICO G6 — `TokenConnection` con URL malformada (TokenConnection.java línea 17):**
```java
URI.create("http://localhost:8085/api/v1/token/{" + token + "}")
```
El token se concatena dentro de `{}` en el path, lo que produce URLs inválidas como `http://localhost:8085/api/v1/token/{eyJhbGci...}`. Además el endpoint real es `GET /api/v1/token/` (sin parámetro en path). Esta clase nunca es llamada desde `FiltroGlobal` (el bloque `if` está vacío), por lo que es código muerto.

**Pendientes:**
- [ ] Cambiar URIs hardcodeadas a `lb://nombre-servicio`.
- [ ] Corregir el orden y especificidad de rutas (usuario debe ser `Path=/api/login/**, /api/v1/usuario/**, /api/v1/token/**`).
- [ ] Restringir actuator: solo exponer `health` e `info` públicamente.
- [ ] **Corregir `FiltroGlobal`**: añadir `@Component`, implementar validación real del token llamando a `TokenConnection`, y retornar `chain.filter(exchange)`.
- [ ] **Corregir `TokenConnection`**: URL debe ser `http://localhost:8085/api/v1/token/` con el token en el header `Authorization`, no en el path.

---

## 3. microservicio-config — 7/10 ✅ CONFIGURADO

**Rol:** Configuración centralizada vía Spring Cloud Config Server.
**Puerto:** 8888
**Modo:** Native (carpeta `src/main/resources/configs/`)

**Estado actual:** Completamente funcional. Activado el 2026-06-22.
- `@EnableConfigServer` añadido a `MicroservicioConfigApplication`.
- `application.yml` configurado con puerto 8888, perfil `native`, ruta `classpath:/configs`.
- Carpeta `configs/` poblada con configuraciones de todos los microservicios (dev/prod/test).

**Archivos en configs/:**
`microservicio-eureka.yml`, `microservicio-gateway.yml` (+dev/prod), `microservicio-usuario.yml` (+dev/prod/test), `microservicio-participante.yml` (+dev/prod/test), `microservicio-dato.yml` (+dev/prod/test), `microservicio-patologia.yml` (+dev/prod/test), `microservicio-sennal.yml` (+dev/prod/test), `microservicio-redneuronal.yml` (+dev/prod/test).

**Verificación:** Arrancar Config Server y abrir `http://localhost:8888/microservicio-usuario/dev` — debe devolver JSON con la configuración.

**Pendientes restantes:**
- [ ] Usar cifrado del Config Server para secretos (JWT secret, clave AES, contraseñas BD).

---

## 4. microservicio-usuario — 3/10

**Puerto:** 8085 · **Paquete base:** `com.example.usuario` · **Packaging:** jar
**Responsabilidad:** Autenticación JWT, gestión de usuarios (CRUD), roles.

### Endpoints

| Método | Ruta | Auth (@PreAuthorize) | Descripción |
|--------|------|----------------------|-------------|
| POST | `/api/login/` | ❌ pública | Login, devuelve JWT |
| GET | `/api/v1/token/` | Admin, SuperAdmin, Médico, Paciente, Investigador | Valida token (para otros microservicios) |
| POST | `/api/v1/usuario/create` | Admin, SuperAdmin | Crear usuario |
| GET | `/api/v1/usuario/all` | Admin, SuperAdmin | Listar usuarios |
| PUT | `/api/v1/usuario/update/{id}` | Admin, SuperAdmin | Modificar usuario |
| DELETE | `/api/v1/usuario/delete/{id}` | Admin, SuperAdmin | Eliminar usuario |
| PATCH | `/api/v1/usuario/obtenerUsuario/{id}` | Admin, SuperAdmin | Obtener por ID (⚠ debería ser GET) |
| POST | `/api/v1/usuarioApp/create` | ❌ pública | Crear usuario Paciente desde la app (⚠ sin seguridad) |

> ⚠ **No existe el endpoint `/existeUsuario/{id}`** que el `UsuarioClient` de participante intenta llamar (ver bug abajo).

### Entidades (JPA)

| Entidad | Tabla | Campos |
|---------|-------|--------|
| `Usuario` (implements UserDetails) | `usuario` | id (seq `usuario_id_seq`), rol (ManyToOne `rol_id`), nombre, username (unique), contrasenna, noprofesional (unique). Validación `@Size(5,255)` en varios campos. |
| `Rol` | `rol` | id, nombreRol (col `nombre`, unique), usuarios (OneToMany EAGER) |
| `Estado` | `estados` | estado_id, estado (col `nombre_estado`), descripcion. Bloquea insert/update/remove con `UnsupportedOperationException`. |
| `Logs` | `log` | id, event_date, log_level, user_name, ip_address, message. Bloquea update/remove. |

### DTOs

- `UsuarioDto` (id, id_rol, nombre, username, contrasenna, noprofesional) — entrada
- `UsuarioDtoSent` (record: id, rol, nombre, username, noprofesional) — **salida sin contraseña, correcto**
- `UsuarioLogin` (record: username, password)
- `TokenDto`, `LogDto`, `ParticipantesDto`, `UserName`, `RespuestaEstandar<T>` (genérico)

### Repositorios

- `UsuarioRepository`: findIdByNombre (JPQL), findByNombre, existsByNombre, findByUsernameEqualsIgnoreCase, findByUsernameEquals, existsByUsernameEquals
- `RolRepository`: findById, existsByNombreRolEquals, findByNombreRolEquals
- `EstadoRepository`, `LogsRepository`

### Feign Clients

- `ParticipanteClient` → `localhost:8086/api/v1/participante`, método `obtenerParticipanteXUsuario(id)`. **Import muerto:** `com.zaxxer.hikari.util.UtilityElf`.

### Config

- `SecurityConfigurations`: `anyRequest().permitAll()`, BCryptPasswordEncoder bean, filtros CORS + SecurityFilter
- `CorsConfiguration`: `allowedOrigins("localhost")` (sin `http://`, probablemente no matchea nada), allowCredentials false
- `SimpleCorsFilter`: segundo filtro CORS (conflicto potencial con el anterior)
- `ListadosCarga`: carga roles/estados como `@Bean List<>` al arrancar (no se refresca en runtime)
- `ControllerAspecto`: AOP que solo hace `logger.info`, **no persiste logs**
- `application-dev.yml`: pass DB default `0000`, show-sql true, ddl-auto update, rabbitmq configurado

### Seguridad — CRÍTICO

**BUG CRÍTICO 1 — Clave JWT no se resuelve (TokenService.java líneas 20 y 52):**
```java
Algorithm algorithm = Algorithm.HMAC256("${jwt.secreto}");
```
La cadena `"${jwt.secreto}"` es un String literal, no una variable. Spring no interpola valores dentro de parámetros de métodos Java. La clave JWT real es literalmente `${jwt.secreto}`. Cualquiera que lo sepa puede firmar tokens válidos. Solución: inyectar con `@Value("${jwt.secreto}") private String jwtSecreto;` y usar esa variable.

**BUG CRÍTICO 2 — Contraseña en el payload JWT (TokenService.java línea 26):**
```java
.withClaim("password", usuario.getPassword())
```
El hash BCrypt de la contraseña viaja en cada token. Los tokens JWT son decodificables sin la clave. Eliminar esta línea.

**BUG CRÍTICO 3 — SecurityFilterChain abierto (SecurityConfigurations.java línea 48):**
```java
.authorizeHttpRequests(reques -> reques.anyRequest().permitAll())
```
Toda la protección recae en `@PreAuthorize` en los controllers. Cualquier endpoint sin esa anotación es completamente público.

**BUG CRÍTICO 4 — Lógica del SecurityFilter invertida (SecurityFilter.java líneas 61–83):**
```java
if(!swagger) {
    logger.info(request.getRequestURI()); // No hace nada
}
else if (!token.getToken().isBlank() && ...) { // Solo valida si ES swagger
```
La condición está al revés. El filtro solo valida el token cuando la petición ES a Swagger, y no hace nada cuando es a `/api/v1/usuario`. Es decir, el filtro de seguridad está completamente inoperativo para las rutas reales.

**BUG CRÍTICO 5 — Clave AES hardcodeada (DescrifradoBase64Android.java línea 20):**
```java
private static final String key = "08wR?!S6_wo&-v$f#0RUdrEfRoclTh";
```
La clave de descifrado AES está en código fuente. Cualquiera con acceso al repo puede descifrar todas las contraseñas.

**BUG CRÍTICO 6 — Sistema de autenticación no usa BCrypt (LoginControllerServiceImpl.java línea 37):**
```java
if (!DescrifradoBase64Android.getdesencriptacion(user.getPassword()).equals(usuarioLogin.password()))
```
La contraseña se descifra con AES para comparar. Esto significa que las contraseñas no están hasheadas con BCrypt (aunque `BCryptPasswordEncoder` está declarado como bean), sino cifradas con AES reversible. AES reversible NO es almacenamiento seguro de contraseñas.

**BUG CRÍTICO 7 — `validarToken` está roto (TokenControllerServiceImpl.java líneas 44, 55):**
```java
Algorithm algorithm = Algorithm.HMAC256("${jwt.secreto}");  // mismo bug de clave literal
...
return (passwordEncoder.matches(decodedJWT.getClaim("password").asString(), usuario.getPassword()) ...);
```
Compara con `passwordEncoder.matches()` (BCrypt) el claim `password` del token contra la contraseña en BD, que está cifrada con AES (no BCrypt). Nunca dará true. El endpoint `/api/v1/token/` que los demás microservicios usan para validar tokens **siempre devuelve false**. La validación de tokens entre servicios está inoperativa.

**BUG CRÍTICO 8 — El endpoint `/existeUsuario/{id}` no existe:**
`UsuarioClient` de participante llama a `GET /api/v1/usuario/existeUsuario/{id}`, pero `UsuarioController` NO expone ese endpoint (el método `existeUsuariobynombre` existe en el ServiceImpl pero ningún controller lo publica). **Crear un participante falla con 404** al validar el usuario.

### Calidad de código

**BUG menor — `@PatchMapping` para obtener un usuario (UsuarioController.java línea 72):**
```java
@PatchMapping("/obtenerUsuario/{id}")
```
`PATCH` es para actualizaciones parciales. Debe ser `@GetMapping`.

**Problema — Doble capa de servicios innecesaria:**
Existe `UsuarioService` / `UsuarioServiceImpl` Y además `UsuarioControllerService` / `UsuarioControllerServiceImpl`. Los controllers delegan al `ControllerService`, que delega al `Service`. Es una capa extra sin justificación que duplica código.

**Problema — `ControllerAspecto` no persiste los logs (ControllerAspecto.java):**
El aspecto solo hace `logger.info(...)` con `java.util.logging`. No llama a `LogsService.insertarLog()`. Los logs de auditoría AOP no se persisten en BD desde este aspecto.

**Problema — `ControllerException` siempre devuelve 400:**
```java
return ResponseEntity.badRequest().body(...);
```
Todos los errores, incluyendo errores internos del servidor, devuelven HTTP 400. Un error de BD debería ser 500.

**Problema — `UsuarioAppController` sin seguridad:**
El endpoint `POST /api/v1/usuarioApp/create` no tiene ninguna restricción. Cualquiera puede crear usuarios desde el exterior sin autenticación.

**Problema — `ListadosCarga` carga roles y estados al arrancar pero no los cachea correctamente:**
Devuelve `List<>` como `@Bean`, lo que significa que si la BD cambia en tiempo de ejecución, los beans no se actualizan.

**Pendientes:**
- [ ] **URGENTE** Corregir inyección de `jwt.secreto` con `@Value`.
- [ ] **URGENTE** Eliminar `password` del payload JWT.
- [ ] **URGENTE** Corregir la lógica invertida del `SecurityFilter`.
- [ ] **URGENTE** Mover la clave AES a variable de entorno.
- [ ] **URGENTE** Cambiar sistema de autenticación a BCrypt puro.
- [ ] **URGENTE** Corregir `validarToken` (mismo secreto + comparación coherente con el almacenamiento).
- [ ] **URGENTE** Crear el endpoint `GET /api/v1/usuario/existeUsuario/{id}` (lo consume participante).
- [ ] Corregir `@PatchMapping` → `@GetMapping` en `obtenerUsuario`.
- [ ] Eliminar la capa `serviceController` innecesaria.
- [ ] Añadir `@PreAuthorize` a `UsuarioAppController`.
- [ ] Corregir `ControllerException` para devolver 500 en errores de servidor.
- [ ] Corregir `CorsConfiguration` (`allowedOrigins("localhost")` sin esquema no funciona).
- [ ] Eliminar import muerto `UtilityElf` en `ParticipanteClient`.
- [ ] Hacer que `ControllerAspecto` persista logs (hoy solo loguea a consola).

---

## 5. microservicio-participante — 4/10

**Puerto:** 8086 · **Paquete base:** `com.example.participante` · **Packaging:** jar
**Responsabilidad:** CRUD de pacientes, relación usuario-participante.

### Endpoints

| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| POST | `/api/v1/participante/create` | ❌ | Crear participante |
| PUT | `/api/v1/participante/update/{id}` | ❌ | Modificar participante |
| GET | `/api/v1/participante/all` | ❌ | Listar todos |
| GET | `/api/v1/participante/getParticipante/{id}` | ❌ | Obtener por ID |
| GET | `/api/v1/participante/obtenerParticipanteXCi/{ci}` | ❌ | ⚠ usa `@RequestBody` en un GET |
| GET | `/api/v1/participante/obtenerParticipanteXUsuario/{id}` | ❌ | Participantes de un usuario |
| GET | `/api/v1/participante/obtenerDatoXParticipante/{idparticipante}` | ❌ | Datos del participante (Feign a dato) |
| DELETE | `/api/v1/participante/delete/{id}` | ❌ | Eliminar |
| GET | `/api/v1/participante/getIdbyParticipantebyCi/{ci}` | ❌ | ID por CI |

### Entidades (JPA)

| Entidad | Tabla | Campos / PK |
|---------|-------|-------------|
| `Participante` | `participante` | id (IDENTITY), nombre, ci, sexo, telefono |
| `UsuarioParticipante` | `usuario_participante` | **@Id solo en `id_usuario`**, id_participante (⚠ ver bug) |
| `Logs` | `log` | (igual que en usuario) |

### DTOs

`ParticipanteDto`, `ParticipanteAllDto`, `ParticipanteUpdateDto`, `UsuarioParticipanteDto`, `DatoDto`, `PatologiaDto`, `SennalDto`, `LogDto`

### Repositorios

- `ParticipanteRepository`: findByCi, getIdByCi (JPQL), existsByCi
- `UsuarioParticipanteRepository`: findAllByIdUser (JPQL con JOIN Participante-UsuarioParticipante)
- `LogsRepository`

### Feign Clients

- `UsuarioClient` → `localhost:8085/api/v1/usuario`, método `existeUsuario(id)` → **llama a endpoint inexistente (404)**
- `DatoClient` → `localhost:8087/api/v1/datoparticipante`, método `obtenerDatoXParticipante(id)`

### BUG — Clave primaria mal en `UsuarioParticipante`:
`@Id` está solo en `id_usuario`. Esto significa que **un usuario solo puede estar relacionado con UN participante** (la PK es única por usuario). Para un médico con muchos pacientes esto rompe el modelo. Debe ser clave compuesta `(id_usuario, id_participante)` con `@IdClass` o `@EmbeddedId`. Además, el constructor desde DTO es `private` (inutilizable).

**Entidad `Participante` incompleta:** Solo `nombre`, `ci`, `sexo`, `telefono`. Falta: fecha de nacimiento, dirección, estado activo/inactivo.

**BUG — `datoDtoList.get(0)` sin propósito (ParticipanteServiceImpl.java línea 99):**
```java
datoDtoList.get(0); // Esta línea no asigna nada y lanza IndexOutOfBoundsException si la lista está vacía
```
Crash garantizado si el participante no tiene datos aún.

**BUG — `@GetMapping` con `@RequestBody` (ParticipanteController.java línea 88):**
```java
@GetMapping("/obtenerParticipanteXCi/{ci}")
public ResponseEntity<?> obtenerParticipanteXCI(@RequestBody String ci)
```
Las peticiones GET no tienen body. Esta combinación falla en la mayoría de clientes HTTP. El CI debe venir como `@PathVariable`.

**Problema — Sin seguridad:**
Ningún endpoint de participante tiene `@PreAuthorize`. Cualquiera puede listar, crear, modificar o eliminar pacientes sin autenticarse.

**Problema — `aceptado` y `error` como campos de instancia mutables:**
```java
private String aceptado = "Aceptado";
private String error = "Error";
```
Deben ser `private static final String`.

**Problema — Inserción silenciosa:**
`insertarParticipante` no lanza excepción si el participante ya existe (CI duplicado). Simplemente no hace nada y devuelve 200, lo que confunde al cliente.

**Pendientes:**
- [ ] Eliminar `datoDtoList.get(0)` en `ParticipanteServiceImpl`.
- [ ] Corregir `@RequestBody` → `@PathVariable` en `obtenerParticipanteXCI`.
- [ ] Añadir `@PreAuthorize` a todos los endpoints.
- [ ] Añadir excepción cuando el participante ya existe al insertar.
- [ ] Ampliar entidad `Participante` (fecha de nacimiento, estado).
- [ ] **Corregir PK de `UsuarioParticipante`** a clave compuesta (hoy un usuario solo puede tener 1 participante).
- [ ] Hacer público el constructor `UsuarioParticipante(UsuarioParticipanteDto)`.

---

## 6. microservicio-dato — 4/10

**Puerto:** 8087 · **Paquete base:** `com.example.microserviciodato` · **Packaging:** jar
**Responsabilidad:** Registro de cada sesión de medición (fecha, hora, medidas físicas, flag de patología). Orquesta patología + señal vía Feign.

### Endpoints

| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| POST | `/api/v1/datoparticipante/create` | ❌ | Crear dato |
| PUT | `/api/v1/datoparticipante/update/{id}` | ❌ | Modificar |
| DELETE | `/api/v1/datoparticipante/delete/{id}` | ❌ | Eliminar |
| GET | `/api/v1/datoparticipante/allbyParticipante/{idparticipante}` | ❌ | Dato + patologías + señales (2 llamadas Feign) |
| PATCH | `/api/v1/datoparticipante/obtenerDato/{id}` | ❌ | ⚠ debería ser GET |
| GET | `/api/v1/datoparticipante/findbyid/{id}` | ❌ | ¿Existe el dato? (bool) |
| GET | `/api/v1/datoparticipante/obtenerIdbyparticipanteAndFecha/idParticipante={..}&fecha={..}` | ❌ | ⚠ URL no estándar |

### Entidades (JPA)

| Entidad | Tabla | Campos |
|---------|-------|--------|
| `DatoParticipante` | `datoparticipante` | id (IDENTITY), idparticipante, fecha (LocalDate), hora (LocalTime), edad (int), patologia (boolean), numerodecalzado, medicioncinturatobillo, largopierna, alturadelsensor |
| `Logs` | `log` | (igual) |

### DTOs

- `DatoParticipanteDto`, `DatoIdDto`, `PatologiaDto`, `SennalDto`, `LogDto`
- `PatologiaResponse` (response builder): id, idparticipante, fecha, hora, edad, patologia, `List<PatologiaDto> patologiaList`, medidas físicas, `SennalDto sennalList`

### Repositorios

- `DatoParticipanteRepository`: existsDatoParticipanteByFechaAndIdparticipante, findAllByIdparticipante, getIdByIdparticipanteAndFecha (JPQL)
- `LogsRepository`

### Feign Clients

- `PatologiaClient` → `localhost:8088/api/v1/patologia`, `obtenerPatologiaXDato(id_dato)` → `List<PatologiaDto>`
- `SennalClient` → `localhost:8089/api/v1/sennal`, `obtenerSennalXDato(iddato)` → **declara `SennalDto` pero el endpoint devuelve `ListaSensoresDto`** (ver bug)

### BUG — Incompatibilidad de tipos en `SennalClient`:
`SennalClient.obtenerSennalXDato` declara retorno `SennalDto` (un objeto plano), pero `SennalController./findAll/{iddato}` devuelve `ListaSensoresDto` (con listas de Double por eje). La deserialización producirá un objeto con casi todos los campos nulos. El campo `sennalList` en `PatologiaResponse` será incorrecto.

**Entidad `DatoParticipante`:** Estructura correcta para el dominio.

**BUG — `@PatchMapping` para obtener dato (DatoParticipanteController.java línea 89):**
```java
@PatchMapping("/obtenerDato/{id}")
```
Misma confusión que en usuario. Debe ser `@GetMapping`.

**BUG — Import de anotación web en capa de servicio (DatoParticipanteServiceImpl.java línea 16):**
```java
import org.springframework.web.bind.annotation.PathVariable;
```
`@PathVariable` importado en un `@Service`. No se usa. Indica que el código fue copiado y pegado desde un controller.

**BUG — `existeDato` con ternario trivial (DatoParticipanteServiceImpl.java línea 98):**
```java
return datoParticipanteRepository.existsById(id) ? true : false;
```
Simplificar a `return datoParticipanteRepository.existsById(id);`.

**BUG — `obtenerDatosXParticipante` con cast inseguro (línea 71):**
```java
return (List<PatologiaResponse>) datoParticipanteList.stream()...collect(Collectors.toList());
```
El cast a `List<PatologiaResponse>` es innecesario y genera warning de "unchecked cast". Quitar el cast.

**Problema — Sin seguridad:**
Ningún endpoint tiene `@PreAuthorize`. Datos médicos sin protección.

**Problema — URL con múltiples `@PathVariable` concatenados:**
```java
@GetMapping("/obtenerIdbyparticipanteAndFecha/idParticipante={idParticipante}&fecha={fecha}")
```
Esto no es REST estándar. Debería ser query params: `/obtenerIdbyparticipanteAndFecha?idParticipante={id}&fecha={fecha}` con `@RequestParam`.

**Problema — `insertarDato` silencioso al duplicar:**
Si ya existe un dato para el mismo participante y fecha, no inserta pero devuelve 200. El cliente no sabe que la inserción falló.

**Pendientes:**
- [ ] Corregir `@PatchMapping` → `@GetMapping` en `obtenerDato`.
- [ ] Eliminar import innecesario de `@PathVariable`.
- [ ] Simplificar ternario trivial en `existeDato`.
- [ ] Corregir cast innecesario en `obtenerDatosXParticipante`.
- [ ] Cambiar URL con `=` y `&` a query params estándar.
- [ ] Añadir `@PreAuthorize` en todos los endpoints.
- [ ] Lanzar excepción cuando el dato ya existe al insertar.
- [ ] **Corregir tipo de retorno de `SennalClient`** (`SennalDto` → `ListaSensoresDto`) o alinear el endpoint.

---

## 7. microservicio-patologia — 6/10

**Puerto:** 8088 · **Paquete base:** `com.example.patologia` · **Packaging:** jar
**Responsabilidad:** Catálogo de patologías y tabla relacional dato-patología.
**Es el microservicio más limpio del proyecto.** Lógica bien separada y simple.

### Endpoints

| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| POST | `/api/v1/patologia/create` | ❌ | Crear patología |
| POST | `/api/v1/patologia/createrelacion` | ❌ | Crear relación dato-patología |
| PUT | `/api/v1/patologia/update/{idpatologia}` | ❌ | Modificar (⚠ log dice "usuario") |
| DELETE | `/api/v1/patologia/delete/{idpatologia}` | ❌ | Eliminar (⚠ sin log en catch) |
| GET | `/api/v1/patologia/all` | ❌ | Listar todas |
| GET | `/api/v1/patologia/obtenerPatologiaXDato/{id_dato}` | ❌ | Patologías de un dato (devuelve entidad) |
| GET | `/api/v1/patologia/obtenerIdbyNombre/{nombre}` | ❌ | ID por nombre |
| DELETE | `/api/v1/patologia/eliminarrelacion/iddato={..}&fecha={..}` | ❌ | ⚠ URL no estándar |

### Entidades (JPA)

| Entidad | Tabla | Campos / PK |
|---------|-------|-------------|
| `Patologia` | `patologia` | id (IDENTITY), nombre |
| `DatoPatologia` | `dato_patologia` | **@Id solo en `iddato`**, idpatologia, fechaevaluacion (⚠ ver bug) |
| `Logs` | `log` | (igual) |

### DTOs

`PatologiaDto`, `DatoPatologiaDto`, `DatoDto`, `LogDto`

### Repositorios

- `PatologiaRepository`: existsByNombre, getIdbyNombre (JPQL)
- `DatoPatologisRepository` (sic, falta la "a"): deleteByIddatoAndFechaevaluacion, existsByIddatoAndIdpatologiaAndFechaevaluacion, findAllByIddato (JPQL JOIN Patologia-DatoPatologia)

### BUG — Clave primaria mal en `DatoPatologia`:
`@Id` solo en `iddato`. Significa que **un dato solo puede tener UNA patología** (la PK es única por dato), pero conceptualmente un paciente en una sesión puede tener varias patologías. Igual que en `UsuarioParticipante`: debe ser clave compuesta. La tabla SQL real usa `(id_dato, id_patologia, fecha_evaluacion)`, la entidad JPA no lo refleja.

**Entidad `Patologia`:** Solo `id` y `nombre`. Podría necesitar: descripción, categoría (neurológica, musculoesquelética...), nivel de riesgo.

**Problema — Log incorrecto en `eliminarPatologia` (PatologiaController.java línea 69):**
```java
logsService.insertarLog(request, aceptado, "Se modificó un usuario");
```
El mensaje dice "Se modificó un usuario" en el endpoint de modificar patología. Copiar-pegar sin actualizar.

**Problema — Sin log en `eliminarPatologia` (línea 83):**
```java
} catch (Exception e) {
    return ResponseEntity.badRequest().body("Ocurrio un error");
}
```
No llama a `logsService.insertarLog(...)` en el catch, al contrario que todos los demás métodos.

**Problema — URL con `=` y `&` en `eliminarRelacion`:**
```java
@DeleteMapping("/eliminarrelacion/iddato={iddato}&fecha={fecha}")
```
Misma mala práctica que en dato. No es REST estándar.

**Problema — `obtenerPatologiaXDato` devuelve `List<Patologia>` directamente:**
Devuelve entidades JPA, no DTOs. Si se añaden relaciones a la entidad en el futuro, se serializarán datos no deseados.

**Problema — Sin seguridad:**
Ningún endpoint tiene `@PreAuthorize`.

**Pendientes:**
- [ ] Corregir mensaje de log en `modificarPatologia`.
- [ ] Añadir log en catch de `eliminarPatologia`.
- [ ] Cambiar URL de `eliminarRelacion` a query params.
- [ ] Devolver `PatologiaDto` en lugar de entidad en `obtenerPatologiaXDato`.
- [ ] Añadir campos descriptivos a entidad `Patologia`.
- [ ] Añadir `@PreAuthorize` en todos los endpoints.
- [ ] **Corregir PK de `DatoPatologia`** a clave compuesta (hoy un dato solo admite 1 patología).

---

## 8. microservicio-sennal — 4/10

**Puerto:** 8089 · **Paquete base:** `com.example.sennal` · **Packaging:** jar
**Responsabilidad:** Recibe/persiste lecturas IMU. Es el **orquestador** que debería llamar a redneuronal (flujo comentado).

### Endpoints

| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| POST | `/api/v1/sennal/create` | ❌ | Insertar lista de señales (debería disparar predicción — comentado) |
| DELETE | `/api/v1/sennal/delete/{idSennal}` | ❌ | Eliminar señal |
| GET | `/api/v1/sennal/findAll/{iddato}` | ❌ | Señales de un dato → `ListaSensoresDto` |

### Entidades (JPA)

| Entidad | Tabla | Campos |
|---------|-------|--------|
| `Sennal` | `sennal` | id (IDENTITY), iddato, fecha, acelerometro x/y/z, giroscopio x/y/z, magnetometro x/y/z, temperatura, rangoacelerometro, rangogiroscopio, rangomagnetometro, frecuenciademuestreo |
| `Logs` | `log` | (igual) |

### DTOs

- `SennalDto` (objeto plano por muestra, 17 campos)
- `ListaSenalesDTO` (entrada: `List<SennalDto> senales` + `List<String> enfermedades`)
- `ListaSensoresDto` (salida: 9 `List<Double>` por eje de sensor)
- `dto/entrenamiento/`: `Sennal` (listas por eje + diagnosticos), `ModeloRequest` (sennalList + enfermedades), `PatologiaDto`

### Repositorios

- `SennalRepository`: findAllByIddato, existsByIddatoAndFecha

### Feign Clients

- `DatoClient` → `localhost:8087/...`, `existeDato(id)` → boolean
- `PatologiaClient` → `localhost:8088/...`, `obtenerPatologias()` → `List<PatologiaDto>`
- `RedNeuronalClient` → `localhost:8092/api/v1/modelo`, `entrenarConNuevosDatos(ModeloRequest)` → String **(declarado pero nunca usado — flujo comentado)**

**Entidad `Sennal`:** Estructura técnicamente correcta.

**BUG CRÍTICO — Flujo principal completamente comentado (SennalServiceImpl.java líneas 51–62):**
```java
/*List<PatologiaDto> enfermedades = enferm.obtenerPatologias();
...
//String respuesta = red.entrenarConNuevosDatos(modelo);
/*HttpClient cliente = HttpClient.newBuilder()...*/
```
El proceso de lanzar la predicción al insertar señales está comentado. El flujo núcleo del sistema (señal → red neuronal → resultado) no ocurre automáticamente. Cuando la app móvil envía señales, solo se guardan en BD, nunca se analizan.

**Problema — Imports muertos:**
```java
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
```
Imports no usados que quedaron de experimentos anteriores.

**Problema — `SenalesPredecir` nunca se llama:**
El método privado `SenalesPredecir` (línea 115) existe para preparar los datos para la red neuronal pero nunca se invoca porque todo el bloque está comentado.

**Problema — Sin seguridad:**
Ningún endpoint tiene `@PreAuthorize`. Cualquiera puede insertar o eliminar señales de sensores.

**Problema — No hay validación de datos de entrada:**
Se acepta cualquier valor para acelerómetro/giroscopio/magnetómetro sin validar rangos físicos posibles. Una señal con todos ceros pasaría sin error.

**Pendientes:**
- [ ] **PRIORIDAD ALTA** Descomentar y corregir el flujo inserción → predicción.
- [ ] Decidir: ¿predicción automática al insertar, o endpoint separado? (ver Espacio de Debate).
- [ ] Eliminar imports muertos.
- [ ] Añadir `@PreAuthorize` en todos los endpoints.
- [ ] Añadir validación de rangos en los DTOs de señal.

---

## 9. microservicio-redneuronal — 5/10

**Puerto:** 8092 · **Paquete base:** `com.example.redneuronal` · **Packaging:** jar
**Responsabilidad:** Entrenamiento e inferencia de la red neuronal Neuroph. Corazón técnico del sistema.
**Main:** `@SpringBootApplication @EnableDiscoveryClient @EnableFeignClients` (import `FeignClient` muerto, no hay feign clients aquí).

### Endpoints

| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| POST | `/api/v1/modelo/crear-inicial` | ❌ | Crear y entrenar modelo desde cero |
| POST | `/api/v1/modelo/entrenar` | ❌ | Reentrenar con datos nuevos (carga modelo de BD) |
| POST | `/api/v1/modelo/predecir` | ❌ | Top-3 patologías para una señal |

### Entidades / Modelo

| Clase | Tabla | Campos |
|-------|-------|--------|
| `RedNeuronalEntity` | `modeloredneuronal` | id, nombre (String), fecha (LocalDate), modelo (`bytea` byte[]) |
| `ModeloRequest` (DTO) | — | `List<Sennal> sennalList`, `List<String> enfermedades` |
| `Sennal` (util) | — | 9 `List<Double>` por eje + `List<String> diagnosticos` |
| `Prediccion` (util) | — | indice (int), valor (double) |

### Repositorio

- `RedNeuronalRepository`: `findTopByOrderByFechaDesc()` (siempre el más reciente)

### Lógica ML (clases clave)

- `SennalPreccesor`: mean, variance, stdDev, min, max. `extractSensorFeatures` → 15 features/sensor (5 stats × 3 ejes)
- `ObtenerCaracteristicas`: ensambla 45 features (15 acc + 15 gyro + 15 mag)
- `ModeloServiceImpl`: MLP SIGMOID, capa oculta `(45+salidas)/2`, BackPropagation, pseudo-etiquetado umbral 0.8
- `ModeloCRUDServiceImpl`: guarda/carga modelo como `.nnet` temporal → byte[] → BD

### Dependencias (pom propio)

- **Neuroph 2.98 con scope `system`** (JARs locales en `libs/neuroph-2.98/Framework/`): ajt-2.9, android-1.5_r4, neuroph-core/contrib/imgrec/ocr, visrec-api. ⚠ Los JARs `system-scope` NO se empaquetan en el fat-jar de Spring Boot → riesgo de `ClassNotFoundException` en runtime al desplegar.
- **`spring-cloud.version` = 2023.0.4** aquí, pero el padre `microservicio-gestion` usa **2022.0.4**. Versiones inconsistentes de Spring Cloud entre módulos.

### Perfil PROD (application-prod.yml) — bien hecho ✅

A diferencia de dev, el perfil prod: `ddl-auto: validate`, `show-sql: false`, swagger deshabilitado, todas las variables obligatorias con sintaxis `${VAR:?mensaje}`. Es el único servicio con un prod bien configurado.

**URL de BD en dev (application-dev.yml línea 5):**
```yaml
url: jdbc:postgresql:${RedNeuronalBDURL}
```
Falta `//` tras `postgresql:`. Funciona solo si la variable `RedNeuronalBDURL` empieza por `//host:puerto/bd`. Frágil y poco evidente.

### Análisis ML

**BUG ML 1 — Modelo se recarga desde BD en cada petición (ModeloController.java líneas 42 y 53):**
```java
modeloService.predecir(request, modeloCRUDService.cargarModelo());
modeloCRUDService.guardarModelo(modeloService.entrenarModeloConDatosNuevos(request, modeloCRUDService.cargarModelo()));
```
Cada predicción o entrenamiento hace una query completa a PostgreSQL para leer el modelo binario (`bytea`). Si el modelo pesa varios MB, esto es extremadamente lento. El modelo debería cargarse en memoria al arrancar el servicio y solo recargarse cuando se entrene uno nuevo.

**BUG ML 2 — Enfermedades no se guardan junto al modelo (ModeloCRUDServiceImpl.java línea 37):**
```java
entity.setNombre("modelo-principal");
```
El campo `nombre` de `RedNeuronalEntity` debería contener la lista de enfermedades separadas por coma (para que `cargarUltimasEnfermedades()` funcione), pero siempre se guarda como `"modelo-principal"`. `cargarUltimasEnfermedades()` devolverá siempre `["modelo-principal"]` en lugar de las enfermedades reales.

**BUG ML 3 — Función de salida del MLP incorrecta (ModeloServiceImpl.java línea 120):**
```java
if (idx >= 0) salida[idx] = 1.0 - (i * 0.3);
// Resultado: diagnóstico 1 = 1.0, diagnóstico 2 = 0.7, diagnóstico 3 = 0.4
```
Esta heurística arbitraria (degradar 0.3 por posición) confunde al MLP durante el entrenamiento. En multi-label classification lo correcto es one-hot (1.0 para cada diagnóstico presente).

**BUG ML 4 — Sin normalización de señales:**
`SennalPreccesor.extractSensorFeatures()` calcula estadísticas directamente sobre los valores crudos del sensor. Los rangos típicos son muy distintos: acelerómetro ±2g–±16g, giroscopio ±250–±2000 dps, magnetómetro ±4–±16 gauss. Sin normalización, las features de mayor magnitud dominarán el entrenamiento del MLP.

**Diseño ML — Arquitectura fija:**
```java
int ocultas = (ENTRADAS + salidas) / 2;
```
La arquitectura del MLP se calcula automáticamente. No hay forma de experimentar sin cambiar código. Sería mejor que el número de capas y neuronas sean parámetros en el `ModeloRequest`.

**Diseño ML — Solo un modelo activo:**
`findTopByOrderByFechaDesc()` siempre carga el más reciente. No hay versionado ni posibilidad de rollback a un modelo anterior que funcionara mejor.

**Positivo — Semi-supervised learning implementado:**
El pseudo-etiquetado con umbral 0.8 para señales no etiquetadas es un buen enfoque.

**Positivo — Top-3 predicciones:**
Devolver las 3 patologías más probables con porcentaje es buena UX para el clínico.

**Pendientes:**
- [ ] **URGENTE** Corregir URL de BD en `application-dev.yml` (`jdbc:postgresql:` → `jdbc:postgresql://`).
- [ ] Cachear el modelo en memoria (campo `@Autowired` con `@PostConstruct` o evento `ApplicationReady`).
- [ ] Corregir `guardarModelo` para que guarde la lista de enfermedades en el campo `nombre`.
- [ ] Corregir `generarSalidasDesdeDiagnosticos` a one-hot puro.
- [ ] Añadir normalización Z-score o Min-Max en `SennalPreccesor`.
- [ ] Añadir versionado: campo `version` e `accuracy` en `RedNeuronalEntity`.
- [ ] Parametrizar arquitectura MLP en `ModeloRequest`.
- [ ] **Resolver el problema de JARs system-scope** (instalar Neuroph en repo local Maven o usar `includeSystemScope=true` en el plugin).
- [ ] Unificar `spring-cloud.version` con el del padre (2022.0.4 vs 2023.0.4).
- [ ] Eliminar import muerto `FeignClient` en la clase main.

---

## 10. microservicio-log — 1/10

**Puerto:** Sin asignar
**Archivos analizados:** MicroservicioLogApplication.java, util/ (8 clases)

**Estado real: el módulo es un cascarón vacío.**

Solo contiene:
- `MicroservicioLogApplication.java` (clase de arranque con `@EnableDiscoveryClient`)
- 8 clases en `util/` copiadas literalmente de `microservicio-usuario`:
  - `ConversionObjetos`, `DescrifradoBase64Android`, `FechaFormato`, `IpUtils`, `TokenUtils`, `UsuarioUtil`, `Validacion`

**Lo que falta (absolutamente todo):**
- No tiene controller.
- No tiene entidad ni tabla propia.
- No tiene servicio.
- No tiene repository.
- No tiene `application.yml` para dev/prod.
- No tiene ninguna funcionalidad.

La estructura de carpetas tiene `core/controller/` y `core/aspect/` pero están vacías (solo con `.gitkeep`).

**Impacto:** Cada microservicio guarda sus logs en su propia tabla `logs` local. No hay sistema centralizado. Es imposible correlacionar una petición que atraviesa 4 microservicios.

**Pendientes:**
- [ ] Decidir: ¿completar el microservicio-log o eliminar el módulo? (ver Espacio de Debate).
- [ ] Si se completa: añadir entidad `Log`, `LogRepository`, `LogController` con endpoint `POST /api/v1/log` que reciba logs de todos los servicios.
- [ ] Si se completa: cada microservicio debe llamar a este servicio vía Feign en lugar de guardar en BD local.
- [ ] Eliminar las 8 clases de `util/` duplicadas de usuario.

---

## 12. interfaz_visual (FRONTEND) — 2/10

**Ubicación:** `interfaz_visual/` (NO es módulo Maven, es proyecto Node independiente)
**Stack:** React 19.2 + Vite 8 + Bootstrap 5.3 + TailwindCSS 4.2 + TypeScript 6 (configurado, no usado aún)

**Estado real:** Proyecto recién inicializado. Solo existe:
- `src/main.jsx`: renderiza un `<h1>Hola Rayner</h1>` y un botón de Bootstrap. Nada más.
- `src/index.css`, `index.html`, `vite.config.js`, `eslint.config.js`
- `node_modules/` instalado (5256 archivos)

**Lo que NO existe todavía:**
- No hay componentes, ni rutas (no hay react-router), ni llamadas a la API.
- No hay pantallas de login, listados, ni formularios.
- No hay cliente HTTP (axios/fetch wrapper) para consumir el gateway.
- No hay gestión de estado ni de tokens JWT.
- Mezcla Bootstrap **y** Tailwind a la vez (decidir uno).

**Scripts disponibles:** `npm run dev` (Vite dev server), `npm run build`, `npm run lint`, `npm run preview`.

**Pendientes:**
- [ ] Decidir arquitectura del frontend (ver Espacio de Debate F3).
- [ ] Elegir entre Bootstrap o Tailwind (no ambos).
- [ ] Añadir react-router para navegación.
- [ ] Crear cliente HTTP que apunte al gateway (`http://localhost:8081`).
- [ ] Implementar login + almacenamiento de JWT + interceptor de Authorization.
- [ ] Pantallas: login, listado de participantes, detalle de paciente, visualización de señales/predicciones.

---

## 13. diagramas/ (DOCUMENTACIÓN)

Carpeta con 2 imágenes de arquitectura:
- `Arquitectura app movil.png`
- `Diagrama de arquitectura Inicial.png`

> Útiles como referencia visual. No se han abierto en este análisis. Conviene revisarlos al planear cambios de arquitectura.

---

## 11. BASE DE DATOS (sensor_inteligente.sql) — 6/10

**Tablas identificadas:** `usuario`, `rol`, `estado`, `participante`, `usuario_participante`, `datoparticipante`, `sennal`, `patologia`, `dato_patologia`, `logs`, `modeloredneuronal`

**Triggers implementados:**
- `tr_comprobar_contrasenna`: valida que contraseña == repetircontraseña al insertar usuario. ✅
- `tr_patologia_existente`: evita patologías duplicadas (ignora mayúsculas y espacios). ✅
- `tr_relación repetida`: evita duplicados en `dato_patologia`. ✅
- `tr_relación repetida_usuario-participante`: evita duplicados en `usuario_participante`. ✅

**Stored procedures:** `datoparticipante_insert/update/delete`, `participante_insert/update/delete`, `patologia_insert/delete`, `sennal_insert/delete`, `dato_patologia_insert`. Buenos, pero los microservicios no los usan (usan JPA directo).

**Problema BD1 — `ddl-auto: update` en todos los perfiles incluyendo prod:**
Hibernate puede alterar el schema automáticamente en producción. Debería ser `validate` en prod y usar Flyway.

**Problema BD2 — Contraseña default `0000`:**
```yaml
password: ${DB_PASSWORD:0000}
```
En todos los microservicios. Si no se configura la variable, arranca con credenciales triviales.

**Problema BD3 — `show-sql: true` en dev (y posiblemente en prod):**
Imprime todas las queries SQL en los logs. En producción expone la estructura de datos.

**Problema BD4 — Tabla `logs` duplicada:**
Cada microservicio tiene su propia entidad `Logs` que apunta a la misma tabla `logs` de PostgreSQL. No está claro si todas las entidades comparten la misma tabla o si cada servicio tiene la suya. Necesita clarificarse.

**Problema BD5 — Sin índices explícitos en columnas clave:**
La tabla `sennal` tendrá millones de filas. No hay índice en `id_dato`. La tabla `participante` no tiene índice en `ci`. Las búsquedas serán full table scan.

**Problema BD6 — `modeloredneuronal` sin versionado:**
Solo se almacena el modelo más reciente (se busca por `findTopByOrderByFechaDesc`). No hay versión, accuracy, ni notas del modelo.

**Pendientes:**
- [ ] Añadir índice en `sennal(id_dato)` y `participante(ci)`.
- [ ] Cambiar `ddl-auto: update` → `validate` en perfil `prod`.
- [ ] Implementar Flyway para migraciones versionadas.
- [ ] Cambiar contraseña default por algo no trivial.
- [ ] Añadir columnas `version`, `accuracy`, `descripcion` a `modeloredneuronal`.
- [ ] Clarificar si `logs` es una sola tabla compartida o una por servicio.

---

## 12. CI/CD (GitHub Actions) — 3/10

**Archivo:** `.github/workflows/github-actions-demo.yml`

**Lo que hace:**
- Se activa en push a `master`.
- Configura JDK 17 con Temurin.
- Ejecuta `mvn -B clean install -DskipTests`.

**Problemas:**
- **Tests saltados siempre:** `-DskipTests`. Si se rompe algo, CI no lo detecta.
- **Solo compila, no despliega:** No hay CD. No hay Docker build. No hay deploy a ningún entorno.
- **No hay etapa de análisis de calidad:** Sin SonarQube, sin Checkstyle, sin análisis de vulnerabilidades.
- **No hay caché de dependencias Maven efectiva:** `cache: maven` está configurado pero el `pom.xml` raíz con todas las dependencias innecesarias hace que la caché sea enorme.

**Pendientes:**
- [ ] Quitar `-DskipTests` y activar los tests.
- [ ] Añadir step de Docker build y push a registry.
- [ ] Añadir análisis con SonarQube o similar.

---

## CÓDIGO DUPLICADO ENTRE MICROSERVICIOS

Las siguientes clases están copiadas literalmente en 5–7 microservicios:

| Clase | Aparece en |
|-------|-----------|
| `IpUtils` | usuario, participante, dato, patologia, sennal, log |
| `UsuarioUtil` | usuario, participante, dato, patologia, sennal, log |
| `DescrifradoBase64Android` | usuario, log |
| `FechaFormato` | usuario, log |
| `TokenUtils` | usuario, log |
| Entidad `Logs` | usuario, participante, dato, patologia, sennal |
| `LogsService` / `LogsServiceImpl` | usuario, participante, dato, patologia, sennal |
| `SearchException` | usuario, participante, dato, patologia, sennal, redneuronal |

**Solución:** Crear un módulo Maven `microservicio-commons` con estas clases compartidas y que todos los demás lo importen.

---

## DEPENDENCIAS EN `pom.xml` QUE NO SE USAN EN EL CÓDIGO

Están declaradas en el `pom.xml` de `microservicio-gestion` pero sin implementación:

| Dependencia | Estado |
|-------------|--------|
| `spring-kafka` | Añadida, no se usa en ningún servicio |
| `spring-boot-starter-amqp` (RabbitMQ) | Configurado en yml pero sin consumers ni producers implementados. Puede fallar al arrancar si no hay broker. |
| `spring-boot-starter-data-redis` | Sin ningún `@Cacheable` ni `RedisTemplate` en uso |
| `spring-ai-client-chat` | Sin ninguna integración IA implementada |
| `spring-ai-pgvector-store` | Sin uso |
| `spring-boot-starter-graphql` | Sin ningún schema `.graphqls` ni resolver |
| `micrometer-registry-prometheus` | Dependencia añadida pero Prometheus no configurado |
| `spring-cloud-starter-circuitbreaker-resilience4j` | Sin ningún `@CircuitBreaker` implementado |

---

## FUNCIONALIDADES FALTANTES Y PROPUESTAS

> Esta sección es para debatir. Claude escribe `[C]`, el desarrollador escribe `[R]`.

### F1 — Flujo de predicción automática al insertar señales
El flujo `insertar señal → predicción → resultado` está comentado en `SennalServiceImpl`. La app móvil envía señales y no recibe ningún diagnóstico.
**[C]:** ¿Quieres que al insertar señales se lance la predicción automáticamente y el resultado se devuelva en la misma respuesta? ¿O prefieren que el médico lance la predicción manualmente?
**[R]:** ___

### F2 — Historial de predicciones por participante
No existe tabla ni endpoint para guardar resultados históricos de predicciones. No se puede ver la evolución del riesgo de caída a lo largo del tiempo.
**[C]:** Propongo añadir tabla `prediccion(id, id_dato, fecha, patologias_json, confianza_max)` y endpoint en microservicio-dato para consultarla.
**[R]:** ___

### F3 — Frontend `interfaz_visual` (React + Vite, recién iniciado)
**[C]:** El frontend ya existe con React 19 + Vite + Bootstrap + Tailwind, pero solo muestra "Hola Rayner". ¿Lo desarrollamos como dashboard web completo? ¿Eliges Bootstrap o Tailwind (ahora están los dos)? ¿Qué pantallas son prioritarias: login, gestión de pacientes, visualización de predicciones?
**[R]:** ___

### F4 — Completar microservicio-log
**[C]:** El módulo existe pero está vacío. Opciones: (A) Completarlo como servicio centralizado de logs con Feign desde todos los servicios. (B) Eliminarlo y mantener logs locales en cada servicio. (C) Usar ELK Stack / Loki en el futuro.
**[R]:** ___

### F5 — Reentrenamiento automático del modelo
**[C]:** Con cada nuevo participante procesado, el modelo podría reentrenarse periódicamente con `@Scheduled`. ¿Cuándo reentrenar? ¿Cada N días? ¿Cuando hay X nuevas señales etiquetadas?
**[R]:** ___

### F6 — Normalización de señales IMU
**[C]:** Sin normalización la red neuronal puede dar resultados inconsistentes. Propongo añadir Z-score en `SennalPreccesor` antes de `extractSensorFeatures`. Bajo impacto, alta mejora en calidad ML.
**[R]:** ___

### F7 — Docker Compose para desarrollo local
**[C]:** Levantar 8+ microservicios manualmente es inviable. Propongo crear `docker-compose.yml` con todos los servicios + PostgreSQL. ¿Tienes Docker instalado?
**[R]:** ___

### F8 — Roles médicos más granulares
**[C]:** Actualmente solo hay `Administrador` y `Super Administrador`. Para un sistema médico real haría falta: `Médico` (ve sus pacientes), `Fisioterapeuta`, `Investigador` (acceso a datos anonimizados). Los roles de `TokenController.java` ya mencionan `ROLE_Médico`, `ROLE_Paciente`, `ROLE_Investigador` pero no existen en la BD.
**[R]:** ___

---

## PRIORIDADES INMEDIATAS

### 🔴 CRÍTICO — Hacer antes de cualquier otra cosa
1. **Corregir clave JWT** — `TokenService.java` y `TokenControllerServiceImpl.java` usan literal `"${jwt.secreto}"`. Inyectar con `@Value`.
2. **Eliminar `password` del payload JWT** — `TokenService.java` línea 26.
3. **Corregir lógica del `SecurityFilter`** — La condición está invertida, el filtro no valida nada.
4. **Mover clave AES a variable de entorno** — `DescrifradoBase64Android.java` línea 20.
5. **Corregir `validarToken`** — Siempre devuelve false (BCrypt vs AES). Validación de tokens entre servicios rota.
6. **Crear endpoint `/existeUsuario/{id}`** — Participante lo llama por Feign y recibe 404 → no se pueden crear participantes.
7. **Corregir `FiltroGlobal` del gateway** — Retorna null (NullPointerException) y no tiene `@Component`. Nunca se ejecuta.
8. **Corregir `TokenConnection` del gateway** — URL malformada con token en path. Código muerto.
9. **Corregir PKs de `UsuarioParticipante` y `DatoPatologia`** — Claves primarias mal puestas limitan las relaciones a 1:1.
~~8. **Arreglar el Config Server**~~ ✅ RESUELTO 2026-06-22

### 🟡 IMPORTANTE — Segunda fase
6. **Activar flujo señal → predicción** — Descomentar y completar `SennalServiceImpl.java`.
7. **Cachear modelo neuronal en memoria** — Evitar carga desde BD en cada petición.
8. **Corregir `guardarModelo`** — Guardar enfermedades reales, no `"modelo-principal"`.
9. **Corregir función de salida MLP** — `generarSalidasDesdeDiagnosticos` a one-hot puro.
10. **Eliminar `datoDtoList.get(0)`** — Bug de crash en `ParticipanteServiceImpl`.
11. **Corregir ruta gateway** — `Path=/api/**` absorbe todo. Especificar rutas concretas.
12. **Añadir seguridad** — `@PreAuthorize` en endpoints de participante, dato, patología, sennal.

### 🟢 MEJORAS — Cuando los críticos estén resueltos
13. Crear módulo `microservicio-commons` con clases compartidas.
14. Limpiar dependencias no usadas del `pom.xml`.
15. Añadir normalización Z-score en `SennalPreccesor`.
16. Implementar `microservicio-log` o decidir eliminarlo.
17. Añadir índices en `sennal(id_dato)` y `participante(ci)`.
18. Añadir versionado de modelos en `RedNeuronalEntity`.
19. Crear `docker-compose.yml`.
20. Activar tests en CI (quitar `-DskipTests`).
21. Implementar Flyway para migraciones.
22. Tests unitarios para `SennalPreccesor` y `ModeloServiceImpl`.

---

## NOTAS TÉCNICAS RÁPIDAS

- **DB local:** PostgreSQL `localhost:5432`, BD: `sensores_inteligentes`, user: `postgres`, pass: `0000` (dev)
- **Orden de arranque:** Eureka (:8761) → Config (:8888) → Gateway (:8081) → resto en cualquier orden
- **Puertos:** Gateway:8081, Eureka:8761, Usuario:8085, Participante:8086, Dato:8087, Patologia:8088, Sennal:8089, RedNeuronal:8092
- **Swagger:** `/swagger-ui.html` en cada servicio por su puerto directo
- **JWT secreto actual (DEV):** La cadena LITERAL `${jwt.secreto}` — bug crítico sin corregir
- **Clave AES descifrado:** `08wR?!S6_wo&-v$f#0RUdrEfRoclTh` hardcodeada en código — bug crítico sin corregir
- **Modelo neuronal:** Tabla `modeloredneuronal` como `bytea` en PostgreSQL
- **Features:** 45 = (media, std, min, max, varianza) × 3 ejes × 3 sensores (acc + gyro + mag)
- **Neuroph JARs:** `microservicio-redneuronal/libs/neuroph-2.98/` (scope: system)
- **RabbitMQ:** Configurado en yml pero sin broker ni implementación — puede hacer fallar el arranque
- **CI:** GitHub Actions solo compila, tests saltados con `-DskipTests`
