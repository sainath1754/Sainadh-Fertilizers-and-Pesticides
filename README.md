# Sainadh Fertilizers & Pesticides

A full-stack agriculture e-commerce platform built with **Spring Boot 3.2.5**, **Spring Security**, **Spring Data JPA**, **Redis**, and **JWT authentication**.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 3.2.5 |
| Web | Spring MVC, Thymeleaf |
| Security | Spring Security 6, JWT (jjwt 0.12.5), BCrypt |
| Persistence | Spring Data JPA, Hibernate 6.4, MySQL 8.x |
| Caching | Spring Cache Abstraction, Redis |
| Validation | Jakarta Bean Validation |
| Build | Maven (wrapper included) |
| Utilities | Lombok 1.18.46, Spring DevTools |

---

## Architecture

The application follows a layered **MVC architecture**:

```
┌──────────────────────────────────────────────────────┐
│                    Client (Browser)                  │
└──────────────────┬───────────────────────────────────┘
                   │  HTTP / HTTPS
┌──────────────────▼───────────────────────────────────┐
│              Spring Security Filter Chain            │
│         (JwtAuthFilter → FormLogin → CSRF)           │
└──────────────────┬───────────────────────────────────┘
                   │
┌──────────────────▼───────────────────────────────────┐
│                   Controller Layer                   │
│  AuthController │ HomeController │ StoreController   │
│  CartController │ ApiAuthController │ AboutController│
└──────────────────┬───────────────────────────────────┘
                   │
┌──────────────────▼───────────────────────────────────┐
│                    Service Layer                     │
│  UserService │ ProductService │ CartService          │
│  OrderService │ CustomUserDetailsService             │
└─────────┬────────────────────────────┬───────────────┘
          │                            │
┌─────────▼──────────┐   ┌────────────▼───────────────┐
│  Repository Layer  │   │      Redis Cache Layer     │
│  (Spring Data JPA) │   │  (Product & Cart caching)  │
└─────────┬──────────┘   └────────────────────────────┘
          │
┌─────────▼──────────┐
│     MySQL 8.x      │
└────────────────────┘
```

---

## Security

### Authentication Flow

The application uses a **dual authentication** strategy:

1. **Session-Based (Web UI)** — Spring Security form login with CSRF protection. On successful login, a JWT is also issued as an `HttpOnly` cookie.
2. **JWT-Based (REST API)** — Stateless token authentication for the `/api/**` endpoints. Tokens are validated via `JwtAuthFilter` registered before `UsernamePasswordAuthenticationFilter`.

### Security Configuration

| Feature | Detail |
|---|---|
| Password Encoding | BCrypt (strength 12) |
| CSRF | Enabled (Thymeleaf auto-injects tokens) |
| Session Policy | Max 1 concurrent session per user |
| Session Timeout | 30 minutes |
| Account Lockout | Locks after 5 consecutive failed login attempts |
| JWT Expiration | 24 hours |
| JWT Cookie | `HttpOnly`, path `/` |

### REST API Authentication

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Authenticate and receive a JWT |

JWT tokens are signed with HMAC-SHA256 and must be included in the `Authorization: Bearer <token>` header for protected API endpoints.

---

## Caching (Redis)

Redis is used as a caching layer to reduce database load on frequently accessed data.

| Configuration | Value |
|---|---|
| Cache Provider | Redis |
| TTL | 5 minutes (300,000 ms) |
| Null Caching | Disabled |
| Fallback | Graceful — application starts without Redis using `RedisFallbackConfig` |

**Cached operations:** Product catalog queries, cart count lookups.

If Redis is unavailable at startup, the application falls back to a no-op cache configuration and continues to serve requests directly from MySQL.

---

## Database Schema

The application uses `spring.jpa.hibernate.ddl-auto=update` — tables are auto-created/updated on startup.

```
┌─────────────┐       ┌──────────────┐
│    users     │       │   products   │
├─────────────┤       ├──────────────┤
│ id (PK)     │       │ id (PK)      │
│ username    │       │ name         │
│ email       │       │ description  │
│ password    │       │ price        │
│ role        │       │ category     │
│ locked      │       │ image_url    │
│ fail_count  │       └──────┬───────┘
└──────┬──────┘              │
       │                     │
       │    ┌────────────────┤
       │    │                │
┌──────▼────▼──┐    ┌───────▼────────┐
│  cart_items   │    │  order_items   │
├──────────────┤    ├────────────────┤
│ id (PK)      │    │ id (PK)        │
│ user_id (FK) │    │ order_id (FK)  │
│ product_id   │    │ product_id(FK) │
│ quantity     │    │ quantity       │
└──────────────┘    │ price          │
                    └───────┬────────┘
                            │
                    ┌───────▼────────┐
                    │    orders      │
                    ├────────────────┤
                    │ id (PK)        │
                    │ user_id (FK)   │
                    │ total_amount   │
                    │ order_date     │
                    │ status         │
                    └────────────────┘
```

---

## Project Structure

