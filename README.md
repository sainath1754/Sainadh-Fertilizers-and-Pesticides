# 🌿 Sainadh Fertilizers & Pesticides — Agriculture E-Commerce Platform

A full-stack agriculture e-commerce web application built with **Spring Boot** and **RESTful APIs** to digitally connect farmers with fertilizer and pesticide suppliers.

---

## 📋 Project Overview

This project implements a complete e-commerce platform for agricultural products (fertilizers and pesticides). Farmers can browse the product catalog, add items to their shopping cart, and place orders — all through a simple, clean web interface.

The application follows **MVC architecture** and **SDLC practices**, with **MySQL** for persistent data storage and **Spring Security** for user authentication.

---

## 🛠️ Tech Stack

| Layer        | Technology                         |
|--------------|------------------------------------|
| Backend      | Java 17, Spring Boot 3.2.5         |
| Web          | Spring MVC, RESTful APIs           |
| Security     | Spring Security, BCrypt hashing    |
| Database     | MySQL 8.x, Spring Data JPA         |
| Frontend     | Thymeleaf, HTML5, CSS3, JavaScript |
| Build Tool   | Maven                              |
| ORM          | Hibernate (via Spring Data JPA)    |
| Dev Tools    | Lombok, Spring DevTools            |

---

## ✨ Features

### Authentication
- **Login** — Secure login with username and password
- **Register** — New user registration with form validation
- **Logout** — Session-based logout with CSRF protection
- **Account Lockout** — Locks account after 5 failed login attempts

### Pages (after login)
- **Home** — Dashboard with stats (product counts, cart items, orders) and quick links
- **Store** — Product catalog with category filters (Fertilizers / Pesticides), add-to-cart
- **Cart** — View cart items, update quantity, remove items, checkout (place order)
- **Chatbot** — Chat interface for agricultural queries (UI-only demo, no backend AI)
- **About** — Our emotional story, mission, vision, features, and contact details

### Technical
- MVC architecture with separate Model, Controller, Service, Repository layers
- CSRF protection on all POST forms
- Session management (30-minute timeout, max 1 session per user)
- Responsive design for mobile and desktop
- Seed data (admin user + 12 products) loaded on first startup

---

## 📁 Project Structure

```
fertilizers/
├── src/main/java/com/sainadh/fertilizers/
│   ├── FertilizersApplication.java          # Main Spring Boot entry point
│   ├── config/
│   │   └── SecurityConfig.java              # Spring Security configuration
│   ├── controller/
│   │   ├── AuthController.java              # Login, Register endpoints
│   │   ├── HomeController.java              # Home dashboard
│   │   ├── StoreController.java             # Product catalog, add to cart
│   │   ├── CartController.java              # Cart operations, checkout
│   │   ├── ChatbotController.java           # Chatbot UI page
│   │   └── AboutController.java             # About page
│   ├── model/
│   │   ├── User.java                        # User entity
│   │   ├── Product.java                     # Product entity
│   │   ├── CartItem.java                    # Cart item entity
│   │   ├── OrderRecord.java                 # Order entity
│   │   └── OrderItem.java                   # Order line item entity
│   ├── repository/
│   │   ├── UserRepository.java              # User data access
│   │   ├── ProductRepository.java           # Product data access
│   │   ├── CartItemRepository.java          # Cart data access
│   │   ├── OrderRepository.java             # Order data access
│   │   └── OrderItemRepository.java         # Order item data access
│   ├── service/
│   │   ├── UserService.java                 # Registration logic
│   │   ├── ProductService.java              # Product catalog logic
│   │   ├── CartService.java                 # Cart operations logic
│   │   ├── OrderService.java                # Order placement logic
│   │   ├── CustomUserDetailsService.java    # Spring Security user loader
│   │   └── LoginAuditService.java           # Login audit (placeholder)
│   └── init/
│       └── DataInitializer.java             # Seeds admin user + products
├── src/main/resources/
│   ├── application.properties               # DB config, server settings
│   ├── static/css/
│   │   └── style.css                        # All page styles
│   └── templates/
│       ├── fragments/
│       │   └── layout.html                  # Shared navbar & footer
│       ├── login.html                       # Login page
│       ├── register.html                    # Registration page
│       ├── home.html                        # Home dashboard
│       ├── store.html                       # Product store
│       ├── cart.html                        # Shopping cart
│       ├── chatbot.html                     # Chatbot interface
│       └── about.html                       # About us page
├── pom.xml                                  # Maven dependencies
├── commands.txt                             # MySQL setup commands
└── README.md                               # This file
```

