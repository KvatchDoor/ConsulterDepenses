# Consulter Frontend

Frontend Angular pour l'application Consulter Dépenses.

## Technologies

- **Angular**: 21.x (Dernière version LTS)
- **TypeScript**: 5.x
- **Node.js**: 20+
- **npm**: 11.x

## Prérequis

- Node.js 20+
- npm 11+

## Installation

```bash
npm install
```

## Démarrage

Démarrer le serveur de développement :

```bash
npm start
```

L'application sera disponible sur `http://localhost:4200`

## Build

Créer une version de production :

```bash
npm run build
```

Le résultat sera dans le dossier `dist/`

## Tests

Exécuter les tests unitaires :

```bash
npm test
```

## Structure du Projet

```
consulter-frontend/
├── src/
│   ├── app/
│   │   ├── app.ts
│   │   ├── app.config.ts
│   │   ├── app.routes.ts
│   │   └── app.css
│   ├── index.html
│   ├── styles.css
│   └── main.ts
├── angular.json
├── package.json
├── tsconfig.json
└── .gitignore
```

## Configuration

- **Port par défaut**: 4200
- **Base URL API**: À configurer dans les services

## Code scaffolding

Angular CLI inclut de puissants outils de scaffolding. Pour générer un nouveau composant, exécutez :

```bash
ng generate component component-name
```

Pour la liste complète des schémas disponibles, exécutez :

```bash
ng generate --help
```

