# Movie

### A movie streaming service built with Java Spring, PostgreSQL, Redis, and Kafka. This project provides functionality for streaming movies as well as full CRUD (Create, Read, Update, Delete) operations.

# Features

- **User Authentication**: Secured with JWT (RSA512)
- **Role-Based Access Control**:
  - Users can only access GET endpoints
  - Admins have full access to all endpoints
- **Service Communication**: Asynchronous communication between services using Kafka
- **Video Streaming**: Stream video over HTTP
- **Rate Limiting**: Implemented to prevent abuse and ensure fair usage
- **Spring Reactive**: Utilized for non-blocking, efficient, and scalable service operations

# Architecture
### The Movie composed of following components:
- API Server: Handles HTTP requests for Movie, Actor, Director and Genre.
- Redis Cache: Caches the id of data as the key and the data as the value.
- PostgreSQL Database: Stores the Movie and its associated Actor, Director and Genre.
- Kafka: Asynchronous communication between services.
# Installation

   1. Clone the repository: <code> git clone https://github.com/Kruzk02/Movie </code>
   2. Navigate to the project directory: <code> cd Movie </code>
   3. Start Docker compose.
   4. Start Java project.
   5. Access the API at <http://localhost:8000>.