---

## 🎨 UI/UX Enhancements (Latest Update)

The following frontend improvements were made to elevate the user experience:

### 🌈 Color Palette Overhaul
- Upgraded to a deeper emerald green palette (`#0b3d2c`, `#14644a`, `#27ae76`)
- Added warm amber (`#f0a500`) and orange (`#ff6b35`) accent colors
- Body background changed to a multi-stop gradient for depth
- Login page uses a rich 4-stop gradient

### ✨ Animations
- **6 keyframe animations** added: `fadeInUp`, `fadeIn`, `slideDown`, `pulse`, `shimmer`, `float`
- Navbar slides down smoothly on page load
- Cards and stat blocks fade in with staggered delays
- Product and stat cards glow on hover with floating icons
- Welcome banner has a radial gradient overlay effect
- Nav links have animated underline indicators on hover
- Smooth cubic-bezier transitions throughout the app

### 🧭 Navbar Reordered
- **Before:** Home → Store → Cart → Chatbot → About
- **After:** Home → About → Store → Cart → Chatbot
- Logical flow: learn about us first, then shop

### 🌱 Our Story (About Page)
- Rewrote the "Our Story" section with a heartfelt, emotional narrative
- Dedicated to the founder's father, **Suresh Swamy Naidu Pragada**, who runs a traditional fertilizer shop in **Pathuru village**, Andhra Pradesh
- Styled with a warm amber left border and cream gradient background
- Includes a personal signature with 💓 emoji

### 📞 Contact Information Updated
- **Email:** sainadh1754@gmail.com
- **Phone:** +91 8019789641
- **Address:** PATHURU, ANDHRA PRADESH, INDIA
- Redesigned as a visual grid with emoji icons and clickable links

### 💓 Footer Dedication
- Footer now displays: **"Dedicated to Suresh Swamy Naidu Pragada 💓"**
- Name highlighted in amber/gold color for emphasis
- Footer background upgraded to a dark gradient

### 🃏 Card & Component Upgrades
- Cards have hover shadow elevation effects
- Stat cards change border color to amber on hover
- Quick link cards trigger floating icon animation on hover
- Product cards have glow effect and floating icon on hover
- Contact section uses styled cards with hover lift effect
- Logout button uses a gradient with glow shadow


## 🚀 Setup & Run Instructions

### Prerequisites
- **Java 17** (JDK)
- **MySQL 8.x** (MySQL Workbench recommended)
- **Maven** (or use the included `mvnw` wrapper)

### Step 1 — Create the Database
Open MySQL Workbench and run the commands from `commands.txt`:

```sql
CREATE DATABASE IF NOT EXISTS sainadh_fertilizers;
USE sainadh_fertilizers;
```

> The application uses `spring.jpa.hibernate.ddl-auto=update`, so all tables will be created automatically on first startup.

### Step 2 — Configure Database Credentials
Edit `src/main/resources/application.properties` if your MySQL credentials differ:

```properties
spring.datasource.username=root
spring.datasource.password=Sainadh@1754
```

### Step 3 — Run the Application

**Using Maven wrapper (Windows):**
```bash
mvnw.cmd spring-boot:run
```

**Using Maven wrapper (Linux/Mac):**
```bash
./mvnw spring-boot:run
```

**Using Maven directly:**
```bash
mvn spring-boot:run
```

### Step 4 — Access the Application
Open your browser and go to: **http://localhost:8080**

### Default Admin Login
| Field    | Value          |
|----------|----------------|
| Username | `admin`        |
| Password | `Sainadh@1754` |

---

## 📊 Database Schema

The application creates these tables automatically:

| Table         | Description                              |
|---------------|------------------------------------------|
| `users`       | Registered users (username, email, role)  |
| `products`    | Fertilizers and pesticides catalog        |
| `cart_items`  | Items in user shopping carts             |
| `orders`      | Placed order records                     |
| `order_items` | Individual items within each order       |

---

## 👤 Author

**Sainadh** — sainadh1754@gmail.com  
📱 +91 8019789641  
📍 Pathuru, Andhra Pradesh, India

---

## 💓 Dedication

This project is lovingly dedicated to **Suresh Swamy Naidu Pragada** — a father whose traditional fertilizer shop in Pathuru village inspired every line of code.

---

## 📜 License

This project is developed for educational purposes as part of a Spring Boot web development course.
