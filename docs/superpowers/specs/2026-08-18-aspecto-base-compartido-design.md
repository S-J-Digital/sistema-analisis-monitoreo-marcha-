# Diseño — Clase base compartida para los aspectos de logging (`microservicio-usuario`)

**Fecha:** 2026-08-18
**Módulo:** `backend/microservicio-gestion/microservicio-usuario`
**Rama:** `usuario`

## Contexto

`microservicio-usuario` ya tenía `ControllerAspecto` (`core/aspect/ControllerAspecto.java`), un `@Aspect` con un único método `@Around` que envuelve todas las llamadas a los controllers (`core.controller.*`), mide el tiempo de ejecución y persiste un registro en la tabla `logs` vía `LogsService.insertarLog(...)` — tanto en éxito ("Aceptado") como en fallo ("Rechazado"), incluyendo IP, usuario, nombre del método y tiempo.

Se crearon dos clases nuevas, vacías, como punto de partida para extender el mismo tipo de logging a las otras dos capas del patrón `Controller → ServiceController → Service` que sigue el proyecto:
- `ServiceControllerAspecto` (para `core.serviceController.serviceControllerImpl.*`)
- `ServiceAspecto` (para `core.service.serviceimpl.*`)

Copiar el método `@Around` de `ControllerAspecto` tal cual en las otras dos clases duplicaría la lógica 3 veces. El objetivo de este cambio es compartir esa lógica sin perder la separación en 3 clases (una por capa), porque cada una necesita su propio pointcut.

## Objetivo

Extraer la lógica común de logging a una clase base abstracta, y que las 3 clases de aspecto (`ControllerAspecto`, `ServiceControllerAspecto`, `ServiceAspecto`) la reutilicen, cada una aportando solo su propio pointcut.

## Diseño

### `AspectoBase` (nueva clase abstracta, `core/aspect/AspectoBase.java`)

- Constructor `protected AspectoBase(LogsService logsService)` que guarda `logsService` en un campo `protected final`.
- Campo `protected final Logger log` (igual que las clases actuales).
- Método `protected Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable` con exactamente la lógica que hoy tiene `ControllerAspecto.logAroundController`:
  1. Obtiene el `HttpServletRequest` actual vía `RequestContextHolder`.
  2. Toma el nombre del método desde `joinPoint.getSignature().getName()`.
  3. Mide el tiempo de ejecución alrededor de `joinPoint.proceed()`.
  4. En éxito: `logsService.insertarLog(request, "Aceptado", "Operacion finalizada con éxito", method, tiempo)`.
  5. En excepción: `logsService.insertarLog(request, "Rechazado", mensaje, method, tiempo)` y relanza.

### Las 3 clases concretas

Cada una:
- Extiende `AspectoBase`.
- Mantiene su propio constructor `@Autowired` que llama a `super(logsService)`.
- Define un único método `@Around("<pointcut de su capa>")` de una línea que delega a `logExecution(joinPoint)`.

Pointcuts:

| Clase | Pointcut |
|---|---|
| `ControllerAspecto` | `execution(* com.example.usuario.core.controller.*.*(..))` *(sin cambios respecto al actual)* |
| `ServiceControllerAspecto` | `execution(* com.example.usuario.core.serviceController.serviceControllerImpl.*.*(..))` |
| `ServiceAspecto` | `execution(* com.example.usuario.core.service.serviceimpl.*.*(..)) && !within(com.example.usuario.core.service.serviceimpl.LogsServiceImpl)` |

### Por qué `ServiceAspecto` excluye `LogsServiceImpl`

`LogsServiceImpl` vive en el mismo paquete (`core.service.serviceimpl`) que las demás implementaciones de servicio. Si el pointcut de `ServiceAspecto` cubriera ese paquete sin excepción, cada llamada a `logsService.insertarLog(...)` (incluida la que hace el propio aspecto para registrar el log) sería interceptada por el mismo aspecto, que al terminar volvería a llamar a `insertarLog` para registrar *esa* ejecución — generando recursión infinita / `StackOverflowError`.

La exclusión `&& !within(...)` es un punto de partida; el desarrollador la ajustará manualmente más adelante si hace falta un enfoque distinto (p. ej. mover `LogsServiceImpl` a otro paquete).

## Efecto secundario esperado (aceptado conscientemente)

Con las 3 capas instrumentadas, una sola petición HTTP que atraviese Controller → ServiceController → Service generará **hasta 3 filas en `logs`** (una por capa), donde antes generaba 1. Es el resultado esperado de tener trazabilidad por capa; no se busca deduplicar en este cambio.

## Fuera de alcance

- No se toca el contenido de `LogsServiceImpl`, `LogsService`, ni el esquema de la tabla `logs`.
- No se añade ningún campo nuevo al log (p. ej. "capa") — cada fila se distingue por el nombre del método que ya se registra.
- No se afinan más los pointcuts de exclusión más allá de lo indicado — el desarrollador lo hará manualmente si es necesario.
- No se tocan los hallazgos de seguridad pendientes de `microservicio-usuario` (quedan documentados en `pendientes.md`, sección 3).

## Testing

- Levantar el microservicio y ejercitar un flujo que atraviese las 3 capas (p. ej. `POST /api/login/`), confirmando que aparecen las filas esperadas en `logs` sin errores de recursión.
- Provocar un fallo (p. ej. usuario inexistente) para confirmar que la rama "Rechazado" también se registra en las 3 capas sin romper la respuesta de error al cliente.
- Confirmar explícitamente que insertar un log (llamada interna a `LogsServiceImpl.insertarLog`) no dispara el propio `ServiceAspecto` sobre sí mismo.
