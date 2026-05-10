# ConsulterDepenses

Un mono-repository pour consulter mes budgets avec un backend Spring Boot et un frontend Angular.

## Structure du Projet

```
ConsulterDepenses/
├── consulter-backend/          # API Backend Spring Boot 4
│   ├── src/
│   ├── pom.xml
│   └── README.md
├── consulter-frontend/         # Application Frontend Angular 21
│   ├── src/
│   ├── package.json
│   └── README.md
└── README.md                   # Ce fichier
```

## Technologies

### Backend
- **Java**: 25
- **Spring Boot**: 4.0.0
- **Build Tool**: Maven
- **Port**: 8080

### Frontend
- **Angular**: 21.x (Dernière version LTS)
- **TypeScript**: 5.x
- **Node.js**: 20+
- **npm**: 11.x
- **Port**: 4200

## Installation

### Backend

```bash
cd consulter-backend
mvn clean install
```

### Frontend

```bash
cd consulter-frontend
npm install
```

## Démarrage

### Backend

```bash
cd consulter-backend
mvn spring-boot:run
```

L'API sera disponible sur `http://localhost:8080/api`

### Frontend

```bash
cd consulter-frontend
npm start
```

L'application sera disponible sur `http://localhost:4200`

## Documentation

- [Backend Documentation](./consulter-backend/README.md)
- [Frontend Documentation](./consulter-frontend/README.md)

## Développement

Chaque projet a son propre `.gitignore` pour gérer les fichiers à ignorer.

Pour développer les deux services :
1. Ouvrir deux terminaux
2. Dans le premier, lancer le backend avec `mvn spring-boot:run`
3. Dans le second, lancer le frontend avec `npm start`
