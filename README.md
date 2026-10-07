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


## 💡 ¿Cómo funciona Spring Boot? (Guía para principiantes)

Si es tu primera vez trabajando con **Spring Boot**, piensa en él como un marco de trabajo (*framework*) que facilita enormemente la creación de aplicaciones en Java. En lugar de configurar manualmente servidores, conexiones a bases de datos o rutas HTTP, Spring Boot se encarga de casi todo por ti de manera automática.

### 1. El Concepto Principal: Inyección de Dependencias (IoC)
Spring actúa como un "administrador de objetos". En lugar de que tú crees instancias manualmente con `new MiClase()`, Spring crea y gestiona los objetos por ti (llamados **Beans**) y los "inyecta" automáticamente donde se necesiten mediante anotaciones como `@Autowired` o a través de constructores.

### 2. Flujo de una Petición HTTP (Arquitectura en Capas)

Cuando un usuario interactúa con la aplicación, la información fluye secuencialmente paso a paso:

1. **Cliente / Frontend**
   - Envía la solicitud HTTP (GET, POST, PUT, DELETE) con los datos del usuario.
   
2. **Controller (`/controller`)**
   - Recibe y atiende la petición HTTP.
   - Valida la estructura y formato de los datos recibidos mediante DTOs.
   
3. **Service (`/service`)**
   - Contiene la lógica principal del negocio.
   - Aplica las reglas del sistema, procesa los datos y realiza validaciones complejas.
   
4. **Repository (`/repository`)**
   - Gestiona la comunicación con la base de datos a través de consultas JPA.
   
5. **Base de Datos**
   - Almacena o recupera la información solicitada.

### 3. Componentes clave del proyecto:
- **Controller (`/controller`)**: Es la puerta de entrada. Recibe la petición web, extrae los parámetros y delega el trabajo al servicio.
- **Service (`/service`)**: Es el "cerebro" de la aplicación. Aquí residen la lógica de negocio y las reglas específicas del sistema.
- **Repository (`/repository`)**: Es la capa de persistencia. Spring Data JPA permite hacer operaciones CRUD (Crear, Leer, Actualizar, Borrar) en la base de datos sin escribir SQL manualmente.
- **Model/Entity (`/model`)**: Son clases Java simples que representan directamente las tablas de la base de datos (por ejemplo, la clase `Contacto` mapea a la tabla `contacto`).
- **DTO (`/dto`)**: *Data Transfer Object*. Sirven para definir qué datos se reciben y se envían hacia afuera, evitando exponer las entidades internas directamente.