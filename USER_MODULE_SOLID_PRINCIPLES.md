# User Module - Clean Architecture + DDD + SOLID Principles

## Tổng quan

Module User được xây dựng theo **Clean Architecture** và **Domain-Driven Design (DDD)** với việc tuân thủ nghiêm ngặt các nguyên lý **SOLID**. Module cung cấp các API cơ bản để quản lý người dùng (CRUD operations).

## Cấu trúc Clean Architecture

```
user/
├── domain/                    # Core Business Logic (không phụ thuộc vào framework)
│   ├── model/                # Domain Models
│   ├── repository/           # Repository Interfaces (Ports)
│   └── service/              # Domain Services
├── application/               # Use Cases (Business Logic Orchestration)
│   ├── usecase/              # Use Case Implementations
│   └── dto/                  # Application Data Transfer Objects
├── infrastructure/            # Technical Implementation (Adapters)
│   ├── persistence/          # Database Implementation
│   │   ├── entity/           # JPA Entities
│   │   ├── mapper/           # Domain ↔ JPA Mappers
│   │   └── repository/       # Repository Implementations
│   └── config/               # Infrastructure Configuration
└── presentation/              # API Layer
    ├── controller/           # REST Controllers
    └── dto/                  # API Request/Response DTOs
```

---

## SOLID Principles - Chi tiết từng file

### 1. Single Responsibility Principle (SRP)
> Một class chỉ nên có một lý do để thay đổi

#### ✅ Áp dụng trong:

**`UserId.java`** (domain/model/UserId.java:1)
- **Trách nhiệm duy nhất**: Đại diện cho định danh người dùng
- **Lý do thay đổi**: Chỉ thay đổi khi logic định danh người dùng thay đổi
```java
public class UserId {
    private final UUID value;
    // Chỉ quản lý logic liên quan đến User ID
}
```

**`Email.java`** (domain/model/Email.java:1)
- **Trách nhiệm duy nhất**: Validate và đại diện cho email
- **Lý do thay đổi**: Chỉ thay đổi khi quy tắc validate email thay đổi
```java
public class Email {
    private static final Pattern EMAIL_PATTERN = ...;
    // Chỉ quản lý logic email validation
}
```

**`User.java`** (domain/model/User.java:1)
- **Trách nhiệm duy nhất**: Quản lý business logic của User entity
- **Lý do thay đổi**: Chỉ thay đổi khi business rules của User thay đổi
```java
public class User {
    // Encapsulates user business logic
    public void updateInformation(...) { }
    public void deactivate() { }
    public void activate() { }
}
```

**`CreateUserUseCase.java`** (application/usecase/CreateUserUseCase.java:1)
- **Trách nhiệm duy nhất**: Xử lý logic tạo user
- **Lý do thay đổi**: Chỉ thay đổi khi flow tạo user thay đổi
```java
@Service
public class CreateUserUseCase {
    public UserResponse execute(CreateUserRequest request) {
        // Only handles user creation orchestration
    }
}
```

**`UserMapper.java`** (infrastructure/persistence/mapper/UserMapper.java:1)
- **Trách nhiệm duy nhất**: Chuyển đổi giữa Domain Model và JPA Entity
- **Lý do thay đổi**: Chỉ thay đổi khi cấu trúc mapping thay đổi

**`UserController.java`** (presentation/controller/UserController.java:1)
- **Trách nhiệm duy nhất**: Xử lý HTTP requests/responses
- **Lý do thay đổi**: Chỉ thay đổi khi API contract thay đổi

---

### 2. Open/Closed Principle (OCP)
> Open for extension, closed for modification

#### ✅ Áp dụng trong:

**`User.java`** (domain/model/User.java:1)
- **Open for extension**: Có thể kế thừa để tạo các loại User đặc biệt
- **Closed for modification**: Không cần sửa code hiện tại khi thêm tính năng mới
```java
public class User {
    // Core business logic is stable
    // Can be extended through inheritance without modification
}
```

**`UserResponse.java`** (application/dto/UserResponse.java:1)
```java
public record UserResponse(...) {
    // Can add new factory methods without modifying existing code
    public static UserResponse from(User user) { }
}
```

**`ApiResponse.java`** (presentation/dto/ApiResponse.java:1)
- **Open for extension**: Generic type cho phép sử dụng với bất kỳ response type nào
- **Closed for modification**: Không cần sửa class khi thêm response type mới
```java
public record ApiResponse<T>(...) {
    // Generic design allows extension without modification
}
```

**`UserController.java`** (presentation/controller/UserController.java:1)
- **Open for extension**: Có thể thêm endpoint mới
- **Closed for modification**: Các endpoint hiện tại không bị ảnh hưởng
```java
@RestController
public class UserController {
    // Can add new endpoints without modifying existing ones
}
```

