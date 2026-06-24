# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot microservices backend for a gait analysis system that collects IMU sensor data (accelerometer, gyroscope, magnetometer) from mobile devices, processes it through feature extraction, and runs neural network inference to detect fall-risk pathologies in elderly patients.

## Build & Run Commands

```bash
# Build all modules (skip tests)
mvn clean install -DskipTests

# Run tests across all modules
mvn test

# Run a single module's tests
mvn -pl microservicio-gestion/microservicio-usuario test

# Start individual microservices (use Maven wrapper if preferred: ./mvnw)
mvn -pl microservicio-eureka spring-boot:run
mvn -pl microservicio-config spring-boot:run
mvn -pl microservicio-gateway spring-boot:run
mvn -pl microservicio-gestion/microservicio-usuario spring-boot:run
mvn -pl microservicio-gestion/microservicio-participante spring-boot:run
mvn -pl microservicio-gestion/microservicio-dato spring-boot:run
mvn -pl microservicio-gestion/microservicio-patologia spring-boot:run
mvn -pl microservicio-gestion/microservicio-sennal spring-boot:run
mvn -pl microservicio-gestion/microservicio-redneuronal spring-boot:run
```

**Required startup order:** Eureka → Config → Gateway → any `microservicio-gestion/*` services (order among these is flexible).

## Database Setup

PostgreSQL database `sensores_inteligentes`:

```bash
# Create database
psql -U postgres -c "CREATE DATABASE sensores_inteligentes WITH ENCODING = 'UTF8';"

# Load schema, stored procedures, and triggers
psql -U postgres -d sensores_inteligentes -f sensor_inteligente.sql
```

Hibernate `ddl-auto=update` handles incremental schema changes after initial load. Each service connects on `localhost:5432` by default; production uses environment variables `${RedNeuronalBDURL}` / `${SensoresBDURL}`, `${DB_USER}`, `${BD_PASS}`.

Tests use an H2 in-memory database (no PostgreSQL required).

## Architecture

```
Mobile App
    │
    ▼
microservicio-gateway :8081      ← Routes all external requests
    │
    ├── microservicio-eureka :8761   ← Service discovery (Netflix Eureka)
    ├── microservicio-config         ← Centralized config (Spring Cloud Config)
    │
    └── microservicio-gestion/
        ├── microservicio-usuario :8085     ← Auth, JWT, user roles
        ├── microservicio-participante :8086 ← Patient management
        ├── microservicio-dato :8087        ← Raw sensor data persistence
        ├── microservicio-patologia :8088   ← Pathology classification
        ├── microservicio-sennal :8089      ← Signal processing & feature extraction
        └── microservicio-redneuronal :8092 ← Neuroph inference (packaged as WAR)
```

Inter-service calls use OpenFeign (`@FeignClient`). `microservicio-sennal` is the orchestrator — it calls dato, patologia, and redneuronal clients in sequence.

## Package Conventions

Every `microservicio-gestion/*` service follows this layout under its base package (e.g. `com.example.usuario`):

```
config/            ← Spring beans: CORS, Security, Swagger, TokenService
core/
  controller/      ← REST endpoints
  dto/             ← Request/response DTOs (suffix: Dto)
  entities/        ← JPA entities (no suffix)
  repository/      ← Spring Data repositories (suffix: Repository)
  service/         ← Business logic interfaces (suffix: Service)
  serviceimpl/     ← Implementations (suffix: ServiceImpl)
  feingclient/     ← OpenFeign client interfaces
  exception/       ← Custom exceptions
  controllerException/ ← @ControllerAdvice handlers
aspect/            ← AOP logging aspects (writes to logs table via LogsService)
```

## Key Technology Choices

- **Java 17 / Spring Boot 3.3.6 / Spring Cloud 2023.0.4**
- **Security:** Spring Security + JWT (`java-jwt 4.4.0`). `TokenService` issues/validates tokens; `SecurityFilter` intercepts requests. Role-based access is enforced per controller.
- **Neural network:** Neuroph 2.98 (Java) via system-scope JARs in `microservicio-redneuronal/libs/neuroph-2.98/`. `ObtenerCaracteristicas` + `SennalPreccesor` extract features; `ModeloService` runs inference.
- **API docs:** SpringDoc OpenAPI 2.5.0 — Swagger UI available at `/swagger-ui.html` on each service.
- **AOP logging:** `ControllerAspecto` captures method entry/exit and persists to the `logs` DB table.

## Database Objects

Key tables: `usuario`, `participante`, `datoparticipante`, `sennal`, `patologia`, `dato_patologia`, `logs`.

The SQL file defines stored procedures (e.g. `datoparticipante_insert`, `sennal_insert`) and triggers (`tr_comprobar_contrasenna` for password validation, `tr_patologia_existente` for duplicate prevention). Prefer calling the stored procedures through service layer rather than raw JPQL/HQL for mutation operations.

## CI/CD

GitHub Actions (`.github/workflows/github-actions-demo.yml`) runs `mvn -B clean install -DskipTests` on every push to `master`. Tests are skipped in CI; run them locally before merging.