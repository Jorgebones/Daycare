🍼 Daycare Management System – API REST (Java + Spring Boot)

API REST diseñada para la gestión de una guardería, con enfoque en seguridad, arquitectura limpia, y buenas prácticas profesionales.
Incluye autenticación basada en JWT, control de roles (ADMIN/TEACHER), CRUDs seguros, manejo de excepciones y persistencia con H2 para desarrollo.

🚀 Tecnologías utilizadas

Java 21
Spring Boot 3.5
Spring Security 6 + JWT
Spring Data JPA
H2 Database
Maven
Lombok

🔐 Seguridad y Autenticación

El sistema implementa:

Autenticación con JSON Web Tokens (JWT)

Roles: ADMIN y TEACHER

Filtro personalizado con OncePerRequestFilter

Encriptación de contraseñas con BCrypt

Rutas protegidas según el rol asignado

Manejador global de excepciones (@ControllerAdvice)

📁 Arquitectura

Controllers – Endpoints REST

Services – Lógica de negocio

Repositories – Acceso a datos

DTOs – Transferencia segura de datos

Security – Configuración JWT + filtros + UserDetails

Exception Handling – Respuestas claras y estructuradas

🧪 Base de datos (H2)

El proyecto incluye una configuración H2 para desarrollo y pruebas.
La consola H2 puede habilitarse para inspeccionar tablas en tiempo real.

🔄 Flujo de trabajo (Git)

El repositorio sigue un flujo simple:

Crear rama nueva

Commit & push

Pull Request → merge a main

Ejemplo:

git checkout -b feature/security
git add .
git commit -m "Implement JWT authentication"
git push origin feature/security

🛠 Cómo ejecutar el proyecto
mvn spring-boot:run


El backend arrancará en:

http://localhost:8080

🧸 Sobre el proyecto

Este sistema fue creado con fines educativos y para demostrar habilidades en:

Desarrollo backend con Java

Spring Security avanzado

Arquitectura limpia y mantenible

Implementación de autenticación robusta

Buenas prácticas profesionales en proyectos reales

Kisss me pretty prince