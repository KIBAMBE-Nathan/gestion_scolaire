package com.ecole.gestion_scolaire.repository;

import com.ecole.gestion_scolaire.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Student
 * Fournit les méthodes CRUD de base + méthodes personnalisées si nécessaire
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Méthode pour trouver un étudiant par email
    Student findByEmail(String email);
    
    // Méthode pour vérifier si un email existe déjà
    boolean existsByEmail(String email);
}

