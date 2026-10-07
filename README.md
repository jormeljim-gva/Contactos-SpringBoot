# API REST - Gestión de Contactos (Spring Boot 3)

Este proyecto es una aplicación web backend desarrollada con **Spring Boot 3** y **Java 17** implementando una **Arquitectura en Capas (Layered Architecture)** limpia y mantenible.

---

## 📁 Estructura del Proyecto

```
contactos-springboot/
├── pom.xml                                  # Configuración de dependencias Maven
├── mvnw / mvnw.cmd                         # Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/com/ejemplo/contactos/
│   │   │   ├── ContactosApplication.java    # Clase principal Spring Boot
│   │   │   ├── config/                      # Configuración de Swagger y DataLoader
│   │   │   │   ├── OpenAPIConfig.java
│   │   │   │   └── DataLoader.java
│   │   │   ├── controller/                  # Endpoints REST (Controladores)
│   │   │   │   └── ContactoController.java
│   │   │   ├── dto/                         # Data Transfer Objects & Validaciones
│   │   │   │   ├── ContactoDTO.java
│   │   │   │   ├── CrearContactoDTO.java
│   │   │   │   └── ActualizarContactoDTO.java
│   │   │   ├── exception/                   # Manejo global de errores
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── BadRequestException.java
│   │   │   │   └── ErrorDetails.java
│   │   │   ├── model/                       # Entidades JPA (Persistencia)
│   │   │   │   └── Contacto.java
│   │   │   ├── repository/                  # Repositorios Spring Data JPA
│   │   │   │   └── ContactoRepository.java
│   │   │   └── service/                     # Lógica de negocio (Servicios)
│   │   │       ├── ContactoService.java
│   │   │       └── impl/ContactoServiceImpl.java
│   │   └── resources/
│   │       ├── application.properties       # Configuración global
│   │       └── application-dev.properties   # Configuración entorno de desarrollo (H2)
│   └── test/                                # Pruebas unitarias e integración
```

---

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Data JPA & Hibernate**
- **Base de Datos H2** (en memoria para desarrollo)
- **Spring Boot Starter Validation** (Bean Validation `@NotBlank`, `@Email`, etc.)
- **Lombok** (Generación de Getters, Setters, Builders y Constructors)
- **Springdoc OpenAPI (Swagger UI 2.6.0)** (Documentación interactiva de la API)

---

## 🚀 Instrucciones de Ejecución

### Requisitos previos
- JDK 17 o superior instalado.

### 1. Compilar y ejecutar
```bash
./mvnw clean spring-boot:run
```

### 2. Acceso a Documentación Interactiva (Swagger UI)
Una vez iniciada la aplicación, navega a:
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 3. Consola de Base de Datos H2
Accede a la consola H2 en:
👉 [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL:** `jdbc:h2:mem:contactosdb`
- **User:** `sa`
- **Password:** *(vacío)*

---

## 📡 Endpoints Disponibles (`/api/v1/contactos`)

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/v1/contactos` | Obtener lista paginada de contactos (`page`, `size`, `sortBy`, `sortDir`) |
| `GET` | `/api/v1/contactos/{id}` | Obtener un contacto por su ID |
| `GET` | `/api/v1/contactos/buscar?query={termino}` | Buscar por nombre, apellido o email |
| `POST` | `/api/v1/contactos` | Crear un nuevo contacto |
| `PUT` | `/api/v1/contactos/{id}` | Actualizar un contacto existente |
| `DELETE` | `/api/v1/contactos/{id}` | Eliminar un contacto |
