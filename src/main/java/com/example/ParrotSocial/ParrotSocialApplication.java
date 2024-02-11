package com.example.ParrotSocial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParrotSocialApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParrotSocialApplication.class, args);
	}

}

/*
 * Aby skorzystać z dokumentacji swagger z paczki OpenApi (OpenApi zawiera w sobie Swagger) należy:
 * - w przeglądarce (GET): http://localhost:8080/v3/api-docs
 * - w przeglądarce (HTML UI): http://localhost:8080/swagger-ui/index.html
 * (^ domyślne trasy dokumentacji zmienione za pomocą application.properties)
 * */


/*
 * Adnotacje swagger ( a w naszym przypadku springdoc):
 * -jako referencja  --- https://docs.swagger.io/swagger-core/v1.5.0/apidocs/
 * -adnotacje ktore mozna używać --- https://springdoc.org/#migrating-from-springfox
 * - problemy z dostępnością swaggera przy spring security: https://stackoverflow.com/questions/37671125/how-to-configure-spring-security-to-allow-swagger-url-to-be-accessed-without-aut
 * */


/*
* Mongodb queries construction:
* https://www.baeldung.com/queries-in-spring-data-mongodb
* https://www.baeldung.com/spring-data-mongodb-tutorial
* */


/*
* Other approaches to JWT in springboot (i did not try them as the 29.01.2024
* https://www.baeldung.com/spring-security-oauth-jwt
* */