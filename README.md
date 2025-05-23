
# Darrasni E-Learning Platform

Darrasni is a microservices-based e-learning platform designed to provide a scalable, secure, and flexible learning experience. This system uses Spring Boot and Spring Cloud for service orchestration, communication, and security, and integrates with third-party services like Stripe for payment processing.

![System Architecture](Untitled%20Diagram%201.png)

---

## 📐 Architecture Overview

The architecture follows the microservices pattern with the following core components:

### 🔐 **API Gateway**
- Acts as the entry point for all client requests.
- Handles request routing, authentication, and rate limiting.
- Secured with **Spring Security**.

### ⚖️ **Load Balancer**
- Distributes incoming requests to the appropriate microservices.
- Ensures fault tolerance and availability.

### 🔎 **Eureka Server**
- Service discovery mechanism that keeps track of all registered microservices.

### 🤝 **Feign Client**
- Handles inter-service communication using synchronous REST calls.
- Enables declarative REST client setup.

---

## 🧩 Microservices

Each microservice is independent and uses its own database where applicable:

### 1. **Auth-Service**
- Manages user authentication and registration.
- Uses **JPA Repository** for persistence.

### 2. **Cours-Service**
- Manages course creation, updates, and enrollment.
- Uses **MongoDB** for flexible course data modeling.

### 3. **Payment-Service**
- Handles all payment transactions.
- Integrates with **Stripe** API for payment processing.
- Uses **JPA Repository** for transaction history.

### 4. **Forum-Service**
- Provides discussion forums for students and instructors.
- Uses **JPA Repository** for storing posts and replies.

### 5. **Statistics-Service**
- Tracks user and course statistics for analytics.
- Uses **MongoDB Repository**.

### 6. **Video-Call-Service**
- Manages real-time video conferencing features for courses.

---

## 🛠️ Technologies Used

- **Spring Boot** & **Spring Cloud**
- **Spring Security**
- **Eureka (Service Discovery)**
- **Feign Client**
- **MongoDB** & **MySQL**
- **Stripe API**
- **Docker** *(Optional for deployment)*

---

## 🌐 Frontend

- **Darrasni Website**: Web interface for students and instructors.
- Communicates with backend services via the API Gateway.

---

## 🧾 Database

- **MongoDB**: Used by services that require flexible schemas (e.g., Cours, Statistics).
- **MySQL**: Used for structured data in Auth, Payment, and Forum services.

---

## 📦 Getting Started

To run the project locally:

1. Clone the repository.
2. Start the Eureka server.
3. Run each microservice.
4. Launch the API Gateway.
5. Access the frontend via browser.

---

## 📄 License

This project is licensed under the MIT License.
