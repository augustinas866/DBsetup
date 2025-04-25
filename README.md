# Sports Team Management API

A RESTful API for managing sports teams, players, and coaches.

## Technologies Used

- Java 17
- Spring Boot 3.4.5
- Spring Data JPA
- MySQL
- Swagger/OpenAPI for API documentation
- Lombok for reducing boilerplate code

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven

## Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd project
```

2. Configure the database:
   - Create a MySQL database named `database`
   - Update `application.properties` with your database credentials if needed

3. Build and run the application:
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

Swagger UI is available at: `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Players

- `GET /api/players` - Get all players
  - Query parameters:
    - `name` (optional) - Filter by name
    - `surname` (optional) - Filter by surname
    - `personalCode` (optional) - Filter by personal code

- `POST /api/players` - Create a new player
  ```json
  {
    "name": "James",
    "surname": "Wilson",
    "dateOfBirth": "1995-05-15",
    "personalCode": "PC789"
  }
  ```

- `GET /api/players/{id}` - Get player by ID
- `PUT /api/players/{id}` - Update player
- `DELETE /api/players/{id}` - Delete player

### Coaches

- `GET /api/coaches` - Get all coaches
  - Query parameters:
    - `name` (optional) - Filter by name
    - `surname` (optional) - Filter by surname
    - `licenseId` (optional) - Filter by license ID
    - `personalCode` (optional) - Filter by personal code

- `POST /api/coaches` - Create a new coach
  ```json
  {
    "name": "David",
    "surname": "Brown",
    "coachingFrom": "2015-01-01",
    "licenseId": "LIC456",
    "personalCode": "PC456"
  }
  ```

- `GET /api/coaches/{id}` - Get coach by ID
- `PUT /api/coaches/{id}` - Update coach
- `DELETE /api/coaches/{id}` - Delete coach

### Teams

- `GET /api/teams` - Get all teams
  - Query parameters:
    - `teamName` (optional) - Filter by team name
    - `yearCreated` (optional) - Filter by year created
    - `coachId` (optional) - Filter by coach ID
    - `playerId` (optional) - Filter by player ID

- `POST /api/teams` - Create a new team
  ```json
  {
    "coachId": 1,
    "playerId": 1,
    "teamName": "Eagles",
    "yearCreated": 2024
  }
  ```

- `GET /api/teams/{id}` - Get team by ID
- `PUT /api/teams/{id}` - Update team
- `DELETE /api/teams/{id}` - Delete team

## Response Codes

- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Postman Collection

A Postman collection is available in the `postman` directory. Import it into Postman to test the API endpoints.

## Error Handling

The API returns appropriate HTTP status codes and error messages in case of failures:

```json
{
  "timestamp": "2024-04-25T11:22:29.033+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request - missing required fields",
  "path": "/api/players"
}
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request