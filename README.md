# ParrotSocial REST API

ParrotSocial is a social media application that provides RESTful APIs for various social media functionalities such as user management, messaging, group creation, event management, and more. This README provides an overview of the project structure, dependencies, and how to set up and run the API.

## Features

- **Authorization:** Implemented using Spring Security with JWT tokens.
- **User Management:** Create, update, delete user accounts.
- **Messaging:** Send and receive messages between users.
- **Group Management:** Create groups, add users to groups, and post within groups.
- **Event Management:** Create events, manage event details, and RSVP.
- **Notifications:** Notify users of relevant activities (not yet implemented).

## Tech Stack

- Spring Boot
- MongoDB
- Spring Security
- JWT (JSON Web Tokens)
- Lombok
- Commons FileUpload

## Dependencies

- [spring-boot-starter-security](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security/3.2.2): Spring Security for authentication and authorization using JWT tokens.
- [spring-boot-starter-data-mongodb](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-mongodb): Spring Boot starter for MongoDB integration.
- [spring-boot-starter-web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web): Starter for building web, RESTful applications.
- [projectlombok](https://mvnrepository.com/artifact/org.projectlombok/lombok): Lombok library for reducing boilerplate code in Java.
- [jjwt](https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api/0.11.5): JWT (JSON Web Token) library for Java.
- [commons-fileupload](https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload/1.5): Apache Commons FileUpload library for file uploads.

## Models

- **User:** Represents a user account with various profile details.
- **City:** Represents a city entity.
- **Conversation:** Represents a conversation between users.
- **Message:** Represents a message within a conversation.
- **Event:** Represents an event with details like title, description, and date.
- **Group:** Represents a group with members and other details.
- **Post:** Represents a post made by a user within a group or on their timeline.
- **Comment:** Represents a comment made on a post.
- **Notification:** Represents a notification for activities (not yet implemented).

## Getting Started

1. Clone the repository.
2. Configure MongoDB connection in `application.properties`.
3. Build and run the project using Maven or your preferred IDE.

## Usage

Once the application is running, you can use tools like Postman to interact with the RESTful APIs provided by the application. Refer to the API documentation or codebase for available endpoints and their usage.

## Contributing

Contributions are welcome! Please fork the repository, make your changes, and submit a pull request. Ensure that your code follows the project's coding standards and conventions.

## License

This project is licensed under the [MIT License](LICENSE).