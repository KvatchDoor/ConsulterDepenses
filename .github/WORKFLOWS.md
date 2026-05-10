# GitHub Workflows - ConsulterDepenses

## 📋 Fichiers de Workflow Créés

Trois workflows GitHub Actions ont été configurés dans le dossier `.github/workflows/`:

### 1. **backend-ci.yml** - Pipeline Backend
**Déclenche sur :** Push/PR avec modifications dans `consulter-backend/**`

**Actions :**
- ✅ Setup Java 25
- ✅ Compilation Maven
- ✅ Tests unitaires
- ✅ Upload des artifacts (JAR)
- ✅ Upload des rapports de test

```yaml
Branches: main, develop
```

### 2. **frontend-ci.yml** - Pipeline Frontend
**Déclenche sur :** Push/PR avec modifications dans `consulter-frontend/**`

**Actions :**
- ✅ Setup Node.js 20.x
- ✅ Installation npm (ci)
- ✅ Lint du code
- ✅ Build Angular
- ✅ Tests unitaires
- ✅ Upload des artifacts (dist/)
- ✅ Upload des résultats de test

```yaml
Branches: main, develop
Node.js: 20.x
```

### 3. **ci.yml** - Pipeline Complet
**Déclenche sur :** Push sur main, PR sur main/develop

**Actions :**
- ✅ Build backend ET frontend en parallèle
- ✅ Upload des artifacts des deux projets
- ✅ Plus rapide car les jobs s'exécutent en parallèle

```yaml
Branches: main (push), main/develop (PR)
Jobs en parallèle: backend-build + frontend-build
```

---

## 🎯 Configuration

### Branches Surveillées
- `main` - Production
- `develop` - Développement

### Déclencheurs
- **Push** sur les branches principales
- **Pull Requests** sur les branches principales
- **Chemins conditionnels** - Les workflows backend/frontend se déclenchent uniquement si les fichiers du projet concerné sont modifiés

---

## 📊 Résultats des Workflows

Chaque workflow génère :
- ✅ **Build artifacts** - JAR (backend) ou dist/ (frontend)
- ✅ **Test reports** - Résultats des tests unitaires
- ✅ **Status badge** - Visible dans le README

---

## 🔗 Intégration

Les workflows sont prêts pour :
- ✅ GitHub Actions automatisé
- ✅ Notifications de succès/échec
- ✅ Artifacts stockés pour déploiement
- ✅ Rapports de test accessibles

---

## 📝 Pour Personnaliser

Si vous avez besoin de :
- Ajouter des notifications Slack
- Publier les artifacts
- Déployer automatiquement
- Ajouter des analyses de code (SonarQube)

Modifiez les fichiers YAML dans `.github/workflows/`


