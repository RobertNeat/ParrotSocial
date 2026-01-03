# ParrotSocial REST API

ParrotSocial is a social media application that provides RESTful APIs for various social media functionalities such as user management, messaging, group creation, event management, and more. This README provides an overview of the project structure, dependencies, and how to set up and run the API.

## Features

- **Authorization:** Implemented using Spring Security with JWT tokens.
- **User Management:** Create, update, delete user accounts.
- **Messaging:** Send and receive messages between users.
- **Group Management:** Create groups, add users to groups, and post within groups.
- **Event Management:** Create events, manage event details, and RSVP.
- **Notifications:** Notify users of relevant activities.

- **Notes:** Create user private notes.
- **Tags:** Tagging notes in any amount, searching using tags.
- **User defined states:** Change states of the notes in user defined cycle (the notes can then be searched based on the note state).


## Tech Stack

- PostrgesSQL database 18 
- Spring Boot 4.0.1 (java 25, jar packaging) 
- Angular 21.0.0  (node 24.0.0) 

All stack is contnainerized from the start (even the development can be made in the container environment)

- * Spring Security
- * JWT (JSON Web Tokens)
- * Lombok
- * Commons FileUpload

<!-- ## Dependencies

- [spring-boot-starter-security](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security/3.2.2): Spring Security for authentication and authorization using JWT tokens.
- [spring-boot-starter-data-mongodb](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-mongodb): Spring Boot starter for MongoDB integration.
- [spring-boot-starter-web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web): Starter for building web, RESTful applications.
- [projectlombok](https://mvnrepository.com/artifact/org.projectlombok/lombok): Lombok library for reducing boilerplate code in Java.
- [jjwt](https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api/0.11.5): JWT (JSON Web Token) library for Java.
- [commons-fileupload](https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload/1.5): Apache Commons FileUpload library for file uploads. -->

## Models (to implement ..)

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
2. Build the springboot application to the .jar packaging file and ansure the dockerfile references it properly
3. Run the docker swarm by:

```
# Start all services
docker-compose up -d

# Stop all services (data persists)
docker-compose down

# Stop and remove volumes (removes database data)
docker-compose down -v
```

*all changes in angular will be updated each 2000 ms (springboot needs to be rebuilt to take effect and the docker image needs to be built as well)

## Important notes for the development

0. Set up springboot-database connection to ensure the data ban be saved and read from-to database
1. Use liquibase for migrations
2. Implement Openapi standard for swagger-like capabilities
3. Ensure proper interaction between the backed and optional postman client.

## Contributing

Contributions are welcome! Please fork the repository, make your changes, and submit a pull request. Ensure that your code follows the project's coding standards and conventions.

## License

This project is licensed under the [MIT License](LICENSE).