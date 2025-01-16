# Explore-with-me

## Introduction

Explore-with-me is a platform that allows users to create, manage, and participate in events. It includes features for tracking statistics on event views and hits, with a divided architecture into admin, public, and private controllers for different levels of access and functionality.

## Technologies Used

- **Spring Boot**: For building the backend services.
- **REST**: For API communication.
- **Docker**: For containerization.
- **PostgreSQL**: For database persistence (assumed based on common practices).
- **Lombok**: For reducing boilerplate code.
- **SLF4J**: For logging.

## Project Structure

The project is organized with the following key components:

- **Controllers**: Located in `ru.practicum.controller` package, divided into admin, public, and private sub-packages.
- **Services**: Business logic implementations in `ru.practicum.service`.
- **DTOs**: Data Transfer Objects in `ru.practicum.dto`.
- **Utils**: Utility classes and configurations.

## Setup and Installation

### Prerequisites

- Java 11+
- Maven
- Docker (for containerized setup)
- PostgreSQL (if not using Docker)

### Installation Steps

1. **Clone the Repository**

   ```bash
   git clone https://github.com/your-repo/explore-with-me.git
   ```

2. **Build the Project**

   ```bash
   mvn clean install
   ```

3. **Running with Docker**

    - Ensure Docker and Docker Compose are installed.
    - Navigate to the project root directory.
    - Run:

      ```bash
      docker-compose up --build
      ```

4. **Running Without Docker**

    - Configure database connection properties in `application.properties`.
    - Run the application using your IDE or:

      ```bash
      mvn spring-boot:run
      ```

## API Documentation

### Stats Server Endpoints

- **POST /hit**

    - Saves hit information.
    - Request Body: `EndpointHitDto`

- **GET /stats**

    - Retrieves statistics based on parameters.
    - Query Params: `start`, `end`, `uris`, `unique`

### Admin API Endpoints

- **POST /admin/categories**

    - Creates a new category.
    - Request Body: `NewCategoryDto`

- **GET /admin/compilations**

    - Retrieves compilations.
    - Query Params: `pinned`, `from`, `size`

- **POST /admin/events**

    - Creates a new event.
    - Request Body: `NewEventDto`

- **GET /admin/users**

    - Retrieves users.
    - Query Params: `ids`, `from`, `size`

### Public API Endpoints

- **GET /categories**

    - Retrieves categories.
    - Query Params: `from`, `size`

- **GET /compilations**

    - Retrieves compilations.
    - Query Params: `pinned`, `from`, `size`

- **GET /events**

    - Retrieves events.
    - Query Params: `text`, `categories`, `paid`, `rangeStart`, `rangeEnd`, `onlyAvailable`, `sort`, `from`, `size`

### Private API Endpoints

- **POST /users/{user-id}/events**

    - Creates an event for a user.
    - Request Body: `NewEventDto`

- **GET /users/{user-id}/requests**

    - Retrieves requests for a user.

## Usage Examples

### Sending a Hit

```bash
curl -X POST "http://localhost:8080/hit" -H "Content-Type: application/json" -d '{"app":"app-name","uri":"uri","ip":"127.0.0.1"}'
```

### Retrieving Stats

```bash
curl "http://localhost:8080/stats?start=2023-10-01&end=2023-10-31&uris=/event&unique=true"
```

## Logging

- Logging is handled via SLF4J and logged to the console by default.

## Contribution Guidelines

- Fork the repository.
- Create a new branch for your feature or bug fix.
- Submit a pull request.

---

## Тестирование с помощью Postman

Для тестирования API приложения используются коллекции Postman, которые находятся в файлах:
- `ewm-main-service.json` (тесты для основного сервиса)
- `ewm-stat-service.json` (тесты для сервиса статистики)
- `feature.json` (тесты для фич)

### Инструкция по тестированию:

1. Установите Postman, если он еще не установлен.
2. Импортируйте коллекции тестов в Postman:
    - Откройте Postman.
    - Нажмите `Import` и выберите файлы `ewm-main-service.json`, `ewm-stat-service.json` и `feature.json`.
3. Запустите тесты:
    - Выберите коллекцию и нажмите `Run`.
    - Убедитесь, что серверы запущены (основной сервис и сервис статистики).
4. Проверьте результаты тестов в интерфейсе Postman.

---