# Sistema de gestión, monitoreo y análisis de datos provenientes de sensores conectados al cuerpo

## Objetivos
* Crear una API para recopilar y procesar datos de sensores (acelerómetro, giroscopio, magnetómetro).
* Desarrollar una red neuronal en Java para el análisis de los datos de marcha.
* Integrar la API con la aplicación móvil para la gestión de usuarios médicos.

### Requisitos funcionales:
1. Recolección de Datos de Sensores

   * La aplicación debe poder obtener datos de los sensores del dispositivo móvil (acelerómetro, giroscopio, magnetómetro, entre otros) para analizar la marcha.
   * Debe permitir al usuario seleccionar la frecuencia de muestreo de los sensores (ej. 10 Hz, 50 Hz, 100 Hz, etc.).
   * El usuario debe poder configurar el rango del acelerómetro (2g, 4g, 8g, 16g) y el rango del giroscopio (250, 500, 1000, 2000 grados por segundo).
2. API de Backend

   * La API debe ser capaz de recibir y procesar los datos enviados desde la aplicación móvil.
   * La API debe permitir el acceso seguro a los datos, autenticando a los usuarios (por ejemplo, mediante un sistema de login con contraseña encriptada).
   * Debe soportar la creación, lectura, actualización y eliminación de registros (CRUD) de los datos recolectados.
   * Los usuarios deben poder acceder a los análisis de los datos de la marcha a través de la API.
   * La API debe enviar resultados de las predicciones de la red neuronal de vuelta a la aplicación móvil para su visualización.
3. Red Neuronal

   * La red neuronal debe ser capaz de analizar los datos de los sensores (acelerómetro, giroscopio, etc.) y determinar si la marcha es normal o si presenta algún patrón asociado a posibles afecciones.
   * Debe ofrecer predicciones sobre el estado de la marcha de los adultos mayores basadas en los datos de los sensores recolectados.
   * La red neuronal debe ser entrenada con un conjunto de datos previamente recopilados y normalizados.
4. Visualización y Reportes

   * La aplicación debe permitir que el usuario vea el análisis de los datos de la marcha y los resultados de las predicciones de forma visual.
   * El sistema debe generar reportes detallados que puedan ser consultados por médicos u otros profesionales de la salud.

### Arquitectura
#### Arquitectura Híbrida:
El sistema consta de 2 subsistemas con arquitecturas diferentes:
1. Aplicación movil con arquitectura monolítica modular en la cual los módulos están diseñados para gestionar funciones específicas como la recolección de datos, configuración de sensores, roles de usuario y comunicación con la API.
![Arquitectura de la aplicación movil](diagramas\Arquitectura%20app%20movil.png "Arquitectura de la aplicación movil")
2. Api con arquitectura de microservicios la cual está diseñada como un conjunto de servicios independientes que pueden escalar y desplegarse de manera autónoma.
![Arquitectura de la Api](diagramas\Diagrama%20de%20arquitectura%20Inicial.png "Arquitectura de la Api")
3. **Tecnologías utilizadas en el desarrollo:**
   - Backend: Java, Spring Boot
   - Base de Datos: SQLite, PostgreSql
   - Aplicación movil: Android Studio (Java)
   - Red Neuronal: DeepLearning4J (DL4J)