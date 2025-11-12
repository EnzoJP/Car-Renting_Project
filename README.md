# Car-Renting-App-Project

A client-server Spring Boot application for car rentals. This repository contains a backend server and two separate web clients (administrative and promotional) that demonstrate a RESTful API, file uploads, and typical rental workflows.

## Project structure

- `Servidor/servidor` — Spring Boot backend that exposes REST APIs, handles authentication, stores uploaded documents, and manages business logic for cars, rentals and users.
- `Cliente Administrativo/clienteAdmin` — Administrative web client for staff to manage cars, view rentals, approve documents and perform CRUD operations.
- `Cliente Promocional/clientePromo` — Promotional/public-facing web client for customers to browse cars, make rental requests, pay and upload required documents.
- `Documentation/` — Diagrams and the project specification PDF.

## Key features

- RESTful API implemented with Spring Boot
- User and role management (admin and customer flows)
- Car inventory management and rental lifecycle
- File upload and storage for customer documents
- Separate frontend clients for administrative and promotional use
- deployed in a real hosting service

## Technologies

- Java, Spring Boot 
- Thymeleaf 
- Maven build system
- docker
- Mercado Pago
- Bootstrap
- JasperReports
- MySQL

## How to run

Each module includes a Maven wrapper. From the module folder run (Windows PowerShell):

- Server:
  - cd `Servidor/servidor`
  - ./mvnw.cmd spring-boot:run

- Admin client:
  - cd `Cliente Administrativo/clienteAdmin`
  - ./mvnw.cmd spring-boot:run

- Promo client:
  - cd `Cliente Promocional/clientePromo`
  - ./mvnw.cmd spring-boot:run

Configuration is read from `src/main/resources/application.properties` in each module. Adjust ports and datasource settings there if needed.


