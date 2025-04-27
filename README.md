# Intuit-App

# API de Gestión de Clientes (con Spring Boot, JWT, Docker, Loki y Grafana)

Este proyecto es una API REST con Spring Boot para la gestión de clientes, incluyendo autenticación JWT, persistencia con H2/SQLite, y observabilidad básica con Loki y Grafana.

## Características

* Gestión de Clientes (CRUD).
* Autenticación JWT (Registro, Login, Refresh).
* Spring Security.
* Base de Datos H2 (en modo archivo) o SQLite.
* Documentación API (Swagger UI).
* Manejo Global de Excepciones.
* Observabilidad (Loki para logs, Grafana para visualización).
* Contenerización con Docker y Docker Compose.

## Tecnologías

* Spring Boot (Web, Security, Data JPA, Validation)
* H2 Database / SQLite
* JWT (JJWT)
* Lombok, ModelMapper
* Springdoc (Swagger/OpenAPI)
* Loki, Grafana
* Docker, Docker Compose

## Prerrequisitos

* Docker y Docker Compose
* JDK 17+ (si construyes manualmente)
* Maven o Gradle (si construyes manualmente)

## Configuración

* **Base de Datos:** Configurada vía `application.properties` (H2 en modo archivo `jdbc:h2:file:./intuit-db/intuitdb` o SQLite `jdbc:sqlite:file:./intuit-db/intuitdb`). La persistencia en Docker **requiere configurar un volumen** para el directorio `./intuit-db/`. Tu `docker-compose.yml` actual **no** tiene un volumen para la app Spring Boot, considera añadir uno.
* **JWT Secret:** La clave secreta JWT se configura mediante la variable de entorno `SECRET_KEY` en `docker-compose.yml`. **¡Cambia este valor por uno seguro en producción!**

## Ejecución con Docker Compose

1.  **Clona el Repositorio** (Si aplica).
2.  **Navega a la carpeta raíz:** Abre tu terminal en la carpeta donde se encuentra `docker-compose.yml`.
    ```bash
    cd /ruta/a/tu/proyecto/Intuit
    ```
    (Si el `docker-compose.yml` está en `D:\Intuit\Intuit`, usa `cd D:\Intuit\Intuit`)

3.  **Construye y levanta los servicios:** Este comando construye la imagen de la app y levanta todos los servicios definidos (`springboot-app`, `loki`, `grafana`).

    ```bash
    docker compose up --build -d
    ```

4.  **Verifica el estado:**
    ```bash
    docker compose ps
    ```
    Deberías ver los servicios `springboot-app`, `loki`, y `grafana` en estado `running`.

## Acceso a la API y Herramientas

Una vez que los contenedores estén corriendo:

* **API REST:** `http://localhost:8080/`
* **Documentación Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **Consola H2 Database:** `http://localhost:8080/h2-console`
    * **URL JDBC:** Usa la misma URL de tu `application.properties`, por ejemplo: `jdbc:h2:file:./intuit-db/intuitdb` (Nota: La ruta `./` es relativa al directorio de trabajo *dentro del contenedor*. Asegúrate de que tu volumen esté configurado para que esta ruta persista y sea accesible).
    * **Usuario/Contraseña:** Revisa tu `application.properties` para el usuario y contraseña configurados para H2 (comúnmente `sa` y vacío por defecto si no los cambiaste).
* **Grafana:** `http://localhost:3000/` (Configurado para acceso Admin anónimo y fuente de datos Loki).
* **Loki:** `http://localhost:3100/` (Normalmente interactúas con él vía Grafana).

## Detener y Limpiar

Para detener los servicios:

```bash
docker compose down