# Startrack CSGO API Viewer

Este proyecto es una aplicación construida con **Ktor** y **HTMX** que consume información relacionada con **skins, agentes, cajas (crates)** y **llaves (keys)** del videojuego **Counter-Strike: Global Offensive (CSGO)**. Toda la información se almacena en base de datos y puede visualizarse desde una interfaz web. También se permite la **exportación de datos en formato XML**.

---

## 📦 Tecnologías utilizadas

- 🧰 **Ktor** (Servidor Web y Rutas)
- 🧩 **HTMX** (Frontend usando Server Side Rendering)
- 🏗️ **Exposed** (ORM de Kotlin) 
- 🔗 **Flyway** (Migraciones de base de datos)
- 🗃️ **PostgreSQL** (Base de datos relacional)
- 🐳 **Docker & Docker Compose**
- 📦 **Gradle** (Gestor de dependencias y construcción)
- 🧪 **JUnit + MockEngine** (Pruebas Unitarias)
- 📜 **Kotlinx Serialization** (Serialización JSON/XML)
- 🧠 **DDD** (Domain-Driven Design)

---

## ⚙️ Requisitos

- **JDK 17+**
- **Docker y Docker Compose**
- **Gradle**

---

## 🚀 Cómo levantar el proyecto

```bash
# 1. Clona el repositorio
git clone git@github.com:ErikssonHerlo/StarTrack-Backend-Dev-Test.git

# 2. Entra al proyecto
cd StarTrack-Backend-Dev-Test

# 3. Copia el archivo de entorno por defecto
cp .env.default .env.local

# 4. (Opcional) Cambia las credenciales para mayor seguridad
nano .env.local

# 5. Levanta todo el entorno con Docker Compose
docker compose up --build

# 6. Accede a la aplicación en:
http://localhost:8080
```

> Nota: El puerto utilizado para la base de datos PostgreSQL es `5435`, para evitar conflictos con puertos comunes como `5432`.

---

## 🧭 Rutas habilitadas

### 🌐 Frontend

| Ruta          | Descripción                          |
|---------------|--------------------------------------|
| `/`           | Página principal (home)              |
| `/skins`      | Lista de skins                       |
| `/agents`     | Lista de agentes                     |
| `/crates`     | Lista de cajas (crates)              |
| `/keys`       | Lista de llaves (requiere token)     |

> 💡 El frontend utiliza **HTMX** y **Templates HTML DSL** para renderizar dinámicamente la información.

---

## 📡 Endpoints API

| Endpoint                 | Método | Descripción                          |
|--------------------------|--------|--------------------------------------|
| `/api/v1/skins`          | GET    | Obtener todas las skins              |
| `/api/v1/agents`         | GET    | Obtener todos los agentes            |
| `/api/v1/crates`         | GET    | Obtener todas las cajas              |
| `/api/v1/keys`           | GET    | Obtener todas las llaves (privado)   |
| `/api/v1/skins/export/XML` | POST | Exportar skins a XML                 |
| `/api/v1/agents/export/XML`| POST | Exportar agentes a XML               |
| `/api/v1/crates/export/XML`| POST | Exportar cajas a XML                 |
| `/api/v1/keys/export/XML`  | POST | Exportar llaves a XML                |

🔐 Para acceder a `/api/v1/keys`, se necesita un token de autorización:
```
Authorization: Bearer Token
```

---

## 📥 Proceso de carga de datos

El sistema automáticamente cargará los datos de la API externa de CSGO la primera vez que se ejecute, **solo si la base de datos está vacía**.

Este proceso se realiza a través de los servicios:

- `CSGOService.loadSkinsData()`
- `CSGOService.loadAgentsData()`
- `CSGOService.loadCratesData()`
- `CSGOService.loadKeysData()`

---

## 🧪 Pruebas

El proyecto incluye pruebas unitarias utilizando **MockEngine** para simular los endpoints:

```bash
./gradlew test
```

Los tests están ubicados en:

```
src/test/kotlin/stsa/kotlin_htmx/
├── ApplicationTest.kt
└── DataHandlingTest.kt
```

---

## 🗂️ Estructura del Proyecto

El proyecto está organizado utilizando principios de **DDD (Domain-Driven Design)** y separa claramente cada capa:

```
├── api/              # Estructura de respuestas API
├── config/           # Configuración del sistema (puertos, DB, constantes)
├── domain/           # Modelos de dominio
├── external/         # DTOs y cliente para API externa
├── pages/            # Páginas HTMX del frontend
├── persistence/      # Entidades de base de datos con Exposed
├── plugins/          # Configuraciones de Ktor (HTTP, monitoreo)
├── repositories/     # Acceso a datos
├── routes/           # Definición de rutas API y frontend
├── services/         # Lógica de negocio
└── utils/            # Utilidades para conversión y transformación de datos
```

---

## 👤 Autor

**Eriksson Hernández**  
Full Stack Developer  
📧 erikssonhernandez25@gmail.com

---