```
src/main/java/com/sainadh/fertilizers/
├── FertilizersApplication.java             # Entry point
├── config/
│   ├── SecurityConfig.java                 # Spring Security + filter chain
│   ├── JwtUtil.java                        # JWT generation & validation
│   ├── JwtAuthFilter.java                  # OncePerRequestFilter for JWT
│   ├── RedisConfig.java                    # Redis cache manager setup
│   └── RedisFallbackConfig.java            # Graceful fallback if Redis unavailable
├── controller/
│   ├── AuthController.java                 # /login, /register (web)
│   ├── ApiAuthController.java              # /api/auth/** (REST)
│   ├── HomeController.java                 # /home
│   ├── StoreController.java                # /store, /store/add-to-cart
│   ├── CartController.java                 # /cart, /cart/checkout
│   ├── ChatbotController.java              # /chatbot
│   └── AboutController.java               # /about
├── model/
│   ├── User.java                           # @Entity — users table
│   ├── Product.java                        # @Entity — products table
│   ├── CartItem.java                       # @Entity — cart_items table
│   ├── OrderRecord.java                    # @Entity — orders table
│   └── OrderItem.java                      # @Entity — order_items table
├── repository/
│   ├── UserRepository.java                 # JpaRepository<User, Long>
│   ├── ProductRepository.java              # JpaRepository<Product, Long>
│   ├── CartItemRepository.java             # JpaRepository<CartItem, Long>
│   ├── OrderRepository.java                # JpaRepository<OrderRecord, Long>
│   └── OrderItemRepository.java            # JpaRepository<OrderItem, Long>
├── service/
│   ├── UserService.java                    # Registration, validation
│   ├── ProductService.java                 # CRUD + cache integration
│   ├── CartService.java                    # Add/remove/update cart items
│   ├── OrderService.java                   # Checkout + order persistence
│   ├── CustomUserDetailsService.java       # UserDetailsService impl + lockout
│   └── LoginAuditService.java              # Login audit interface
└── init/
    └── DataInitializer.java                # Seeds admin user + 12 products

src/main/resources/
├── application.properties                  # All configuration
├── static/css/style.css                    # Stylesheet
└── templates/                              # Thymeleaf views
    ├── fragments/layout.html               # Shared navbar & footer
    ├── login.html
    ├── register.html
    ├── home.html
    ├── store.html
    ├── cart.html
    ├── chatbot.html
    └── about.html
```

---

## Setup & Run

### Prerequisites

- **Java 25** (JDK 25.0.1+ recommended)
- **MySQL 8.x**
- **Redis** (optional — app works without it)
- **Maven 3.8+** (or use the included `mvnw` wrapper)

### 1. Create the Database

```sql
CREATE DATABASE IF NOT EXISTS sainadh_fertilizers;
```

> Tables are auto-created by Hibernate on first startup (`ddl-auto=update`).

### 2. Configure Credentials

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sainadh_fertilizers?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=<your_password>
```

### 3. Start Redis (Optional)

```bash
redis-server
```

If Redis is not running, the app logs a warning and falls back to direct DB queries.

### 4. Build & Run the Application

```bash
# Clean build (recommended after dependency changes)
# Windows
mvnw.cmd clean compile
# Linux / macOS
./mvnw clean compile

# Run the application
# Windows
mvnw.cmd spring-boot:run
# Linux / macOS
./mvnw spring-boot:run

# Or with Maven installed globally
mvn spring-boot:run
```

### 5. Access

| URL | Description |
|---|---|
| `http://localhost:8080` | Web application |
| `http://localhost:8080/api/auth/login` | REST API login |

### Default Admin Credentials

| Field | Value |
|---|---|
| Username | `admin` |
| Password | `Sainadh@1754` |

---

## Key Dependencies

```xml
spring-boot-starter-web
spring-boot-starter-security
spring-boot-starter-data-jpa
spring-boot-starter-data-redis
spring-boot-starter-cache
spring-boot-starter-validation
spring-boot-starter-thymeleaf
thymeleaf-extras-springsecurity6
mysql-connector-j
jjwt-api / jjwt-impl / jjwt-jackson  (0.12.5)
lombok                                (1.18.46)
spring-boot-devtools
```

---

## Configuration Reference

| Property | Value | Purpose |
|---|---|---|
| `server.port` | `8080` | Application port |
| `spring.jpa.hibernate.ddl-auto` | `update` | Auto schema migration |
| `spring.jpa.show-sql` | `true` | Log SQL statements |
| `spring.data.redis.host` | `localhost` | Redis host |
| `spring.data.redis.port` | `6379` | Redis port |
| `spring.cache.redis.time-to-live` | `300000` | Cache TTL (5 min) |
| `app.jwt.expiration-ms` | `86400000` | JWT token lifetime (24h) |
| `server.servlet.session.timeout` | `30m` | HTTP session timeout |

---

## Troubleshooting

| Issue | Cause | Fix |
|---|---|---|
| `ExceptionInInitializerError: TypeTag :: UNKNOWN` | Lombok version incompatible with Java runtime | Upgrade Lombok to 1.18.40+ (1.18.46 for Java 25/26) |
| `JwtAuthFilter: Constructor threw exception` | Lombok annotation processing failed at compile time | Run `mvnw clean compile` to rebuild with correct Lombok |
| `sun.misc.Unsafe::objectFieldOffset` warnings | Lombok/Netty using deprecated internal APIs | Non-fatal; will be addressed in future library releases |
| Redis connection refused | Redis server not running | Start Redis, or ignore — app falls back to no-op cache |

---

## License

Every line of code in this project carries the weight of gratitude I can never fully express — this is for you, Daddy💗.
