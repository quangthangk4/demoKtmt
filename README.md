# Demo Application - Clean Architecture + DDD

Spring Boot application xây dựng theo **Clean Architecture**, **Domain-Driven Design (DDD)**, và **SOLID Principles**.

## Tổng quan

Project này demonstrate cách implement một module User hoàn chỉnh với:
- ✅ Clean Architecture (4 layers: Domain, Application, Infrastructure, Presentation)
- ✅ Domain-Driven Design patterns (Value Objects, Aggregates, Repositories)
- ✅ SOLID Principles được áp dụng nghiêm ngặt
- ✅ RESTful API với validation
- ✅ PostgreSQL với JPA/Hibernate
- ✅ Docker Compose cho database
- ✅ Global exception handling

## Tech Stack

- **Java 21**
- **Spring Boot 4.0.0**
  - Spring Data JPA
  - Spring Web MVC
  - Spring Validation
- **PostgreSQL 16**
- **Lombok**
- **Maven**
- **Docker & Docker Compose**

## Cấu trúc Project

```
src/main/java/com/ktmt/demoapplication/
├── DemoApplication.java           # Main application
└── user/                          # User Module
    ├── domain/                    # Domain Layer (Core Logic)
    │   ├── model/                # Domain Models
    │   │   ├── User.java        # Aggregate Root
    │   │   ├── UserId.java      # Value Object
    │   │   └── Email.java       # Value Object
    │   ├── repository/           # Repository Interfaces
    │   │   └── UserRepository.java
    │   └── service/              # Domain Services
    │       └── UserDomainService.java
    │
    ├── application/               # Application Layer (Use Cases)
    │   ├── usecase/
    │   │   ├── CreateUserUseCase.java
    │   │   ├── GetUserUseCase.java
    │   │   ├── UpdateUserUseCase.java
    │   │   └── DeleteUserUseCase.java
    │   └── dto/
    │       └── CreateUserRequest.java, etc.
    │
    ├── infrastructure/            # Infrastructure Layer
    │   ├── persistence/
    │   │   ├── entity/          # JPA Entities
    │   │   ├── mapper/          # Domain ↔ JPA Mapper
    │   │   └── repository/      # Repository Implementations
    │   └── config/              # JPA Config
    │
    └── presentation/              # Presentation Layer (API)
        ├── controller/           # REST Controllers
        └── dto/                  # API DTOs
```

## Quick Start

### 1. Clone & Setup

```bash
git clone <repository-url>
cd DemoApplication
```

### 2. Start Database với Docker

```bash
# Start PostgreSQL và pgAdmin
docker-compose up -d

# Verify
docker-compose ps
```

Access:
- PostgreSQL: `localhost:5432` (demoktmt / postgres / postgres)

### 3. Build & Run Application

```bash
# Build
./mvnw clean install

# Run
./mvnw spring-boot:run
```

Application sẽ chạy tại: http://localhost:8080

### 4. Test API

```bash
# Create user
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "age": 30
  }'

# Get all users
curl http://localhost:8080/api/v1/users
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/users` | Create new user |
| GET | `/api/v1/users` | Get all users |
| GET | `/api/v1/users?active=true` | Get active users only |
| GET | `/api/v1/users/{id}` | Get user by ID |
| PUT | `/api/v1/users/{id}` | Update user |
| DELETE | `/api/v1/users/{id}` | Soft delete (deactivate) |
| DELETE | `/api/v1/users/{id}/permanent` | Hard delete (remove) |

## Documentation

Xem chi tiết trong các file sau:

| File | Description |
|------|-------------|
| **[QUICK_START.md](QUICK_START.md)** | Hướng dẫn quick start và test API |
| **[USER_MODULE_SOLID_PRINCIPLES.md](USER_MODULE_SOLID_PRINCIPLES.md)** | Chi tiết về SOLID principles áp dụng trong code |

## SOLID Principles

Project này demonstrate đầy đủ 5 nguyên lý SOLID:

### Single Responsibility Principle (SRP)
Mỗi class có một trách nhiệm duy nhất:
- `UserId.java` - Chỉ quản lý user identifier
- `Email.java` - Chỉ validate email
- `CreateUserUseCase.java` - Chỉ handle create user logic

### Open/Closed Principle (OCP)
Open for extension, closed for modification:
- `User.java` - Có thể extend mà không modify
- `ApiResponse<T>` - Generic design cho bất kỳ response type

### Liskov Substitution Principle (LSP)
Implementations thay thế được interfaces:
- `UserRepositoryImpl` implements `UserRepository`
- Có thể swap implementations mà không phá vỡ behavior

### Interface Segregation Principle (ISP)
Interfaces tập trung, không bắt buộc implement không cần:
- `UserRepository` - Chỉ methods cần thiết cho User
- Controller inject specific use cases, không dùng "God Service"

