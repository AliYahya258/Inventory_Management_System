# 🧾 Inventory Tracking System – Case Study (Bazaar Challenge)

Welcome! This project is a backend service designed to track product inventory and stock movements, starting from a single kiryana store and scaling up to support thousands of stores with real-time updates, audit logs, and caching.

---

## 🚀 Project Summary

This system manages product inventory across multiple stores, enabling:

- Real-time stock visibility per store and product
- Logging of all stock movements (IN, OUT, REMOVED)
- Asynchronous updates and rate limiting
- Redis-backed caching for scalability
- Audit logs to trace every action
- RESTful API design with secure access

---

## 🏗️ Architecture & Design

### Entity Relationships

- `Store`: Represents a kiryana store.
- `Product`: Central catalog of all products.
- `Inventory`: Junction table linking a product to a store with its current quantity.
- `StockMovement`: Logs each change in inventory with type, date, and quantity.

### Evolution Path

#### ✅ **v1: MVP (Single Store)**
- PostgreSQL used for local persistence
- Basic REST APIs to log and view stock
- Inventory table links store to product

#### ✅ **v2: Multi-store Support**
- Stores are dynamically managed
- Product catalog is shared across stores
- APIs support filtering by store and date range
- Basic authentication in place

#### ✅ **v3: Scalable System**
- Redis added for caching
- Async service methods using `@Async`
- Bucket4j-based rate limiting per IP
- Audit logs written for every movement
- Caching used to reduce DB load

---

## 🔐 Security Features

- Basic Authentication is enforced for all APIs.
- Bucket4j limits each IP to **10 requests/minute (can be modified as per need)** to prevent abuse.

---

## ⚙️ Tech Stack

| Layer        | Tech Used             |
|--------------|-----------------------|
| Backend      | Spring Boot           |
| Database     | PostgreSQL            |
| Caching      | Redis (via Spring Data Redis) |
| Rate Limiting| Bucket4j              |
| Async        | Spring `@Async`       |
| Logging      | Custom audit via StockMovement logs |

---

## 📂 API Endpoint Reference


### 🌐 Base URL

When running locally, all API requests should be made to:

http://localhost:8081


Example:
(GET http://localhost:8081/api/products)


Make sure the server is running on port `8081`. If you've changed it, update your request URLs accordingly.

---

## 📦 Core API Endpoints

### 🧾 Product APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/products` | Create a new product |
| `GET` | `/api/products` | Get all products |
| `GET` | `/api/products/{id}` | Get product by ID |
| `GET` | `/api/products/store/{storeId}` | Get all products by Store ID |
| `PUT` | `/api/products/{id}` | Update product |
| `DELETE` | `/api/products/{id}` | Delete product |

---

### 🏬 Store APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/stores` | Create a store |
| `GET` | `/api/stores` | Get all stores |
| `GET` | `/api/stores/{id}` | Get store by ID |
| `PUT` | `/api/stores/{id}` | Update store |
| `DELETE` | `/api/stores/{id}` | Delete store |

---

### 👤 User APIs
| Method   | Endpoint                      | Description            |
|----------|-------------------------------|------------------------|
| `POST`   | `/api/auth/login`             | Logs in the user       |
| `POST`   | `/api/auth/register`          | Register a Seller user |
| `POST`   | `/api/auth/admin/create`      | Creates a new Admin    |
| `GET`    | `/api/auth/admin/users`       | Get all users          |
| `DELETE` | `/api/auth/admin/delete/{id}` | Delete user            |
| `GET`    | `/api/auth/admin/users/{id}`  | Get user by id         |
---
## 👨‍💻 Author

**[Ali Yahya Amer]**  
FAST-NUCES | Junior |
Spring Boot Developer | Next.js Developer | Fullstack Engineer