# Guide de Test avec Postman - API Gestion Scolaire

## 📋 Prérequis

1. **MySQL** doit être démarré et accessible
2. **Spring Boot** application doit être lancée (port 8080 par défaut)
3. **Postman** installé

## 🚀 Démarrer l'application

### Option 1 : Via IDE (IntelliJ, Eclipse, VS Code)
- Ouvrez le projet dans votre IDE
- Trouvez la classe `GestionScolaireApplication.java`
- Clic droit → Run ou Debug

### Option 2 : Via ligne de commande
```bash
cd "C:\Users\kibam\OneDrive\Documents\master1\programmation client serveur\gestion_scolaire"
mvnw spring-boot:run
```

L'application sera accessible sur : `http://localhost:8080`

---

## 📝 Endpoints à Tester

### 🎓 **ÉTUDIANTS** (`/api/students`)

#### 1. Créer un étudiant
- **Méthode** : `POST`
- **URL** : `http://localhost:8080/api/students`
- **Headers** : 
  - `Content-Type: application/json`
- **Body** (raw JSON) :
```json
{
  "nom": "Jean Dupont",
  "email": "jean.dupont@email.com",
  "telephone": "0123456789"
}
```
- **Réponse attendue** : `201 Created`
- **Exemple de réponse** :
```json
{
  "id": 1,
  "nom": "Jean Dupont",
  "email": "jean.dupont@email.com",
  "telephone": "0123456789",
  "enrollments": []
}
```

#### 2. Lister tous les étudiants
- **Méthode** : `GET`
- **URL** : `http://localhost:8080/api/students`
- **Réponse attendue** : `200 OK`
- **Exemple de réponse** :
```json
[
  {
    "id": 1,
    "nom": "Jean Dupont",
    "email": "jean.dupont@email.com",
    "telephone": "0123456789"
  }
]
```

#### 3. Obtenir un étudiant par ID
- **Méthode** : `GET`
- **URL** : `http://localhost:8080/api/students/1`
- **Réponse attendue** : `200 OK` (si trouvé) ou `404 Not Found` (si non trouvé)

#### 4. Modifier un étudiant
- **Méthode** : `PUT`
- **URL** : `http://localhost:8080/api/students/1`
- **Headers** : 
  - `Content-Type: application/json`
- **Body** (raw JSON) :
```json
{
  "nom": "Jean Dupont Modifié",
  "email": "jean.dupont@email.com",
  "telephone": "0987654321"
}
```
- **Réponse attendue** : `200 OK` ou `404 Not Found`

#### 5. Supprimer un étudiant
- **Méthode** : `DELETE`
- **URL** : `http://localhost:8080/api/students/1`
- **Réponse attendue** : `200 OK` ou `404 Not Found`

#### 6. Lister les cours d'un étudiant
- **Méthode** : `GET`
- **URL** : `http://localhost:8080/api/students/1/courses`
- **Réponse attendue** : `200 OK` avec la liste des cours

---

### 📚 **COURS** (`/api/courses`)

#### 1. Créer un cours
- **Méthode** : `POST`
- **URL** : `http://localhost:8080/api/courses`
- **Headers** : 
  - `Content-Type: application/json`
- **Body** (raw JSON) :
```json
{
  "titre": "Mathématiques",
  "description": "Cours de mathématiques niveau 1",
  "professeur": "M. Martin"
}
```
- **Réponse attendue** : `201 Created`

#### 2. Lister tous les cours
- **Méthode** : `GET`
- **URL** : `http://localhost:8080/api/courses`
- **Réponse attendue** : `200 OK`

#### 3. Obtenir un cours par ID
- **Méthode** : `GET`
- **URL** : `http://localhost:8080/api/courses/1`
- **Réponse attendue** : `200 OK` ou `404 Not Found`

#### 4. Modifier un cours
- **Méthode** : `PUT`
- **URL** : `http://localhost:8080/api/courses/1`
- **Headers** : 
  - `Content-Type: application/json`
- **Body** (raw JSON) :
```json
{
  "titre": "Mathématiques Avancées",
  "description": "Cours de mathématiques niveau 2",
  "professeur": "M. Martin"
}
```
- **Réponse attendue** : `200 OK` ou `404 Not Found`

#### 5. Supprimer un cours
- **Méthode** : `DELETE`
- **URL** : `http://localhost:8080/api/courses/1`
- **Réponse attendue** : `200 OK` ou `404 Not Found`

