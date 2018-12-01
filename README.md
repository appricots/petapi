# API Challenge

## Dependencies

This project uses Maven for builds.
You need Java 8 installed.

## Building

```
$ mvn package
```

## Running

```
java -jar target/api_interview-0.1.0.jar
```
or

```
mvn spring-boot:run
```

## Local access

Base URL: 
    `http://localhost:8080`

Docs:
    `http://localhost:8080/swagger-ui.html`

You can run Get or Post requests via swagger by pressing "Try it out" buttons next to Api calls

## ngrok access

Swagger: http://2f29ae37.ngrok.io/swagger-ui.html
From there you can see the pet-controller and execute same API calls.

GET
http://2f29ae37.ngrok.io/pets
http://2f29ae37.ngrok.io/pets/Pug
http://2f29ae37.ngrok.io/pet/12

POST 
http://2f29ae37.ngrok.io/vote/25/true   (vote up)
http://2f29ae37.ngrok.io/vote/25/false  (vote down)

