# Spring Boot Bank Customer Microservices

Spring Boot Bank Customer Microservices is a Java-based microservices project focused on customer registration in a banking domain.

The repository is structured around separate services for handling customer-related operations and storage responsibilities, following a microservices-oriented organization.

---

## Overview

This project implements a microservices-based backend structure for Banco JAVER, focused on customer registration.

The repository is organized into separate service directories, allowing the application responsibilities to be divided across independent components.

The visible project structure includes:

- `clienteAPI`
- `ClienteStorage`

This separation suggests a backend design where customer-facing API operations and customer storage responsibilities are handled in different services.

---

## Features

- Customer registration domain
- Java-based backend implementation
- Spring Boot microservices structure
- Separate service organization
- API-focused service structure
- Storage-focused service structure
- Banking-domain context

---

## Tech Stack

- **Java**
- **Spring Boot**
- **Microservices Architecture**
- **REST-oriented Backend Structure**

---

## Architecture

The repository follows a microservices-oriented structure with separate service directories.

### Service Responsibilities

- `clienteAPI`  
  Represents the API-facing service responsible for exposing customer-related operations.

- `ClienteStorage`  
  Represents the storage-focused service responsible for customer data handling responsibilities.

This structure helps separate application concerns and supports a modular backend design.

---

## Project Structure

```text
spring-boot-bank-customer-microservices/
├── ClienteStorage/
├── clienteAPI/
└── README.md
```

### Main Directories

- `clienteAPI/`  
  Contains the API service for customer-related operations.

- `ClienteStorage/`  
  Contains the storage service responsible for customer data handling.

- `README.md`  
  Project documentation.

---

## Installation

Clone the repository:

```bash
git clone https://github.com/lucaspc6/Banco-Javer-Microservices.git
```

Access the project directory:

```bash
cd Banco-Javer-Microservices
```

Each service should be accessed and executed from its respective directory.

Example:

```bash
cd clienteAPI
```

or:

```bash
cd ClienteStorage
```

---

## Running the Project

Because the repository is organized into separate services, each service should be started individually.

For a standard Spring Boot project, a service can usually be started with Maven using:

```bash
./mvnw spring-boot:run
```

or, if Maven is installed globally:

```bash
mvn spring-boot:run
```

If the project uses Gradle instead of Maven, use:

```bash
./gradlew bootRun
```

> Check each service directory for its specific build configuration before running the commands.

---

## API Endpoints

The repository is organized as a customer registration microservices project, but the available repository view does not expose verified endpoint documentation.

Recommended documentation to add:

```text
Method | Endpoint | Description
GET    | /clientes | List customers
POST   | /clientes | Register a customer
GET    | /clientes/{id} | Get customer by ID
PUT    | /clientes/{id} | Update customer
DELETE | /clientes/{id} | Delete customer
```

> Add only the endpoints that are actually implemented in the source code.

---

## Environment Variables

No verified environment variable documentation was available in the repository view.

If the services use database connections or service communication settings, document them here.

Example:

```env
SERVER_PORT=
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
```

> Keep sensitive values out of the repository and use environment-specific configuration files when needed.

---

## Testing

No verified automated test documentation was available in the repository view.

If tests are configured in the services, they can typically be executed with:

```bash
mvn test
```

or:

```bash
./mvnw test
```

---

## Screenshots

Screenshots are not required for backend services, but API documentation examples can improve the project presentation.

Recommended additions:

- Example request payload
- Example response payload
- Service communication diagram
- API testing screenshots from Postman or Insomnia

---

## Future Improvements

Potential improvements for this project include:

- Add endpoint documentation for each service
- Add request and response examples
- Add a microservices architecture diagram
- Document how `clienteAPI` communicates with `ClienteStorage`
- Add service-specific setup instructions
- Add database configuration instructions
- Add automated tests for customer registration flows
- Add Docker support for local execution
- Add a Postman or Insomnia collection
- Add CI workflow for build and test validation

---

## Author

**Lucas Carvalho**

GitHub: [@lucaspc6](https://github.com/lucaspc6/)