#### 6. Lister les étudiants d'un cours
- **Méthode** : `GET`
- **URL** : `http://localhost:8080/api/courses/1/students`
- **Réponse attendue** : `200 OK` avec la liste des étudiants

---

### 📝 **INSCRIPTIONS** (`/api/enrollments`)

#### 1. Inscrire un étudiant à un cours
- **Méthode** : `POST`
- **URL** : `http://localhost:8080/api/enrollments`
- **Headers** : 
  - `Content-Type: application/json`
- **Body** (raw JSON) :
```json
{
  "studentId": 1,
  "courseId": 1,
  "dateInscription": "2024-01-15"
}
```
- **Réponse attendue** : `201 Created`
- **Note** : Si l'étudiant est déjà inscrit, vous obtiendrez `400 Bad Request`

#### 2. Lister toutes les inscriptions
- **Méthode** : `GET`
- **URL** : `http://localhost:8080/api/enrollments`
- **Réponse attendue** : `200 OK`

#### 3. Obtenir une inscription par ID
- **Méthode** : `GET`
- **URL** : `http://localhost:8080/api/enrollments/1`
- **Réponse attendue** : `200 OK` ou `404 Not Found`

#### 4. Supprimer une inscription
- **Méthode** : `DELETE`
- **URL** : `http://localhost:8080/api/enrollments/1`
- **Réponse attendue** : `200 OK` ou `404 Not Found`

---

## 🧪 Scénario de Test Complet

### Étape 1 : Créer des étudiants
1. POST `/api/students` avec :
   - Étudiant 1 : "Marie Martin", "marie.martin@email.com", "0123456789"
   - Étudiant 2 : "Pierre Durand", "pierre.durand@email.com", "0987654321"

### Étape 2 : Créer des cours
1. POST `/api/courses` avec :
   - Cours 1 : "Mathématiques", "Cours de math", "M. Martin"
   - Cours 2 : "Français", "Cours de français", "Mme. Dupont"

### Étape 3 : Lister les étudiants et cours
1. GET `/api/students` → Vérifier que les 2 étudiants apparaissent
2. GET `/api/courses` → Vérifier que les 2 cours apparaissent

### Étape 4 : Inscrire des étudiants aux cours
1. POST `/api/enrollments` avec :
   - Étudiant 1 → Cours 1
   - Étudiant 1 → Cours 2
   - Étudiant 2 → Cours 1

### Étape 5 : Tester les requêtes de listage
1. GET `/api/students/1/courses` → Doit retourner 2 cours (Math et Français)
2. GET `/api/courses/1/students` → Doit retourner 2 étudiants (Marie et Pierre)

### Étape 6 : Tester les erreurs
1. GET `/api/students/999` → Doit retourner `404 Not Found`
2. POST `/api/students` avec un email déjà existant → Doit retourner `400 Bad Request`
3. POST `/api/enrollments` avec un étudiant inexistant → Doit retourner `404 Not Found`

---

## ✅ Codes de Réponse HTTP

- **200 OK** : Opération réussie (GET, PUT, DELETE)
- **201 Created** : Ressource créée avec succès (POST)
- **400 Bad Request** : Erreur de validation ou données invalides
- **404 Not Found** : Ressource non trouvée

---

## 📸 Captures d'écran à Prendre

Pour votre livrable, prenez des captures d'écran de Postman montrant :

1. ✅ Création d'un étudiant (POST) - Réponse 201
2. ✅ Liste des étudiants (GET) - Réponse 200
3. ✅ Création d'un cours (POST) - Réponse 201
4. ✅ Inscription d'un étudiant à un cours (POST) - Réponse 201
5. ✅ Liste des cours d'un étudiant (GET) - Réponse 200
6. ✅ Liste des étudiants d'un cours (GET) - Réponse 200
7. ✅ Erreur 404 (GET sur un ID inexistant)
8. ✅ Erreur 400 (POST avec email déjà existant)

---

## 🔧 Dépannage

### L'application ne démarre pas
- Vérifiez que MySQL est démarré
- Vérifiez les identifiants MySQL dans `application.properties`
- Vérifiez que le port 8080 n'est pas déjà utilisé

### Erreur de connexion à MySQL
- Vérifiez que MySQL écoute sur le port 3306
- Vérifiez le username et password dans `application.properties`
- Créez manuellement la base de données `gestion_scolaire` si nécessaire

### Erreur 404 sur tous les endpoints
- Vérifiez que l'application est bien démarrée
- Vérifiez l'URL : doit commencer par `http://localhost:8080/api/...`

