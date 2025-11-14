# Projet Pratique : Développement d'une Application Client/Serveur avec API REST

**Auteur :** KIBAMBE KABULULU Nathan  
**Promotion :** MASTER1 MIAGE  
**Année Académique :** 2024-2025  
**Matière :** Programmation Client/Serveur

---

## Table des Matières

1. [Introduction](#introduction)
2. [Contexte du Projet](#contexte-du-projet)
3. [Architecture et Technologies Utilisées](#architecture-et-technologies-utilisées)
4. [Développement du Projet](#développement-du-projet)
5. [Modèle de Données](#modèle-de-données)
6. [Implémentation des Fonctionnalités](#implémentation-des-fonctionnalités)
7. [Tests avec Postman](#tests-avec-postman)
8. [Résultats et Conclusion](#résultats-et-conclusion)

---

## Introduction

Dans le cadre du cours de Programmation Client/Serveur, j'ai été chargé de développer un système de gestion scolaire  utilisant Spring Boot et une API REST. Ce projet m'a permis de mettre en pratique les concepts de développement backend, de gestion de bases de données relationnelles, et de création d'APIs RESTful.

Ce document présente de manière détaillée toutes les étapes que j'ai suivies pour réaliser ce projet, depuis la création du projet jusqu'aux tests finaux avec Postman.

---

## Contexte du Projet

### Objectif

Créer un système de gestion scolaire permettant de gérer :
- Les étudiants
- Les cours
- Les inscriptions des étudiants aux cours

### Contraintes Techniques

- Utilisation de Spring Boot avec les dépendances : Spring Web, Spring Data JPA, MySQL Driver, Lombok
- Configuration de la connexion à MySQL
- Création d'entités JPA avec relations appropriées
- Développement d'une API REST complète avec gestion des erreurs
- Tests de tous les endpoints avec Postman

### Critères d'Évaluation

- Organisation du projet : 2 pts
- Correctitude des entités et relations JPA : 4 pts
- Fonctionnalité des endpoints REST : 6 pts
- Gestion des erreurs (404, 400) : 2 pts
- Clarté du code et commentaires : 2 pts

**Total : 16 points**

---

## Architecture et Technologies Utilisées

### Stack Technique

J'ai choisi d'utiliser les technologies suivantes pour ce projet :

- **Spring Boot 3.2.0** : Framework Java pour le développement d'applications
- **Java 17** : Langage de programmation (JDK 17)
- **Spring Data JPA** : Pour la gestion de la persistance des données
- **MySQL/MariaDB** : Base de données relationnelle
- **Lombok** : Pour réduire le code boilerplate
- **Maven** : Gestionnaire de dépendances et outil de build
- **Postman** : Pour tester l'API REST

### Structure du Projet

J'ai organisé mon projet selon l'architecture MVC (Model-View-Controller) recommandée par Spring Boot :

```
gestion_scolaire/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ecole/gestion_scolaire/
│   │   │       ├── model/          # Entités JPA
│   │   │       ├── repository/     # Repositories Spring Data
│   │   │       ├── controller/     # Contrôleurs REST
│   │   │       └── GestionScolaireApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

---

## Développement du Projet

### Étape 1 : Création du Projet Spring Boot

J'ai commencé par créer le projet sur le site [Spring Initializr](https://start.spring.io) avec les paramètres suivants :

- **Project** : Maven Project
- **Language** : Java
- **Spring Boot** : Version 3.2.0
- **Project Metadata** :
  - Group : `com.ecole`
  - Artifact : `gestion_scolaire`
  - Package name : `com.ecole.gestion_scolaire`
  - Packaging : Jar
  - Java : 17

**Dépendances ajoutées :**
- Spring Web
- Spring Data JPA
- MySQL Driver
- Lombok

Une fois le projet généré, je l'ai extrait dans mon workspace.

### Étape 2 : Configuration de la Base de Données

J'ai configuré la connexion à MySQL dans le fichier `application.properties` :

```properties
spring.application.name=gestion_scolaire

# Configuration de la base de données MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_scolaire?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=

# Configuration JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true
```

**Explications de la configuration :**
- `createDatabaseIfNotExist=true` : Crée automatiquement la base de données si elle n'existe pas
- `ddl-auto=update` : Met à jour automatiquement le schéma de la base de données
- `show-sql=true` : Affiche les requêtes SQL dans la console (utile pour le débogage)

### Étape 3 : Création des Entités JPA

J'ai créé trois entités JPA dans le package `model` :

#### 3.1. Entité Student (Étudiant)

```java
@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();
}
```

**Caractéristiques :**
- Clé primaire auto-incrémentée
- Email unique et obligatoire
- Relation One-to-Many avec Enrollment

#### 3.2. Entité Course (Cours)

```java
@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    private String description;

    @Column(nullable = false)
    private String professeur;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();
}
```

#### 3.3. Entité Enrollment (Inscription)

```java
@Entity
@Table(name = "enrollments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_inscription", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateInscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
```

**Relation Many-to-Many :**
Cette entité fait le lien entre Student et Course, créant une relation many-to-many avec une table d'association enrichie (date d'inscription).

### Étape 4 : Création des Repositories

J'ai créé trois interfaces Repository qui étendent `JpaRepository` :

#### 4.1. StudentRepository

```java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByEmail(String email);
    boolean existsByEmail(String email);
}
```

#### 4.2. CourseRepository

```java
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByTitre(String titre);
    List<Course> findByProfesseur(String professeur);
}
```

#### 4.3. EnrollmentRepository

```java
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(Student student);
    List<Enrollment> findByCourse(Course course);
    boolean existsByStudentAndCourse(Student student, Course course);
    Enrollment findByStudentAndCourse(Student student, Course course);
}
```

**Avantages de Spring Data JPA :**
- Méthodes CRUD automatiques (save, findAll, findById, deleteById, etc.)
- Génération automatique des requêtes SQL à partir des noms de méthodes
- Réduction significative du code à écrire

### Étape 5 : Développement des Contrôleurs REST

J'ai créé trois contrôleurs REST pour exposer l'API :

#### 5.1. StudentController

Ce contrôleur gère toutes les opérations CRUD sur les étudiants :

- **POST /api/students** : Créer un étudiant (201 Created)
- **GET /api/students** : Lister tous les étudiants (200 OK)
- **GET /api/students/{id}** : Obtenir un étudiant par ID (200 OK ou 404 Not Found)
- **PUT /api/students/{id}** : Modifier un étudiant (200 OK ou 404 Not Found)
- **DELETE /api/students/{id}** : Supprimer un étudiant (200 OK ou 404 Not Found)
- **GET /api/students/{id}/courses** : Lister les cours d'un étudiant (200 OK)

**Gestion des erreurs :**
- Vérification de l'unicité de l'email avant création
- Retour de 400 Bad Request si l'email existe déjà
- Retour de 404 Not Found si l'étudiant n'existe pas

#### 5.2. CourseController

Ce contrôleur gère toutes les opérations CRUD sur les cours :

- **POST /api/courses** : Créer un cours (201 Created)
- **GET /api/courses** : Lister tous les cours (200 OK)
- **GET /api/courses/{id}** : Obtenir un cours par ID (200 OK ou 404 Not Found)
- **PUT /api/courses/{id}** : Modifier un cours (200 OK ou 404 Not Found)
- **DELETE /api/courses/{id}** : Supprimer un cours (200 OK ou 404 Not Found)
- **GET /api/courses/{id}/students** : Lister les étudiants d'un cours (200 OK)

#### 5.3. EnrollmentController

Ce contrôleur gère les inscriptions :

- **POST /api/enrollments** : Inscrire un étudiant à un cours (201 Created)
- **GET /api/enrollments** : Lister toutes les inscriptions (200 OK)
- **GET /api/enrollments/{id}** : Obtenir une inscription par ID (200 OK ou 404 Not Found)
- **DELETE /api/enrollments/{id}** : Supprimer une inscription (200 OK ou 404 Not Found)

**Validations :**
- Vérification de l'existence de l'étudiant et du cours
- Vérification qu'un étudiant n'est pas déjà inscrit au même cours
- Retour de 400 Bad Request si l'inscription existe déjà

### Étape 6 : Gestion des Erreurs

J'ai implémenté une gestion complète des erreurs HTTP :

- **200 OK** : Opération réussie (GET, PUT, DELETE)
- **201 Created** : Ressource créée avec succès (POST)
- **400 Bad Request** : Erreur de validation ou données invalides
- **404 Not Found** : Ressource non trouvée

Chaque méthode du contrôleur vérifie les conditions et retourne le code HTTP approprié avec un message d'erreur explicite.

---

## Modèle de Données

### Schéma Relationnel

Le modèle de données suit une relation many-to-many entre Student et Course, implémentée via la table d'association Enrollment :

```
Student (1) ────────< Enrollment >─────── (1) Course
```

### Tables Créées

Hibernate a automatiquement créé les tables suivantes :

1. **students**
   - id (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
   - nom (VARCHAR(255), NOT NULL)
   - email (VARCHAR(255), NOT NULL, UNIQUE)
   - telephone (VARCHAR(255))

2. **courses**
   - id (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
   - titre (VARCHAR(255), NOT NULL)
   - description (VARCHAR(255))
   - professeur (VARCHAR(255), NOT NULL)

3. **enrollments**
   - id (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
   - date_inscription (DATE, NOT NULL)
   - student_id (BIGINT, NOT NULL, FOREIGN KEY)
   - course_id (BIGINT, NOT NULL, FOREIGN KEY)

---

## Implémentation des Fonctionnalités

### Fonctionnalités Implémentées

✅ **CRUD complet pour les étudiants**
- Création, lecture, mise à jour, suppression
- Validation de l'unicité de l'email
- Liste des cours d'un étudiant

✅ **CRUD complet pour les cours**
- Création, lecture, mise à jour, suppression
- Liste des étudiants inscrits à un cours

✅ **Gestion des inscriptions**
- Inscription d'un étudiant à un cours
- Vérification des doublons
- Suppression d'inscription

✅ **Gestion des erreurs**
- Codes HTTP appropriés (200, 201, 400, 404)
- Messages d'erreur explicites

✅ **Code bien structuré**
- Architecture MVC respectée
- Commentaires Javadoc sur toutes les méthodes
- Utilisation de Lombok pour réduire le code boilerplate

---

## Tests avec Postman

### Configuration de Postman

J'ai créé une collection Postman nommée "Gestion Scolaire API" pour organiser tous mes tests. Voici les tests que j'ai effectués :

### Test 1 : Création d'un Étudiant

**Requête :** POST `http://localhost:8080/api/students`

**Body :**
```json
{
  "nom": "Marie Martin",
  "email": "marie.martin@email.com",
  "telephone": "0123456789"
}
```

**Résultat :** 201 Created

![Création d'étudiant](images/creer%20etudiant%20postman.jpg)

### Test 2 : Liste des Étudiants

**Requête :** GET `http://localhost:8080/api/students`

**Résultat :** 200 OK avec la liste des étudiants

![Liste des étudiants](images/post%20etudiant.jpg)

### Test 3 : Obtenir un Étudiant par ID

**Requête :** GET `http://localhost:8080/api/students/1`

**Résultat :** 200 OK avec les détails de l'étudiant

![Get étudiant par ID](images/get%20etudiant%20par%20id.jpg)

### Test 4 : Création d'un Cours

**Requête :** POST `http://localhost:8080/api/courses`

**Body :**
```json
{
  "titre": "Mathématiques",
  "description": "Cours de mathématiques niveau 1",
  "professeur": "M. Martin"
}
```

**Résultat :** 201 Created

![Création de cours](images/creer%20cours.jpg)

### Test 5 : Liste des Cours

**Requête :** GET `http://localhost:8080/api/courses`

**Résultat :** 200 OK avec la liste des cours

![Liste des cours](images/listes%20des%20cours.jpg)

### Test 6 : Inscription d'un Étudiant à un Cours

**Requête :** POST `http://localhost:8080/api/enrollments`

**Body :**
```json
{
  "studentId": 1,
  "courseId": 1,
  "dateInscription": "2024-01-15"
}
```

**Résultat :** 201 Created

![Inscription étudiant](images/inscription%20etudiant.jpg)

### Test 7 : Liste des Cours d'un Étudiant

**Requête :** GET `http://localhost:8080/api/students/1/courses`

**Résultat :** 200 OK avec la liste des cours auxquels l'étudiant est inscrit

![Liste des cours par étudiant](images/liste%20des%20cours%20par%20etudiant.jpg)

### Test 8 : Modification d'un Étudiant

**Requête :** PUT `http://localhost:8080/api/students/1`

**Body :**
```json
{
  "nom": "Marie Martin Modifiée",
  "email": "marie.martin@email.com",
  "telephone": "0999999999"
}
```

**Résultat :** 200 OK avec l'étudiant modifié

![Modification étudiant](images/modification%20etudiant.jpg)

### Test 9 : Test d'Erreur 404

**Requête :** GET `http://localhost:8080/api/students/999`

**Résultat :** 404 Not Found avec message d'erreur

![Erreur 404](images/erreur%20404.jpg)

### Test 10 : Test d'Erreur 400 (Email existant)

**Requête :** POST `http://localhost:8080/api/students`

**Body :**
```json
{
  "nom": "Test",
  "email": "marie.martin@email.com",
  "telephone": "0000000000"
}
```

**Résultat :** 400 Bad Request avec message d'erreur

![Erreur 400 email](images/erreur%20de%20l'email%20400.jpg)

### Test 11 : Suppression d'une Inscription

**Requête :** DELETE `http://localhost:8080/api/enrollments/1`

**Résultat :** 200 OK avec message de confirmation

![Suppression inscription](images/suppresion%20inscriptions.jpg)

### Résumé des Tests

Tous les endpoints ont été testés avec succès :
- ✅ Création, lecture, modification, suppression pour toutes les entités
- ✅ Gestion des relations (inscriptions)
- ✅ Gestion des erreurs (404, 400)
- ✅ Validation des données

---

## Résultats et Conclusion

### Résultats Obtenus

J'ai réussi à développer un système de gestion scolaire complet avec les fonctionnalités suivantes :

1. **Architecture propre et bien organisée**
   - Respect de l'architecture MVC
   - Code structuré et commenté
   - Utilisation des bonnes pratiques Spring Boot

2. **Modèle de données correct**
   - Relations JPA bien définies
   - Contraintes d'intégrité respectées
   - Tables créées automatiquement par Hibernate

3. **API REST complète**
   - Tous les endpoints CRUD implémentés
   - Gestion appropriée des codes HTTP
   - Messages d'erreur explicites

4. **Tests complets**
   - Tous les endpoints testés avec Postman
   - Gestion des cas d'erreur vérifiée
   - Documentation avec captures d'écran

### Difficultés Rencontrées

Au cours du développement, j'ai rencontré quelques difficultés :

1. **Configuration Java 17** : Initialement, j'avais Java 11 installé, mais Spring Boot 3.2.0 nécessite Java 17. J'ai dû installer Java 17 et configurer IntelliJ IDEA pour l'utiliser.

2. **Dépendances Maven** : J'ai eu des problèmes de téléchargement des dépendances. J'ai résolu cela en forçant la mise à jour avec l'option `-U` de Maven.

3. **Configuration MySQL** : J'ai dû m'assurer que la base de données était correctement configurée et que le serveur MySQL était démarré.

### Compétences Acquises

Ce projet m'a permis de développer plusieurs compétences :

- **Spring Boot** : Maîtrise du framework et de ses fonctionnalités
- **JPA/Hibernate** : Compréhension des relations entre entités
- **API REST** : Développement d'APIs RESTful complètes
- **Gestion d'erreurs** : Implémentation d'une gestion d'erreurs appropriée
- **Tests** : Utilisation de Postman pour tester les APIs
- **Maven** : Gestion des dépendances et compilation de projets

### Améliorations Possibles

Pour une version future, je pourrais améliorer le projet en ajoutant :

- Authentification et autorisation (Spring Security)
- Validation plus poussée des données
- Documentation API avec Swagger/OpenAPI
- Tests unitaires et d'intégration
- Pagination pour les listes
- Recherche et filtrage avancés

### Conclusion

Ce projet m'a permis de mettre en pratique les concepts théoriques appris en cours. J'ai développé une application complète et fonctionnelle qui respecte les bonnes pratiques de développement. L'expérience acquise me sera utile pour mes futurs projets professionnels.

Le système de gestion scolaire est maintenant opérationnel et prêt à être utilisé. Tous les endpoints fonctionnent correctement et la gestion des erreurs est en place.

---

## Annexes

### Structure Complète du Projet

```
gestion_scolaire/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ecole/gestion_scolaire/
│   │   │       ├── controller/
│   │   │       │   ├── CourseController.java
│   │   │       │   ├── EnrollmentController.java
│   │   │       │   └── StudentController.java
│   │   │       ├── model/
│   │   │       │   ├── Course.java
│   │   │       │   ├── Enrollment.java
│   │   │       │   └── Student.java
│   │   │       ├── repository/
│   │   │       │   ├── CourseRepository.java
│   │   │       │   ├── EnrollmentRepository.java
│   │   │       │   └── StudentRepository.java
│   │   │       └── GestionScolaireApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── images/
│   └── [captures d'écran Postman]
├── pom.xml
├── README.md
└── GUIDE_TEST_POSTMAN.md
```

### Liste des Endpoints

#### Étudiants
- `POST /api/students` - Créer un étudiant
- `GET /api/students` - Lister tous les étudiants
- `GET /api/students/{id}` - Obtenir un étudiant par ID
- `PUT /api/students/{id}` - Modifier un étudiant
- `DELETE /api/students/{id}` - Supprimer un étudiant
- `GET /api/students/{id}/courses` - Lister les cours d'un étudiant

#### Cours
- `POST /api/courses` - Créer un cours
- `GET /api/courses` - Lister tous les cours
- `GET /api/courses/{id}` - Obtenir un cours par ID
- `PUT /api/courses/{id}` - Modifier un cours
- `DELETE /api/courses/{id}` - Supprimer un cours
- `GET /api/courses/{id}/students` - Lister les étudiants d'un cours

#### Inscriptions
- `POST /api/enrollments` - Inscrire un étudiant à un cours
- `GET /api/enrollments` - Lister toutes les inscriptions
- `GET /api/enrollments/{id}` - Obtenir une inscription par ID
- `DELETE /api/enrollments/{id}` - Supprimer une inscription

---

**Projet réalisé par :** KIBAMBE KABULULU Nathan  
**Date de réalisation :** Novembre 2024  
**Version :** 1.0

