<div align="center">

# 🖥️ Interfaz Visual — Sistema de Análisis de Marcha

### Panel web de gestión para el monitoreo de patologías de marcha en adultos mayores

<br/>

[![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev/)
[![Vite](https://img.shields.io/badge/Vite-8.x-646CFF?style=for-the-badge&logo=vite&logoColor=white)](https://vitejs.dev/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-4-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)](https://getbootstrap.com/)
[![License](https://img.shields.io/badge/Licencia-MIT-yellow?style=for-the-badge)](../LICENSE)

<br/>

> **Aplicación web** en React 19 que provee a los profesionales médicos una interfaz para gestionar participantes, visualizar sesiones de captura IMU y consultar los resultados de detección de patologías generados por el backend.

</div>

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Características Principales](#-características-principales)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Ejecución](#-ejecución)
- [Scripts Disponibles](#-scripts-disponibles)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Conexión con el Backend](#-conexión-con-el-backend)
- [Licencia](#-licencia)
- [Contacto](#-contacto)

---

## 🎯 Descripción

**Interfaz Visual** es el frontend web del Sistema de Análisis de Monitoreo de la Marcha. Consume la API REST expuesta por el backend de microservicios a través del API Gateway (`:8081`) y permite a los profesionales de salud:

- Gestionar usuarios, participantes y patologías desde un panel centralizado.
- Consultar las sesiones de captura de datos IMU registradas para cada participante.
- Visualizar los resultados de clasificación de patologías de marcha generados por la red neuronal.
- Autenticarse con sus credenciales y operar según su rol (`SUPER_ADMIN`, `ADMIN`, `APP_USER`).

---

## ✨ Características Principales

| Característica | Detalle |
|----------------|---------|
| ⚛️ **React 19** | UI reactiva con el último runtime de React |
| ⚡ **Vite 8** | HMR instantáneo y compilación ultra-rápida |
| 🎨 **Tailwind CSS 4** | Estilos utility-first para diseño consistente |
| 🧩 **Bootstrap 5** | Componentes UI listos para usar |
| 🔐 **Autenticación JWT** | El token obtenido del backend se adjunta en cada petición |
| 📡 **Integración API REST** | Conectado al API Gateway del backend vía HTTP |

---

## 🛠 Tecnologías Utilizadas

| Tecnología | Versión | Uso |
|-----------|---------|-----|
| React | 19.2.4 | Framework de interfaz de usuario |
| Vite | 8.0.4 | Bundler y servidor de desarrollo |
| Tailwind CSS | 4.2.2 | Sistema de estilos utility-first |
| Bootstrap | 5.3.8 | Componentes y layout responsivo |
| ESLint | 9.x | Análisis estático de código |

---

## 📦 Requisitos Previos

| Herramienta | Versión mínima | Verificar |
|------------|---------------|-----------|
| Node.js | **18+** | `node --version` |
| npm | **9+** | `npm --version` |
| Backend activo | — | API Gateway en `http://localhost:8081` |

> ⚠️ El frontend depende del backend. Asegúrate de que el API Gateway y los microservicios estén corriendo antes de iniciar el servidor de desarrollo.

---

## 🚀 Instalación

### 1. Ubicarse en el directorio del frontend

```bash
cd frontend
```

### 2. Instalar dependencias

```bash
npm install
```

---

## ▶️ Ejecución

```bash
npm run dev
```

La aplicación queda disponible en: **`http://localhost:3000`**

---

## 📜 Scripts Disponibles

| Comando | Descripción |
|---------|-------------|
| `npm run dev` | Inicia el servidor de desarrollo con HMR |
| `npm run build` | Genera el build de producción en `dist/` |
| `npm run preview` | Sirve localmente el build de producción |
| `npm run lint` | Ejecuta ESLint sobre todos los archivos fuente |

---

## 📁 Estructura del Proyecto

```
frontend/
├── public/                  # Archivos estáticos públicos
├── src/
│   ├── main.jsx             # Punto de entrada de la aplicación
│   └── index.css            # Estilos globales (Tailwind base)
├── index.html               # HTML raíz
├── vite.config.js           # Configuración de Vite
├── eslint.config.js         # Configuración de ESLint
└── package.json             # Dependencias y scripts
```

---

## 🔗 Conexión con el Backend

Todas las peticiones HTTP se dirigen al API Gateway del backend:

| Recurso | URL base |
|---------|----------|
| API Gateway | `http://localhost:8081` |
| Documentación Swagger | `http://localhost:<puerto>/swagger-ui.html` |

El token JWT obtenido en el login se envía en el encabezado de cada petición:

```
Authorization: Bearer <token>
```

Para cambiar la URL base del backend, actualiza la constante correspondiente en la configuración de la aplicación.

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
