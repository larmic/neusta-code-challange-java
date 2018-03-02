# Neusta Code Challenge

## Introduction

This is a submission to the neusta code challenge 2017. 
The following technologies have been used for the implementation: maven, java, spring boot, swagger and optional docker.

Three endpoints are available after starting the application:

* POST /api/import consumes multipart/form-data
* GET /api/room/{roomNumber} produces JSON
* GET /api/room produces JSON

The acceptance criteria are not completely clear regarding the following question:
Can a room be searched for by person details? 
For example, shall I be able to enter a person's name/ldap and the application returns the room?

In case the above mentioned criteria is required, a quick solution was implemented in the third endpoint.
It accepts the optional query parameter "ldapUser". 
Searching by "ldapUser" returns a list of all rooms that have a person assigned whose ldap user name contains the given string.
 
If the possibility to search for user details is a required feature, two additional endpoints should be implemented:
 
* GET /api/persons/{ldapUser} 
* GET /api/persons?firstName=...&lastName=...&ldapUserName=...

The returning JSON should contain a link to the corresponding rooms (see HATEOAS).

## Main design decisions

Due to time constrains no ARC42 documentation is created. 

| Decision | Reason |
| --- |---|
| Java | <ul><li>Next to kotlin my favorite language</li></ul>|
| Spring | <ul><li>Spring is popular for building web and enterprise applications</li><li>Spring supports dependency injection</li><li>It is easy to write tests in Spring</li></ul>  |
| Spring Boot | <ul><li>Spring Boot make it easy to create Spring-powered, production-grade applications and services with minimum fuss.</li><li>Spring Boot provides a range of non-functional features that are common to large classes of projects (e.g. embedded servers, security, metrics, health checks, externalized configuration)</li></ul>  |
| Hexagonal (or onion) architecture | <ul><li>It is an architectural style that moves programmer’s focus from conceptual layers to a distinction between the software’s inside and outside parts</li><li>Hexagonal Architecture defines conceptual layers of code responsibility, and then points out ways to decouple code between those layers.</li></ul> |

## Requirements

* Java 1.8
* Maven >= 3.2.1
* Docker optional

## Build and run

### Plain Java

```ssh
mvn clean package
java -jar target/ncc.jar
```

### Docker

To build docker image a docker service must run on host system.

```ssh
mvn clean package docker:build
docker run -d -p 8080:8080 ncc/ncc 
```

## Testing and documentation
 
### Swagger UI

After starting the application, please open [swagger ui](http://localhost:8080/) access the rest endpoints documentation.
 
### Test coverage

```ssh
mvn clean cobertura:cobertura
```

and open ```target/site/cobertura/index.html``` in your browser. 