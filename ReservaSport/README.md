# ReservaSport

## Descripción

ReservaSport es una plataforma desarrollada con arquitectura de microservicios para gestionar complejos deportivos, canchas, horarios, reservas, pagos, promociones, notificaciones, reseñas y reportes.

El sistema utiliza servicios independientes, bases de datos PostgreSQL separadas y comunicación REST entre microservicios.

## Integrantes

- Nombre: Javier Huamán
- Nombre: Ingrid Castllanos


**Número de equipo:5

## Arquitectura

El proyecto está compuesto por diez microservicios de negocio y un API Gateway como punto de entrada unificado.

## Microservicios

| Microservicio | Puerto | Base de datos | Responsabilidad |
|---|---:|---|---|
| usuarios-service | 8081 | usuarios_db | Gestión de usuarios |
| canchas-service | 8082 | canchas_db | Gestión de canchas |
| complejos-service | 8083 | complejos_db | Gestión de complejos deportivos |
| horarios-service | 8084 | horarios_db | Gestión de horarios y disponibilidad |
| reservas-service | 8085 | reservas_db | Gestión y validación de reservas |
| pagos-service | 8086 | pagos_db | Gestión de pagos |
| notificaciones-service | 8087 | notificaciones_db | Gestión de notificaciones |
| promociones-service | 8088 | promociones_db | Gestión de promociones y descuentos |
| resenas-service | 8089 | resenas_db | Gestión de reseñas y valoraciones |
| reportes-service | 8090 | reportes_db | Gestión y generación de reportes |
| gateway-service | 8080 | No utiliza | Enrutamiento centralizado |

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Spring Cloud OpenFeign
- Spring Cloud Gateway
- Swagger / OpenAPI
- Spring Validation
- Spring Boot DevTools
- Maven
- Lombok
- SLF4J
- Git
- IntelliJ IDEA
- Postman
- pgAdmin 4
- JUnit 5
- Mockito
- JaCoCo
- Docker
- Trello

## API Gateway

El API Gateway funciona en el puerto:

```text
http://localhost:8080

## Documentación de la API

Cada microservicio cuenta con su propia documentación generada mediante Swagger/OpenAPI.

- usuarios-service: http://localhost:8081/swagger-ui/index.html
- canchas-service: http://localhost:8082/swagger-ui/index.html
- complejos-service: http://localhost:8083/swagger-ui/index.html
- horarios-service: http://localhost:8084/swagger-ui/index.html
- reservas-service: http://localhost:8085/swagger-ui/index.html
- pagos-service: http://localhost:8086/swagger-ui/index.html
- notificaciones-service: http://localhost:8087/swagger-ui/index.html
- promociones-service: http://localhost:8088/swagger-ui/index.html
- resenas-service: http://localhost:8089/swagger-ui/index.html
- reportes-service: http://localhost:8090/swagger-ui/index.html

## Bases de datos

Cada microservicio utiliza una base de datos independiente en PostgreSQL.

- usuarios_db
- canchas_db
- complejos_db
- horarios_db
- reservas_db
- pagos_db
- notificaciones_db
- promociones_db
- resenas_db
- reportes_db

Usuario: postgres

Contraseña: postgres

## Ejecución del proyecto

Para ejecutar el sistema es necesario:

1. Iniciar PostgreSQL.
2. Crear las bases de datos correspondientes.
3. Abrir el proyecto en IntelliJ IDEA.
4. Actualizar las dependencias con Maven.
5. Ejecutar cada microservicio.
6. Iniciar el API Gateway.
7. Probar los servicios desde Swagger o mediante el Gateway.

## Orden de ejecución

Se recomienda iniciar los servicios en el siguiente orden:

1. usuarios-service
2. canchas-service
3. complejos-service
4. horarios-service
5. reservas-service
6. pagos-service
7. notificaciones-service
8. promociones-service
9. resenas-service
10. reportes-service
11. gateway-service

## Comunicación entre microservicios

La comunicación entre los servicios se realiza mediante OpenFeign.

Actualmente:

- reservas-service consulta información de usuarios-service, canchas-service y horarios-service para validar la creación de una reserva.

- pagos-service consulta reservas-service para obtener la información necesaria antes de registrar un pago.

## Estado del proyecto

Actualmente el proyecto cuenta con:

- Diez microservicios desarrollados e independientes.
- Persistencia de datos con PostgreSQL.
- Arquitectura basada en Controller - Service - Repository.
- Comunicación entre microservicios mediante OpenFeign.
- API Gateway para centralizar el acceso a los servicios.
- Documentación de los servicios con Swagger/OpenAPI.
- Manejo global de excepciones.
- Uso de DTO para las solicitudes y respuestas de la API.
- Integración con Spring Data JPA y Hibernate.
- Configuración mediante archivos `application.yml`.
- Registro de eventos mediante SLF4J.
- Proyecto gestionado con Maven.
- Pruebas unitarias implementadas con JUnit 5 y Mockito.
- Generación de cobertura de código mediante JaCoCo.
- Organización y seguimiento del desarrollo mediante Trello.
- Preparado para despliegue utilizando Docker.