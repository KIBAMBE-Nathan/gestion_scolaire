#  Système de Gestion Scolaire - API REST

Application Spring Boot pour la gestion d'un système scolaire avec API REST.

##  Description

Cette application permet de gérer :
- **Les étudiants** : CRUD complet
- **Les cours** : CRUD complet
- **Les inscriptions** : Inscription d'étudiants aux cours

## 🛠 Technologies Utilisées

- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **MySQL**
- **Lombok**
- **Java 17**

##  Structure du Projet

```
gestion_scolaire/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ecole/gestion_scolaire/
│   │   │       ├── model/          # Entités JPA
│   │   │       │   ├── Student.java
│   │   │       │   ├── Course.java
│   │   │       │   └── Enrollment.java
│   │   │       ├── repository/     # Repositories
│   │   │       │   ├── StudentRepository.java
│   │   │       │   ├── CourseRepository.java
│   │   │       │   └── EnrollmentRepository.java
│   │   │       ├── controller/     # Contrôleurs REST
│   │   │       │   ├── StudentController.java
│   │   │       │   ├── CourseController.java
│   │   │       │   └── EnrollmentController.java
│   │   │       └── GestionScolaireApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## 🗄 Modèle de Données

### Entité Student (Étudiant)
- `id` (Long) - Clé primaire
- `nom` (String) - Nom de l'étudiant
- `email` (String) - Email unique
- `telephone` (String) - Numéro de téléphone

### Entité Course (Cours)
- `id` (Long) - Clé primaire
- `titre` (String) - Titre du cours
- `description` (String) - Description du cours
- `professeur` (String) - Nom du professeur

### Entité Enrollment (Inscription)
- `id` (Long) - Clé primaire
- `dateInscription` (Date) - Date d'inscription
- `student` (Student) - Référence vers l'étudiant
- `course` (Course) - Référence vers le cours

**Relation** : Many-to-Many entre Student et Course via Enrollment

## ⚙ Configuration

### Base de Données MySQL

Modifiez `src/main/resources/application.properties` selon votre configuration :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_scolaire?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe
```

La base de données `gestion_scolaire` sera créée automatiquement au démarrage.

##  Démarrage de l'Application

### Prérequis
- Java 17 ou supérieur
- Maven
- MySQL installé et démarré

### Méthode 1 : Via IDE
1. Ouvrez le projet dans votre IDE (IntelliJ, Eclipse, VS Code)
2. Trouvez `GestionScolaireApplication.java`
3. Clic droit → Run ou Debug

### Méthode 2 : Via ligne de commande
```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

L'application sera accessible sur : `http://localhost:8080`

##  Endpoints API

### Base URL
```
http://localhost:8080/api
```

### Étudiants (`/api/students`)
- `POST /api/students` - Créer un étudiant
- `GET /api/students` - Lister tous les étudiants
- `GET /api/students/{id}` - Obtenir un étudiant par ID
- `PUT /api/students/{id}` - Modifier un étudiant
- `DELETE /api/students/{id}` - Supprimer un étudiant
- `GET /api/students/{id}/courses` - Lister les cours d'un étudiant

### Cours (`/api/courses`)
- `POST /api/courses` - Créer un cours
- `GET /api/courses` - Lister tous les cours
- `GET /api/courses/{id}` - Obtenir un cours par ID
- `PUT /api/courses/{id}` - Modifier un cours
- `DELETE /api/courses/{id}` - Supprimer un cours
- `GET /api/courses/{id}/students` - Lister les étudiants d'un cours

### Inscriptions (`/api/enrollments`)
- `POST /api/enrollments` - Inscrire un étudiant à un cours
- `GET /api/enrollments` - Lister toutes les inscriptions
- `GET /api/enrollments/{id}` - Obtenir une inscription par ID
- `DELETE /api/enrollments/{id}` - Supprimer une inscription

##  Tests avec Postman

Consultez le fichier **GUIDE_TEST_POSTMAN.md** pour un guide détaillé avec tous les exemples de requêtes.

##  Codes de Réponse HTTP

- **200 OK** : Opération réussie
- **201 Created** : Ressource créée avec succès
- **400 Bad Request** : Erreur de validation ou données invalides
- **404 Not Found** : Ressource non trouvée

##  Exemples de Requêtes

### Créer un étudiant
```bash
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "nom": "Jean Dupont",
  "email": "jean.dupont@email.com",
  "telephone": "0123456789"
}
```

### Inscrire un étudiant à un cours
```bash
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
  "studentId": 1,
  "courseId": 1,
  "dateInscription": "2024-01-15"
}
```

##  Fonctionnalités Implémentées

✅ CRUD complet pour les étudiants  
✅ CRUD complet pour les cours  
✅ Gestion des inscriptions  
✅ Relations JPA (Many-to-Many via table d'association)  
✅ Gestion des erreurs (400, 404)  
✅ Validation des données  
✅ Code commenté et organisé  

##  Auteur

Projet réalisé dans le cadre du cours de Programmation Client/Serveur

##  Documentation

- [Guide de Test Postman](GUIDE_TEST_POSTMAN.md)

