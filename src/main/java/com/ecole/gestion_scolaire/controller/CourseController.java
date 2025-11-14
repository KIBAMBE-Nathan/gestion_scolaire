package com.ecole.gestion_scolaire.controller;

import com.ecole.gestion_scolaire.model.Course;
import com.ecole.gestion_scolaire.model.Enrollment;
import com.ecole.gestion_scolaire.model.Student;
import com.ecole.gestion_scolaire.repository.CourseRepository;
import com.ecole.gestion_scolaire.repository.EnrollmentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour gérer les opérations CRUD sur les cours
 */
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*") // Permet les requêtes depuis n'importe quelle origine
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    /**
     * Ajouter un nouveau cours
     * POST /api/courses
     * Retourne 201 (Created) si succès, 400 (Bad Request) si erreur de validation
     */
    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody Course course) {
        try {
            Course savedCourse = courseRepository.save(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erreur lors de la création : " + e.getMessage());
        }
    }

    /**
     * Lister tous les cours
     * GET /api/courses
     * Retourne 200 (OK) avec la liste des cours
     */
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return ResponseEntity.ok(courses);
    }

    /**
     * Obtenir un cours par son ID
     * GET /api/courses/{id}
     * Retourne 200 (OK) si trouvé, 404 (Not Found) si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseRepository.findById(id);
        
        if (course.isPresent()) {
            return ResponseEntity.ok(course.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Erreur 404 : Cours avec l'ID " + id + " non trouvé.");
        }
    }

    /**
     * Modifier un cours existant
     * PUT /api/courses/{id}
     * Retourne 200 (OK) si modifié, 404 (Not Found) si non trouvé, 400 (Bad Request) si erreur
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @Valid @RequestBody Course courseDetails) {
        try {
            Optional<Course> optionalCourse = courseRepository.findById(id);
            
            if (optionalCourse.isPresent()) {
                Course course = optionalCourse.get();
                
                // Mettre à jour les champs
                course.setTitre(courseDetails.getTitre());
                course.setDescription(courseDetails.getDescription());
                course.setProfesseur(courseDetails.getProfesseur());
                
                Course updatedCourse = courseRepository.save(course);
                return ResponseEntity.ok(updatedCourse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur 404 : Cours avec l'ID " + id + " non trouvé.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erreur lors de la modification : " + e.getMessage());
        }
    }

    /**
     * Supprimer un cours
     * DELETE /api/courses/{id}
     * Retourne 200 (OK) si supprimé, 404 (Not Found) si non trouvé
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        Optional<Course> course = courseRepository.findById(id);
        
        if (course.isPresent()) {
            courseRepository.deleteById(id);
            return ResponseEntity.ok("Cours avec l'ID " + id + " a été supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Erreur 404 : Cours avec l'ID " + id + " non trouvé.");
        }
    }

    /**
     * Lister les étudiants inscrits à un cours
     * GET /api/courses/{id}/students
     * Retourne 200 (OK) avec la liste des étudiants, 404 si cours non trouvé
     */
    @GetMapping("/{id}/students")
    public ResponseEntity<?> getStudentsByCourse(@PathVariable Long id) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Erreur 404 : Cours avec l'ID " + id + " non trouvé.");
        }

        Course course = courseOpt.get();
        List<Enrollment> enrollments = enrollmentRepository.findByCourse(course);
        List<Student> students = enrollments.stream()
            .map(Enrollment::getStudent)
            .collect(Collectors.toList());

        return ResponseEntity.ok(students);
    }
}

