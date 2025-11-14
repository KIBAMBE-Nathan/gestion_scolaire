package com.ecole.gestion_scolaire.repository;

import com.ecole.gestion_scolaire.model.Enrollment;
import com.ecole.gestion_scolaire.model.Student;
import com.ecole.gestion_scolaire.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Enrollment
 * Fournit les méthodes CRUD de base + méthodes personnalisées pour les requêtes
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    // Trouver toutes les inscriptions d'un étudiant
    List<Enrollment> findByStudent(Student student);
    
    // Trouver toutes les inscriptions d'un cours
    List<Enrollment> findByCourse(Course course);
    
    // Vérifier si un étudiant est déjà inscrit à un cours
    boolean existsByStudentAndCourse(Student student, Course course);
    
    // Trouver une inscription spécifique par étudiant et cours
    Enrollment findByStudentAndCourse(Student student, Course course);
}

