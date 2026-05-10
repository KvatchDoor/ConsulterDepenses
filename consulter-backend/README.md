# Consulter Backend

Backend API pour l'application Consulter Dépenses.

## Technologies

- **Java**: 25
- **Spring Boot**: 4.0.0
- **Build Tool**: Maven

## Prérequis

- Java 25+
- Maven 3.8+

## Installation

```bash
mvn clean install
```

## Démarrage

```bash
mvn spring-boot:run
```

Le serveur démarrera sur `http://localhost:8080`

## Endpoints

- `GET /api/health` - Vérifier l'état du serveur

## Structure du Projet

```
consulter-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/consulter/
│   │   │       ├── ConsulterBackendApplication.java
│   │   │       └── controller/
│   │   │           └── HealthController.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── .gitignore
```

