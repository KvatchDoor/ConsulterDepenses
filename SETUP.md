# Guide d'Installation et Démarrage - ConsulterDepenses

## ✅ Projets Créés

### 1. **consulter-backend**
- **Framework**: Spring Boot 4.0.0
- **Langage**: Java 25
- **Build**: Maven
- **Port**: 8080
- **Location**: `/consulter-backend`

#### Installation Backend
```bash
cd consulter-backend
mvn clean install
```

#### Démarrage Backend
```bash
cd consulter-backend
mvn spring-boot:run
```

**API disponible**: http://localhost:8080/api
**Endpoint santé**: http://localhost:8080/api/health

---

### 2. **consulter-frontend**
- **Framework**: Angular 21.x (Dernière version LTS)
- **Langage**: TypeScript 5.x
- **Build Tool**: npm
- **Port**: 4200
- **Location**: `/consulter-frontend`

#### Installation Frontend
```bash
cd consulter-frontend
npm install
```

#### Démarrage Frontend
```bash
cd consulter-frontend
npm start
```

**Application disponible**: http://localhost:4200

---

## 📋 Structure du Mono-Repository

```
ConsulterDepenses/
├── .git/                       # Repository Git
├── .github/                    # GitHub workflows (si applicable)
├── .idea/                      # Fichiers IDE
│
├── consulter-backend/          # Backend Spring Boot 4
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/consulter/
│   │   │   │   ├── ConsulterBackendApplication.java
│   │   │   │   └── controller/
│   │   │   │       └── HealthController.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   ├── pom.xml                 # Configuration Maven
│   ├── .gitignore              # Exclusions Git (backend)
│   └── README.md               # Documentation backend
│
├── consulter-frontend/         # Frontend Angular 21
│   ├── src/
│   │   ├── app/                # Composants Angular
│   │   ├── index.html
│   │   └── main.ts
│   ├── angular.json            # Configuration Angular
│   ├── package.json            # Dépendances npm
│   ├── tsconfig.json           # Configuration TypeScript
│   ├── .gitignore              # Exclusions Git (frontend)
│   ├── .prettierrc             # Configuration Prettier
│   ├── .editorconfig           # Configuration éditeur
│   └── README.md               # Documentation frontend
│
├── README.md                   # Documentation principale
└── SETUP.md                    # Ce fichier
```

---

## 🚀 Démarrage Complet (2 Terminaux)

### Terminal 1 - Backend
```bash
cd C:\DEV\repository\ConsulterDepenses\consulter-backend
mvn spring-boot:run
```

### Terminal 2 - Frontend
```bash
cd C:\DEV\repository\ConsulterDepenses\consulter-frontend
npm start
```

Puis ouvrir:
- **Backend**: http://localhost:8080/api/health
- **Frontend**: http://localhost:4200

---

## 📦 Prérequis

### Pour le Backend
- ✅ Java 25+
- ✅ Maven 3.8+

### Pour le Frontend
- ✅ Node.js 20+
- ✅ npm 11.x

---

## 📝 Notes Importantes

1. **Chaque projet a son propre `.gitignore`** pour gérer les fichiers à ignorer
2. **Les ports par défaut** sont 8080 (backend) et 4200 (frontend)
3. **La base de données** du backend utilise H2 en mémoire (configurable dans `application.properties`)
4. **Le frontend** inclut Server-Side Rendering (SSR) préconfiguré

---

## 🔧 Commandes Utiles

### Backend
```bash
# Compiler et installer les dépendances
mvn clean install

# Démarrer le serveur
mvn spring-boot:run

# Exécuter les tests
mvn test

# Build pour production
mvn clean package
```

### Frontend
```bash
# Installer les dépendances
npm install

# Démarrer le serveur de développement
npm start

# Build pour production
npm run build

# Exécuter les tests
npm test

# Générer un nouveau composant
ng generate component component-name
```

---

## 📖 Documentation

- [Backend - README.md](./consulter-backend/README.md)
- [Frontend - README.md](./consulter-frontend/README.md)
- [Racine - README.md](./README.md)

---

**Bon développement! 🎉**

