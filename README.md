# Introduction

A library to provide basic features which are required in a microservice application.

## Features

1. Global Exception Handler
2. Built in response class


## Getting Started

Add dependency in your project.

```xml

<dependency>
    <groupId>io.github.ilyaslabs</groupId>
    <artifactId>spring-boot-microservice</artifactId>
    <version>2.0.0</version>
</dependency>
```

## Returning custom error response
In our code we can throw `HttpResponseException` and global exception handler will catch it and return a custom error response.

```java
throw HttpResponseException.ofBadRequest(Map.of("password", "Incorrect password"));
```
> There are variety of methods available to return different status codes like `ofBadRequest`, `ofUnauthorized`, `ofForbidden`, etc.

Response code are not limited the methods provided there is also a variant of the method which can be used to return custom status code.

```java
/**
 * Creates a {@link HttpResponseException} with the specified HTTP status and message.
 *
 * @param status  the HTTP status of the exception
 * @param message the detail message of the exception
 * @return the created HttpResponseException object
 */
public static HttpResponseException of(HttpStatus status, String message) {
    return new HttpResponseException(status, message, null);
}
```

Feel free to browse the code to see all the available methods [here](https://github.com/ilyaslabs/spring-boot-microservice/blob/main/src/main/java/io/github/ilyaslabs/microservice/exception/HttpResponseException.java)
