# News Aggregator

A Spring Boot application that provides a RESTful API for user registration, authentication, and news aggregation based on user preferences.

## Features

- **User Authentication**: JWT-based authentication with user registration and login
- **News Preferences**: Users can set and update their news category preferences
- **News Aggregation**: Fetches news from external APIs based on user preferences
- **Search Functionality**: Search for specific news articles
- **Input Validation**: Comprehensive validation for all API inputs
- **Exception Handling**: Global exception handling with proper HTTP status codes

## Technology Stack

- **Spring Boot 3.2.0**
- **Spring Security** with JWT authentication
- **Spring Data JPA** for data persistence
- **H2 Database** (in-memory)
- **WebClient** for external API calls
- **Maven** for dependency management

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- News API key (get one from [NewsAPI.org](https://newsapi.org/))

## Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd NewsAggregator
   ```

2. **Configure the application**
   - Update `src/main/resources/application.properties`
   - Replace `your-news-api-key-here` with your actual News API key
   - Optionally, update the JWT secret key for production

3. **Build the application**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication

#### Register User
```http
POST /api/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

#### Login User
```http
POST /api/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

### User Preferences

#### Get User Preferences
```http
GET /api/preferences
Authorization: Bearer <jwt-token>
```

#### Update User Preferences
```http
PUT /api/preferences
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "preferences": ["technology", "business", "sports"]
}
```

### News

#### Get News Based on User Preferences
```http
GET /api/news
Authorization: Bearer <jwt-token>
```

#### Search News
```http
GET /api/news/search?query=artificial intelligence
Authorization: Bearer <jwt-token>
```

## Available News Categories

The application supports the following news categories:
- `general`
- `business`
- `technology`
- `sports`
- `entertainment`
- `health`
- `science`

## Database

The application uses H2 in-memory database. You can access the H2 console at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:newsdb`
- Username: `sa`
- Password: `password`

## Example Usage

### 1. Register a new user
```bash
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

### 2. Login and get JWT token
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

### 3. Set user preferences
```bash
curl -X PUT http://localhost:8080/api/preferences \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "preferences": ["technology", "business"]
  }'
```

### 4. Get news based on preferences
```bash
curl -X GET http://localhost:8080/api/news \
  -H "Authorization: Bearer <your-jwt-token>"
```

### 5. Search for specific news
```bash
curl -X GET "http://localhost:8080/api/news/search?query=artificial%20intelligence" \
  -H "Authorization: Bearer <your-jwt-token>"
```

## Error Handling

The application provides comprehensive error handling:

- **400 Bad Request**: Invalid input data or validation errors
- **401 Unauthorized**: Invalid credentials or missing authentication
- **404 Not Found**: User not found
- **500 Internal Server Error**: Unexpected server errors

## Security Features

- JWT-based authentication
- Password encryption using BCrypt
- Input validation and sanitization
- CORS configuration for cross-origin requests
- Stateless session management

## Configuration

Key configuration properties in `application.properties`:

- `jwt.secret`: Secret key for JWT token generation
- `jwt.expiration`: JWT token expiration time in milliseconds
- `news.api.key`: Your News API key
- `news.api.base-url`: News API base URL
- `news.api.timeout`: Request timeout in milliseconds

## Development

### Project Structure
```
src/main/java/com/newsaggregator/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data Transfer Objects
├── entity/         # JPA entities
├── exception/      # Exception handlers
├── repository/     # Data repositories
├── security/       # Security components
└── service/        # Business logic services
```

### Running Tests
```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License. 