# Document and Notification Microservices

This project consists of two microservices: Document Service and Notification Service.

## Project Structure

- document-service/
    - Dockerfile

- notification-service/
    - Dockerfile
    
- docker-compose.yml
- settings.gradle.kts

## Microservices

### Document Service

The Document Service is responsible for managing freelancer documents. It allows freelancers to add, update, and delete their documents. One freelancer can have multiple documents. After every successful verification, the Document Service calls the Notification Service to process and distribute relevant notifications.

### Notification Service

The Notification Service receives and processes notifications generated from the Document Service API. It handles the distribution of these notifications and saves them into the database.

## Services Used

1. RabbitMQ: Used to receive notifications from the `NotificationController.java` and forward them to a queue.

2. Spring Boot Default Cache: Applied on the Document model for caching. The cache is updated after database operations.

3. MongoDB:
   - Reactive MongoDB is used for the Notification Service.
   - Non-reactive MongoDB is used for the Document Service.

4. Containerization: Both services are containerized and can be deployed using `docker-compose.yml`.

## How to Run

This section provides instructions on how to run the project using different methods.

### Running the Project

You have two options to run this project:

1. Using Docker Compose (Recommended)
2. Building and Running Services Independently

#### 1. Using Docker Compose (Recommended)

To start the entire project using Docker Compose:

1. Navigate to the root folder of the project.
2. Run the following command:
   ```
   docker-compose up --build
   ```
   This will start MongoDB, RabbitMQ, and both microservices.

#### 2. Building and Running Services Independently

If you prefer to run services independently, follow these steps:

##### Install Dependencies

1. Install MongoDB:
   ```
   docker run -d --name mongodb -p 27017:27017 mongo:latest
   ```

2. Install RabbitMQ:
   ```
   docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management
   ```

##### Document Service

The Document Service depends on Spring Boot's internal cache. You can run it in two ways:

1. Through an IDE:
   - Update `application.yml`:
     ```yaml
     spring:
       data:
         mongodb:
           uri: ${SPRING_DATA_MONGODB_URI:mongodb://localhost:27017/documentdb}
     notification:
       service:
         url: ${NOTIFICATION_SERVICE_URL:http://localhost:8081/v1/api/notifications}
     ```
   - Run the main application class.

2. Using Docker:
   - Build the image:
     ```
     docker build --no-cache -f document-service/Dockerfile -t document-service:latest .
     ```
   - Run the container:
     ```
     docker run -p 8080:8080 document-service:latest
     ```

##### Notification Service

The Notification Service depends on both MongoDB and RabbitMQ. You can run it in two ways:

1. Through an IDE:
   - Update `application.yml`:
     ```yaml
     spring:
       data:
         mongodb:
           uri: ${SPRING_DATA_MONGODB_URI:mongodb://localhost:27017/notificationdb}
       rabbitmq:
         host: ${SPRING_RABBITMQ_HOST:localhost}
         port: ${SPRING_RABBITMQ_PORT:5672}
         username: guest
         password: guest
     ```
   - Run the main application class.

2. Using Docker:
   - Build the image:
     ```
     docker build --no-cache -f notification-service/Dockerfile -t notification-service:latest .
     ```
   - Run the container:
     ```
     docker run -p 8081:8081 notification-service:latest
     ```

## API Documentation

Both services have Swagger UI enabled for API documentation:

- Document Service: `http://localhost:8080/swagger-ui/index.html`
- Notification Service: `http://localhost:8081/swagger-ui/index.html`

## Health Checks

Health check endpoints are available via Spring Boot Actuator:

- Document Service: `http://localhost:9090/actuator/health`
- Notification Service: `http://localhost:9091/actuator/health`

These endpoints can be used to monitor the health of the services in a production environment.
