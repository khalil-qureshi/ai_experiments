# User Service

This project provides a Spring Boot based user service tailored for e-commerce platforms. It exposes RESTful endpoints for authentication, profile management, address book operations, password changes, and administrative user management. The service is backed by Spring Security with JWT authentication and uses an in-memory H2 database by default.

## Features

- User registration with strong password validation
- JWT-based login and stateless authentication
- Profile retrieval and updates for authenticated users
- Secure password change workflow
- Address management (add/remove shipping addresses)
- Administrative endpoint for listing users (requires `ADMIN` role)
- Automatic role bootstrapping and default admin account (`admin@ecommerce.local` / `ChangeMe123!`)
- H2 in-memory database with Spring Data JPA
- Health check endpoint exposed via Spring Boot Actuator

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

### API Overview

| Method | Endpoint | Description |
| ------ | -------- | ----------- |
| `POST` | `/api/auth/register` | Create a new customer account |
| `POST` | `/api/auth/login` | Authenticate and receive a JWT |
| `GET` | `/api/users/me` | Retrieve the current user's profile |
| `PUT` | `/api/users/me/profile` | Update profile details |
| `PUT` | `/api/users/me/password` | Change the current password |
| `POST` | `/api/users/me/addresses` | Add a new address |
| `DELETE` | `/api/users/me/addresses/{addressId}` | Delete an address |
| `GET` | `/api/users` | List all users (admin only) |

Authenticate subsequent requests by sending the `Authorization: Bearer <token>` header.

### Database & Console

The service uses an in-memory H2 database. Access the H2 console at `http://localhost:8080/h2-console` with the JDBC URL `jdbc:h2:mem:userdb` and credentials `sa` / `password`.

### Running Tests

```bash
mvn test
```

## Integration Tips

- Use the default admin account to seed your e-commerce platform and manage user roles.
- Extend the domain model by adding additional fields or relationships as needed. The project uses standard Spring Boot conventions to make customization straightforward.
- Replace the H2 datasource configuration in `application.yml` with your production-ready database (MySQL, PostgreSQL, etc.) before deployment.
