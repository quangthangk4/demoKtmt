# Quick Start Guide - User Module

## Yêu cầu
- Java 21
- PostgreSQL (hoặc Docker)
- Maven

## Bước 1: Setup Database

### Option 1: Sử dụng Docker (Recommended)

```bash
# Start PostgreSQL và pgAdmin
docker-compose up -d

# Verify containers are running
docker-compose ps

# View logs
docker-compose logs -f
```

**Xong!** Database đã sẵn sàng tại `localhost:5432`
- Database: `demoktmt`
- Username: `postgres`
- Password: `postgres`
- pgAdmin: http://localhost:5050 (admin@admin.com / admin)

Xem chi tiết tại `DOCKER_GUIDE.md`

### Option 2: Cài đặt PostgreSQL thủ công

```bash
# Login vào PostgreSQL
psql -U postgres

# Tạo database
CREATE DATABASE demoktmt;

# Hoặc chạy script
psql -U postgres -f setup-database.sql
```

## Bước 2: Cấu hình Database

File `src/main/resources/application.yml` đã được cấu hình sẵn:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/demoktmt
    username: postgres
    password: postgres
```

**Lưu ý**: Thay đổi username/password nếu cần

## Bước 3: Build & Run Application

### Build:
```bash
./mvnw clean install
```

### Run:
```bash
./mvnw spring-boot:run
```

Hoặc trên Windows:
```cmd
mvnw.cmd spring-boot:run
```

Application sẽ chạy tại: `http://localhost:8080`

## Bước 4: Test API

### 1. Create User
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

**Response:**
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

### 2. Get All Users
```bash
curl http://localhost:8080/api/v1/users
```

### 3. Get User by ID
```bash
curl http://localhost:8080/api/v1/users/{id}
```

### 4. Get Active Users Only
```bash
curl http://localhost:8080/api/v1/users?active=true
```

### 5. Update User
```bash
curl -X PUT http://localhost:8080/api/v1/users/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane.smith@example.com",
    "age": 25
  }'
```

### 6. Soft Delete (Deactivate)
```bash
curl -X DELETE http://localhost:8080/api/v1/users/{id}
```

### 7. Hard Delete (Permanent)
```bash
curl -X DELETE http://localhost:8080/api/v1/users/{id}/permanent
```

## Bước 5: Kiểm tra Database

```sql
-- Kết nối vào database
psql -U postgres -d user_db

-- Xem dữ liệu users
SELECT * FROM users;

-- Xem cấu trúc bảng
\d users
```

## Postman Collection

Nếu sử dụng Postman, import collection sau:

### Tạo Collection mới với các request:

1. **Create User** - POST `http://localhost:8080/api/v1/users`
   - Body: raw JSON
   ```json
   {
     "firstName": "John",
     "lastName": "Doe",
     "email": "john.doe@example.com",
     "age": 30
   }
   ```

2. **Get All Users** - GET `http://localhost:8080/api/v1/users`

3. **Get User by ID** - GET `http://localhost:8080/api/v1/users/{id}`

4. **Update User** - PUT `http://localhost:8080/api/v1/users/{id}`

5. **Delete User** - DELETE `http://localhost:8080/api/v1/users/{id}`

## Validation Examples

### Email không hợp lệ:
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "invalid-email",
    "age": 30
  }'
```

**Response:**
```json
{
  "success": false,
  "message": "Validation failed: {email=Email must be valid}",
  "data": null
}
```

### Age ngoài range:
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "age": 200
  }'
```

**Response:**
```json
{
  "success": false,
  "message": "Validation failed: {age=Age must not exceed 150}",
  "data": null
}
```

### Email đã tồn tại:
```bash
# Tạo user thứ 2 với cùng email
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "john.doe@example.com",
    "age": 25
  }'
```

**Response:**
```json
{
  "success": false,
  "message": "Email already exists: john.doe@example.com",
  "data": null
}
```

## Logs

Application logs sẽ hiển thị:
- SQL queries (với format đẹp)
- Request/Response details
- Validation errors
- Business logic errors

Example log output:
```
2024-12-01 15:30:00 - Hibernate:
    insert
    into
        users
        (active, age, created_at, email, first_name, last_name, updated_at, id)
    values
        (?, ?, ?, ?, ?, ?, ?, ?)
```

## Troubleshooting

### Database Connection Error
```
Error: could not connect to server: Connection refused
```
**Solution**: Đảm bảo PostgreSQL đang chạy
```bash
# Start PostgreSQL service
sudo service postgresql start
```

### Port Already in Use
```
Error: Web server failed to start. Port 8080 was already in use.
```
**Solution**: Đổi port trong `application.yml`:
```yaml
server:
  port: 8081
```

### Compilation Errors
```bash
# Clean và rebuild
./mvnw clean install -U
```

## Next Steps

1. Đọc chi tiết về SOLID principles trong `USER_MODULE_SOLID_PRINCIPLES.md`
2. Explore cấu trúc code để hiểu Clean Architecture
3. Thử modify và extend module theo nhu cầu của bạn

## Support

Nếu gặp vấn đề, check:
- Database đã được tạo chưa
- Username/password đúng chưa
- PostgreSQL service đang chạy chưa
- Port 8080 có bị chiếm không
