# Test Project created for OTA

This project manages operations related to notes.

## Setup Guide
- Install sdkman (optional) - https://sdkman.io/
- Install Maven v3.9 (or onwards)
- Install Java v21 (Eclipse Temurin https://adoptium.net/temurin/releases/) - use LTS version

## Start the app by running the command below
`mvn spring-boot:run`

## Endpoints

- **GET /notes**: Retrieves all notes.
- **GET /notes/{id}**: Retrieves a note by its ID.
- **POST /notes**: Adds a new note.
- **DELETE /notes/{id}**: Deletes a note by its ID.
- **PUT /notes/{id}**: Updates a note by its ID.

## API Documentation
- http://localhost:8080/swagger-ui.html

### Software Version
- Spring Boot v3.3.0
- Java v21