---

### 3. Liskov Substitution Principle (LSP)
> Subclasses should be substitutable for their base classes

#### ✅ Áp dụng trong:

**`UserRepositoryImpl.java`** (infrastructure/persistence/repository/UserRepositoryImpl.java:1)
- **Thay thế được**: `UserRepositoryImpl` có thể thay thế `UserRepository` interface mà không phá vỡ hành vi
- **Không vi phạm contract**: Tất cả các phương thức implement đều tuân thủ contract của interface
```java
@Repository
public class UserRepositoryImpl implements UserRepository {
    // Correctly implements all interface methods
    // Can be substituted wherever UserRepository is used
    @Override
    public User save(User user) { }
    @Override
    public Optional<User> findById(UserId id) { }
}
```

**Ví dụ sử dụng:**
```java
// Application layer chỉ depend on interface
public class CreateUserUseCase {
    private final UserRepository userRepository; // Interface

    // Works with any implementation (UserRepositoryImpl, MockUserRepository, etc.)
    public UserResponse execute(CreateUserRequest request) {
        User savedUser = userRepository.save(user); // LSP in action
    }
}
```

---

### 4. Interface Segregation Principle (ISP)
> Clients should not be forced to depend on interfaces they don't use

#### ✅ Áp dụng trong:

**`UserRepository.java`** (domain/repository/UserRepository.java:1)
- **Interface tập trung**: Chỉ chứa các methods cần thiết cho User persistence
- **Không bắt buộc implement methods không cần**: Mỗi method đều có mục đích rõ ràng
```java
public interface UserRepository {
    // Focused interface with only necessary methods
    User save(User user);
    Optional<User> findById(UserId id);
    Optional<User> findByEmail(Email email);
    List<User> findAll();
    // ... only user-related persistence methods
}
```

**`UserController.java`** (presentation/controller/UserController.java:1)
- **Depend on specific use cases**: Controller không depend vào một "God Service" lớn
- **Mỗi use case có interface riêng**: Chỉ inject các use case cần thiết
```java
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    // Uses specific use cases instead of one large service
    // Each controller method depends only on what it needs
}
```

**`UserJpaRepository.java`** (infrastructure/persistence/repository/UserJpaRepository.java:1)
- **Chỉ extend interfaces cần thiết**: Extend `JpaRepository` với các methods phù hợp
```java
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    // Only declares additional methods specific to User
    Optional<UserJpaEntity> findByEmail(String email);
    List<UserJpaEntity> findAllActive();
}
```

---

### 5. Dependency Inversion Principle (DIP)
> High-level modules should not depend on low-level modules. Both should depend on abstractions.

#### ✅ Áp dụng trong:

**Domain Layer không phụ thuộc vào Infrastructure:**

**`UserRepository.java`** (domain/repository/UserRepository.java:1)
- **Abstraction ở Domain Layer**: Interface được định nghĩa trong domain
- **Implementation ở Infrastructure Layer**: Concrete class ở infrastructure
```java
// domain/repository/UserRepository.java
public interface UserRepository {
    User save(User user);
    // Domain defines the contract
}

// infrastructure/persistence/repository/UserRepositoryImpl.java
@Repository
public class UserRepositoryImpl implements UserRepository {
    // Infrastructure implements the contract
}
```

**Dependency Flow:**
```
Presentation Layer (Controller)
    ↓ depends on
Application Layer (Use Cases)
    ↓ depends on
Domain Layer (Interfaces)
    ↑ implemented by
Infrastructure Layer (Implementations)
```

**`CreateUserUseCase.java`** (application/usecase/CreateUserUseCase.java:1)
```java
@Service
public class CreateUserUseCase {
    private final UserRepository userRepository; // Depends on abstraction
    private final UserDomainService userDomainService; // Depends on abstraction

    // Constructor injection with abstractions
    public CreateUserUseCase(
            UserRepository userRepository, // Interface, not implementation
            UserDomainService userDomainService) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
    }
}
```

**`UserController.java`** (presentation/controller/UserController.java:1)
```java
@RestController
public class UserController {
    // Depends on use case abstractions, not concrete implementations
    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;

    // High-level module depends on abstractions
    public UserController(
            CreateUserUseCase createUserUseCase,
            GetUserUseCase getUserUseCase,
            ...) {
        // Dependencies injected through constructor
    }
}
```

**`UserDomainService.java`** (domain/service/UserDomainService.java:1)
```java
@Service
public class UserDomainService {
    private final UserRepository userRepository; // Depends on interface

    // Domain service depends on repository abstraction
    public void ensureEmailIsUnique(Email email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already exists");
        }
    }
}
```

