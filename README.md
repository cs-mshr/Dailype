
# User Management Spring Boot Application

## Overview

This Spring Boot application provides endpoints for managing user information in a database. It offers functionalities to create, retrieve, update, and delete user records.
Although it was an [assignment](https://gist.github.com/ashwin-dailype/b2f26c7f4ca37304c921b4ef582b75c3).

## Steps to Run the Project

1. Clone the Git repository to your local machine.
2. Open the project in your preferred IDE.
3. Update the MySQL database credentials in the `application.properties` file located in the `src/main/resources` directory.
4. Ensure that MySQL is running on your local machine.
5. Build the project using Maven or Gradle.
6. Run the application.
7. Use any API testing tool like Postman to test the endpoints.

## Changing MySQL Credentials

To change the MySQL credentials in the `application.properties` file:

1. Open the `application.properties` file located in the `src/main/resources` directory.
2. Update the following properties with your MySQL database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/dailype
   spring.datasource.username=root
   spring.datasource.password=root
   ```
3. Save the file.

---


## Accessing Swagger Documentation

You can access the Swagger documentation for this API by navigating to:

- [Swagger UI](http://localhost:8080/swagger-ui/index.html)

This interactive documentation provides a user-friendly interface to explore and test the endpoints.

## Accessing OpenAPI JSON

You can access the OpenAPI JSON for this API by navigating to:

- [OpenAPI JSON](http://localhost:8080/v3/api-docs)

This JSON file contains detailed information about the API endpoints, request parameters, and response structures.

---
## Endpoints

### 1. /create_user

- **Method:** POST
- **Description:** Creates a new user record with provided details.
- **Request Body:**
  ```json
  {
    "full_name": "John Doe",
    "mob_num": "9876543210",
    "pan_num": "AABCP1234C",
    "manager_id": "uuid"
  }
  ```
- **Response:**
  - Success: Returns a success message upon successful user creation.
  - Failure: Returns an appropriate error message if any validation fails.

### 2. /get_users

- **Method:** POST
- **Description:** Retrieves user records based on different criteria.
- **Request Body:**
  ```json
  {
    "user_id": "uuid",
    "mob_num": "9876543210",
    "manager_id": "uuid"
  }
  ```
- **Response:**
  - Success: Returns a JSON object with an array of user objects.
  - Failure: Returns an empty JSON array if no users found.

### 3. /delete_user

- **Method:** POST
- **Description:** Deletes a user record based on user ID or mobile number.
- **Request Body:**
  ```json
  {
    "user_id": "uuid",
    "mob_num": "9876543210"
  }
  ```
- **Response:**
  - Success: Returns a success message upon successful user deletion.
  - Failure: Returns an appropriate error message if user not found.

### 4. /update_user

- **Method:** POST
- **Description:** Updates user records based on provided user IDs and update data.
- **Request Body:**
  ```json
  {
    "user_ids": ["uuid1", "uuid2"],
    "update_data": {
      "full_name": "Updated Name",
      "mob_num": "9876543210",
      "pan_num": "AABCP1234C",
      "manager_id": "uuid"
    }
  }
  ```
- **Response:**
  - Success: Returns a success message upon successful user update.
  - Failure: Returns an appropriate error message if any validation fails.

## BEST Practices followed-

- Proper error handling and logging are implemented.
- Timely commits are made with meaning full comments to display the project progress.

---
