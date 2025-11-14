package com.ecole.gestion_scolaire.repository;

import com.ecole.gestion_scolaire.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Course
 * Fournit les méthodes CRUD de base + méthodes personnalisées si nécessaire
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    // Méthode pour trouver un cours par titre
    Course findByTitre(String titre);
    
    // Méthode pour trouver des cours par professeur
    java.util.List<Course> findByProfesseur(String professeur);
}

