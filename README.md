# Gym CRM System

This project is a Gym Customer Relationship Management (CRM) system implemented using the Spring framework. It provides functionality to manage trainees, trainers, and training sessions.

## Table of Contents

- [Project Structure](#project-structure)
- [Dependencies](#dependencies)
- [Building and Running](#building-and-running)
- [Configuration](#configuration)
- [Data Storage](#data-storage)
- [Exception Handling](#exception-handling)
- [Usage](#usage)

## Project Structure

The project is structured into the following packages:

- **configs**: Contains configuration classes for Spring.
- **daos**: Data Access Object interfaces and implementations.
- **data**: Stores a JSON data file with example data for Map Storage.
- **exceptions**: Custom exceptions, such as `NotFoundException`.
- **facade**: Facade classes that integrate various services.
- **models**: Entity classes representing the domain model.
- **services**: Service classes for Trainee, Trainer, and Training entities.
- **storage**: Configuration and initialization of in-memory storage.

## Dependencies

The project relies on the following dependencies:

- **JUnit**: Testing framework.
- **Jakarta Persistence API (JPA)**: For Java persistence.
- **Spring Core, Beans, Context**: Core Spring dependencies.
- **Lombok**: Library to reduce boilerplate code.
- **Spring Boot Starter Test**: Spring Boot testing utilities.
- **Spring Boot Starter Data JPA**: Starter for Spring Data JPA.
- **SLF4J API and Simple**: Simple Logging Facade for Java.
- **Jackson Databind**: JSON data-binding library.


## Configuration

Application configuration is defined in the `ApplicationConfig` class, located in the `com.epamlearning.configs` package. Notable configurations include the path to the data file:
```json-datafile-properties
data.file.path=/com/epamlearning/data/data.json
```

## Data Storage

### InMemoryStorage
The `InMemoryStorage` class serves as an in-memory storage solution for the Gym CRM system. It is annotated with `@Component`, indicating that it is a Spring bean and can be injected into other components. This class provides separate maps to store instances of different entities, including trainees, trainers, training sessions, users, and training types.

### InMemoryStorageInitializer
The `InMemoryStorageInitializer` class is a Spring bean post-processor responsible for initializing the in-memory storage with data from a JSON file. It implements the `BeanPostProcessor` interface and reads the data file path from the `context.properties` file.

## Exception Handling

Custom exceptions, such as `NotFoundException`, are used for common not found errors for models.

## Usage

The application provides services to manage trainees, trainers, and training sessions.