### Dependency Inversion Principle (DIP)
Depend on abstractions, không depend on concrete classes:
- Domain layer định nghĩa interfaces
- Infrastructure layer implements interfaces
- High-level modules không depend on low-level modules

## Clean Architecture Layers

### 1. Domain Layer (Core)
- **Không phụ thuộc** vào bất kỳ layer nào
- Chứa business logic thuần túy
- Value Objects, Entities, Repository interfaces

### 2. Application Layer
- Orchestrate domain objects
- Use Cases (business workflows)
- Application DTOs

### 3. Infrastructure Layer
- Technical implementations
- Database, JPA entities
- Repository implementations
- External services

### 4. Presentation Layer
- User interface / API
- Controllers, API DTOs
- Request/Response handling

**Dependency Flow**: Presentation → Application → Domain ← Infrastructure

## DDD Patterns Implemented

- **Value Objects**: `UserId`, `Email` (immutable, self-validating)
- **Aggregate Root**: `User` (encapsulates business logic)
- **Repository Pattern**: Interface ở domain, implementation ở infrastructure
- **Domain Services**: `UserDomainService` (cross-entity business rules)
- **Factory Methods**: `User.create()`, `UserId.create()`

## Database Schema

```sql
CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    age INTEGER NOT NULL CHECK (age >= 0 AND age <= 150),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_user_email ON users(email);
```

## Development

### Build
```bash
./mvnw clean install
```

### Run Tests
```bash
./mvnw test
```

### Run Application
```bash
./mvnw spring-boot:run
```

## Docker Commands

```bash
# Start
docker-compose up -d

# Stop
docker-compose down

# View logs
docker-compose logs -f

# Reset database (WARNING: deletes data)
docker-compose down -v && docker-compose up -d

# Connect to PostgreSQL
docker exec -it demoktmt-postgres psql -U postgres -d demoktmt
```

## Configuration

### Database (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/demoktmt
    username: postgres
    password: postgres
```

### Change Port
```yaml
server:
  port: 8081  # Default: 8080
```

## Example Requests

### Create User
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "age": 30
  }'
```

### Response
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "firstName": "John",
    "lastName": "Doe",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "age": 30,
    "active": true,
    "createdAt": "2024-12-01T15:30:00",
    "updatedAt": "2024-12-01T15:30:00"
  }
}
```

## Validation Examples

### Invalid Email
```json
{
  "success": false,
  "message": "Validation failed: {email=Email must be valid}",
  "data": null
}
```

### Duplicate Email
```json
{
  "success": false,
  "message": "Email already exists: john.doe@example.com",
  "data": null
}
```

## Logging

Application logs:
- SQL queries (formatted)
- Request/Response
- Validation errors
- Business logic errors

Example:
```
2024-12-01 15:30:00 - Hibernate:
    insert into users
        (active, age, created_at, email, first_name, last_name, updated_at, id)
    values
        (?, ?, ?, ?, ?, ?, ?, ?)
```

## Best Practices Demonstrated

✅ Separation of Concerns
✅ Dependency Injection
✅ Constructor Injection (recommended over field injection)
✅ Immutable Value Objects
✅ Rich Domain Models
✅ Transaction Management
✅ Exception Handling
✅ Input Validation
✅ Database Indexing
✅ Connection Pooling (HikariCP)

## Why This Architecture?

### Benefits:
1. **Testability** - Mỗi layer test độc lập
2. **Maintainability** - Code dễ đọc, dễ sửa
3. **Flexibility** - Dễ thay đổi technical details
4. **Scalability** - Dễ mở rộng features
5. **Team Collaboration** - Clear boundaries

### Trade-offs:
- More files/classes (boilerplate)
- Steeper learning curve
- Over-engineering cho small projects

**Khi nào nên dùng?**
- Medium to large projects
- Team > 3 developers
- Long-term maintenance
- Complex business logic

**Khi nào KHÔNG nên dùng?**
- Prototypes, MVPs
- Simple CRUD apps
- Solo projects
- Tight deadlines

## Troubleshooting

### Port Already in Use
```bash
# Change port in application.yml
server:
  port: 8081
```

### Database Connection Failed
```bash
# Check if PostgreSQL running
docker-compose ps

# Restart database
docker-compose restart postgres
```

### Build Errors
```bash
# Clean rebuild
./mvnw clean install -U
```

## Contributing

1. Follow SOLID principles
2. Maintain Clean Architecture layers
3. Add tests for new features
4. Update documentation
5. Use meaningful commit messages

## License

This is a demo/educational project.

## Contact

For questions about implementation, refer to:
- `USER_MODULE_SOLID_PRINCIPLES.md` - SOLID principles details
- `DOCKER_GUIDE.md` - Docker usage
- `QUICK_START.md` - Getting started guide

---

**Happy Coding!** 🚀