---

## DDD Patterns

### 1. Value Objects
**`UserId.java`**, **`Email.java`**
- Immutable
- Self-validating
- Equality based on value, not identity

### 2. Aggregate Root
**`User.java`**
- Encapsulates business logic
- Controls access to internal state
- Factory methods for creation

### 3. Repository Pattern
**`UserRepository.java`** (Interface) + **`UserRepositoryImpl.java`** (Implementation)
- Abstracts persistence logic
- Domain-focused interface
- Infrastructure implementation

### 4. Domain Services
**`UserDomainService.java`**
- Contains domain logic that doesn't fit in a single entity
- Stateless operations
- Works with multiple entities

### 5. Use Cases (Application Services)
**`CreateUserUseCase.java`**, **`GetUserUseCase.java`**, etc.
- Orchestrates domain objects
- Transaction boundaries
- Converts between DTOs and domain models

---

## Dependency Flow

```
┌─────────────────────────────────────────┐
│      Presentation Layer                 │
│  (Controllers, API DTOs)                │
│  - UserController                       │
│  - CreateUserApiRequest                 │
└──────────────┬──────────────────────────┘
               │ depends on
               ↓
┌─────────────────────────────────────────┐
│      Application Layer                  │
│  (Use Cases, Application DTOs)          │
│  - CreateUserUseCase                    │
│  - CreateUserRequest                    │
└──────────────┬──────────────────────────┘
               │ depends on
               ↓
┌─────────────────────────────────────────┐
│      Domain Layer                       │
│  (Entities, Value Objects, Interfaces)  │
│  - User (Aggregate Root)                │
│  - UserId, Email (Value Objects)        │
│  - UserRepository (Interface)           │
└──────────────△──────────────────────────┘
               │ implemented by
               ↑
┌─────────────────────────────────────────┐
│      Infrastructure Layer               │
│  (JPA, Database, Implementations)       │
│  - UserRepositoryImpl                   │
│  - UserJpaEntity                        │
│  - UserMapper                           │
└─────────────────────────────────────────┘
```

**Key Point**: Domain Layer không phụ thuộc vào bất kỳ layer nào (DIP)

---

## API Endpoints

### 1. Create User
```http
POST /api/v1/users
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "age": 30
}
```

### 2. Get User by ID
```http
GET /api/v1/users/{id}
```

### 3. Get All Users
```http
GET /api/v1/users
GET /api/v1/users?active=true
```

### 4. Update User
```http
PUT /api/v1/users/{id}
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@example.com",
  "age": 31
}
```

### 5. Soft Delete User
```http
DELETE /api/v1/users/{id}
```

### 6. Hard Delete User
```http
DELETE /api/v1/users/{id}/permanent
```

---

## Configuration

### Database Setup (application.yml:1)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/user_db
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
```

Trước khi chạy application, tạo database:
```sql
CREATE DATABASE user_db;
```

---

## Testing với cURL

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

### Get All Users
```bash
curl http://localhost:8080/api/v1/users
```

### Get User by ID
```bash
curl http://localhost:8080/api/v1/users/{user-id}
```

---

## Lợi ích của Clean Architecture + SOLID

### 1. **Testability**
- Mỗi layer có thể test độc lập
- Domain logic có thể test mà không cần database
- Use cases có thể test với mock repositories

### 2. **Maintainability**
- Mỗi class có responsibility rõ ràng (SRP)
- Dễ dàng tìm và sửa bugs
- Code dễ đọc và hiểu

### 3. **Flexibility**
- Dễ dàng thay đổi database (chỉ sửa Infrastructure layer)
- Dễ dàng thay đổi API structure (chỉ sửa Presentation layer)
- Business logic không bị ảnh hưởng bởi thay đổi technical

### 4. **Scalability**
- Có thể mở rộng từng layer độc lập
- Thêm features mới không ảnh hưởng code cũ (OCP)
- Có thể thay thế implementations dễ dàng (LSP, DIP)

### 5. **Team Collaboration**
- Nhiều developers có thể làm việc trên các layers khác nhau
- Ít conflicts khi merge code
- Clear boundaries giữa các concerns

---

## Kết luận

Module User này là ví dụ hoàn chỉnh về cách áp dụng:
- ✅ **Clean Architecture** với 4 layers rõ ràng
- ✅ **Domain-Driven Design** với Value Objects, Aggregates, Repositories
- ✅ **SOLID Principles** được tuân thủ nghiêm ngặt ở mọi class
- ✅ **Separation of Concerns** giữa business logic và technical details
- ✅ **Testability** và **Maintainability** cao

Code này có thể serve như một template/reference cho các modules khác trong dự án.
