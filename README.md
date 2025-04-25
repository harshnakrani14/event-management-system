# Event Management System (EMS)

## Overview
The Event Management System (EMS) is a web application designed to facilitate the management of events, including user registration, event creation, ticket booking, and user authentication. The application utilizes Spring Boot for the backend, MongoDB for data storage, and JWT for secure authentication.

## Features
- **User  Registration and Authentication**: Users can register and log in using JWT tokens.
- **Event Management**: Admin users can create, update, and delete events.
- **Ticket Booking**: Users can book tickets for events, view their tickets, and manage bookings.
- **Role-Based Access Control**: Different roles (USER, ADMIN) with specific permissions for accessing various endpoints.
- **Date and Time Validation**: Ensures that event timings do not overlap.

## Technologies Used
- **Spring Boot**: For building the RESTful API.
- **MongoDB**: For data storage.
- **Joda-Time**: For handling date and time.
- **MapStruct**: For object mapping between DTOs and entities.
- **Spring Security**: For securing the application and managing user authentication.
- **JWT (JSON Web Tokens)**: For secure user authentication.

## Project Structure

    src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── ems
    │   │               ├── config
    │   │               ├── controller
    │   │               ├── dto
    │   │               ├── exception
    │   │               ├── mapper
    │   │               ├── model
    │   │               ├── repository
    │   │               ├── security
    │   │               └── service
    │   └── resources
    │       └── application.properties
    └── test


## application.properties
    spring.application.name = ems
    spring.data.mongodb.uri = mongodb://localhost:27017/ems
    spring.mvc.problem-details.enabled = false
    server.error.include-message = always
    server.error.include-binding-errors = always
    server.error.include-stacktrace = never
    jwt.secret = your_jwt_key


## Installation
1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/ems.git
   cd ems
2. **Set up MongoDB**: Ensure you have MongoDB installed and running. Update the application.properties file with your MongoDB connection details.
3. **Build the project**:
    ```bash
   ./mvnw clean install
4. **Run the application**:
    ```bash
   ./mvnw spring-boot:run
## API Endpoints
### User Endpoints
- **`POST /api/register`**: Register a new user.
- **`POST /api/login`**: Authenticate a user and return a JWT token.
- **`GET /api/profile`**: Get the current user's profile.
- **`PATCH /api/update`**: Update user details.

### Event Endpoints
- **`POST /api/events`**: Create a new event (Admin only).
- **`GET /api/events/all-events`**: Get all events (User and Admin).
- **`GET /api/events/location/{location}`**: Get events by location.
- **`PATCH /api/events/update`**: Update an event (Admin only).
- **`DELETE /api/events/{eventId}`**: Delete an event (Admin only).

### Ticket Endpoints
- **`POST /api/tickets`**: Create a new ticket (User only).
- **`GET /api/tickets/event/{eventId}`**: Get tickets for a specific event (User only).
- **`DELETE /api/tickets/{ticketId}`**: Delete a ticket (User only).

## Exception Handling
The application includes a global exception handler to manage errors and provide meaningful responses to the client.

## Acknowledgments
- Thanks to the Spring community for their excellent documentation and support.
- Special thanks to the contributors of the libraries used in this project.



