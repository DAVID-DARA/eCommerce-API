# E-Commerce API

This project is an API for an online fashion web application built using Java's Spring Boot Framework. It provides endpoints for managing user authentication, product catalog, shopping cart, orders, payments, and more.

## Project Overview

### Project Information
- **Group**: `com.project`
- **Artifact**: `e-commerce_api`
- **Name**: `e-commerce_api`
- **Package Name**: `com.project.ecommerce_api`
- **Description**: API for an online fashion web app.

### Features
- **User Authentication**: Secure signup, login, and token-based authentication using JWT.
- **Product Management**: Manage product listings, categories, and inventory.
- **Shopping Cart**: Add, update, and remove items from the cart.
- **Wishlist**: Create and manage multiple wishlists.
- **Order Processing**: Place orders, manage order items, and track order history.
- **Payment Integration**: Integrate with payment gateways to process transactions.
- **Discount Management**: Apply discounts to products or orders.
- **Shipping Management**: Configure shipping methods and calculate costs.
- **Audit Logging**: Track and log significant actions in the system.
- **User Verification**: Token-based email verification for user signups.

## Technologies Used
- **Java 17**
- **Spring Boot 3.x**
- **Spring Security**
- **JWT for security**
- **MySQL/PostgreSQL** (for database)
- **Cloudinary** (for image storage)
- **Mailtrap** (for email handling)
- **DotEnv** (for environment variable management)
- **Docker** (for containerization)

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven or Gradle
- MySQL or PostgreSQL database
- Postman (optional, for testing)
- An IDE like IntelliJ IDEA or Eclipse

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/e-commerce_api.git
   cd e-commerce_api

2. **Set up the database:**
   - Create a new MySQL/PostgreSQL database.
   - Update the database configuration in application.properties or .env file.
 
3. **Install dependencies:**
    ```bash
    ./mvnw install
   
4. **Run the application locally:**
    ```bash
    ./mvnw spring-boot:run
   
5. **Access the API:**
   - The API will be running at http://localhost:8080.

### Running Tests
- To run unit and integration tests, use the following command:
    ```bash
    ./mvnw test

### Deployment with Docker
1. **Build Docker Image:**
    ```bash
    docker build -t e-commerce-api .

2. **Run Docker Container:**
    ```bash
    docker run -p 8080:8080 --env-file .env e-commerce-api

3. **Access the API:**
   - The API will be available at http://localhost:8080 within the Docker container.

### Environment Variables
- The application uses environment variables managed by DotEnv. You can define these in a .env file:
    ```makefile
    DB_URL=jdbc:mysql://localhost:3306/ecommerce_db
    DB_USERNAME=root
    DB_PASSWORD=password
    JWT_SECRET=your_jwt_secret_key
    CLOUDINARY_URL=cloudinary://api_key:api_secret@cloud_name
    MAILTRAP_API_KEY=your_mailtrap_api_key

## Credit
- Spring Boot: For the framework.
- Cloudinary: For image storage.
- Mailtrap: For email handling.

## Contributing
- Contributions are welcome! Please follow the contribution guidelines.

## License
- This project is licensed under the MIT License. See the LICENSE file for details.
    ```csharp 
    Feel free to adjust this further based on your specific needs or preferences.