package com.ecole.gestion_scolaire.controller;

import com.ecole.gestion_scolaire.model.Course;
import com.ecole.gestion_scolaire.model.Enrollment;
import com.ecole.gestion_scolaire.model.Student;
import com.ecole.gestion_scolaire.repository.CourseRepository;
import com.ecole.gestion_scolaire.repository.EnrollmentRepository;
import com.ecole.gestion_scolaire.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour gérer les inscriptions (Enrollment)
 */
@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Inscrire un étudiant à un cours
     * POST /api/enrollments
     * Body: { "studentId": 1, "courseId": 1, "dateInscription": "2024-01-15" }
     * Retourne 201 (Created) si succès, 400 (Bad Request) ou 404 (Not Found) si erreur
     */
    @PostMapping
    public ResponseEntity<?> enrollStudentToCourse(@RequestBody EnrollmentRequest request) {
        try {
            // Vérifier que l'étudiant existe
            Optional<Student> studentOpt = studentRepository.findById(request.getStudentId());
            if (studentOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur 404 : Étudiant avec l'ID " + request.getStudentId() + " non trouvé.");
            }

            // Vérifier que le cours existe
            Optional<Course> courseOpt = courseRepository.findById(request.getCourseId());
            if (courseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur 404 : Cours avec l'ID " + request.getCourseId() + " non trouvé.");
            }

            Student student = studentOpt.get();
            Course course = courseOpt.get();

            // Vérifier si l'étudiant est déjà inscrit à ce cours
            if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur 400 : L'étudiant est déjà inscrit à ce cours.");
            }

            // Créer l'inscription
            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            enrollment.setDateInscription(request.getDateInscription() != null 
                ? request.getDateInscription() 
                : new Date()); // Date actuelle par défaut

            Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEnrollment);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erreur lors de l'inscription : " + e.getMessage());
        }
    }

    /**
     * Lister tous les inscriptions
     * GET /api/enrollments
     * Retourne 200 (OK) avec la liste des inscriptions
     */
    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        return ResponseEntity.ok(enrollments);
    }

    /**
     * Obtenir une inscription par son ID
     * GET /api/enrollments/{id}
     * Retourne 200 (OK) si trouvé, 404 (Not Found) si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable Long id) {
        Optional<Enrollment> enrollment = enrollmentRepository.findById(id);
        
        if (enrollment.isPresent()) {
            return ResponseEntity.ok(enrollment.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Erreur 404 : Inscription avec l'ID " + id + " non trouvée.");
        }
    }

    /**
     * Supprimer une inscription
     * DELETE /api/enrollments/{id}
     * Retourne 200 (OK) si supprimé, 404 (Not Found) si non trouvé
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnrollment(@PathVariable Long id) {
        Optional<Enrollment> enrollment = enrollmentRepository.findById(id);
        
        if (enrollment.isPresent()) {
            enrollmentRepository.deleteById(id);
            return ResponseEntity.ok("Inscription avec l'ID " + id + " a été supprimée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Erreur 404 : Inscription avec l'ID " + id + " non trouvée.");
        }
    }

    /**
     * Lister les cours auxquels un étudiant est inscrit (endpoint alternatif)
     * GET /api/enrollments/students/{studentId}/courses
     * Retourne 200 (OK) avec la liste des cours, 404 si étudiant non trouvé
     */
    @GetMapping("/students/{studentId}/courses")
    public ResponseEntity<?> getCoursesByStudentFromEnrollment(@PathVariable Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Erreur 404 : Étudiant avec l'ID " + studentId + " non trouvé.");
        }

        Student student = studentOpt.get();
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        List<Course> courses = enrollments.stream()
            .map(Enrollment::getCourse)
            .collect(Collectors.toList());

        return ResponseEntity.ok(courses);
    }

    /**
     * Lister les étudiants inscrits à un cours (endpoint alternatif)
     * GET /api/enrollments/courses/{courseId}/students
     * Retourne 200 (OK) avec la liste des étudiants, 404 si cours non trouvé
     */
    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<?> getStudentsByCourseFromEnrollment(@PathVariable Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Erreur 404 : Cours avec l'ID " + courseId + " non trouvé.");
        }

        Course course = courseOpt.get();
        List<Enrollment> enrollments = enrollmentRepository.findByCourse(course);
        List<Student> students = enrollments.stream()
            .map(Enrollment::getStudent)
            .collect(Collectors.toList());

        return ResponseEntity.ok(students);
    }

    /**
     * Classe interne pour recevoir les données de requête d'inscription
     */
    public static class EnrollmentRequest {
        private Long studentId;
        private Long courseId;
        private Date dateInscription;

        // Getters et Setters
        public Long getStudentId() {
            return studentId;
        }

        public void setStudentId(Long studentId) {
            this.studentId = studentId;
        }

        public Long getCourseId() {
            return courseId;
        }

        public void setCourseId(Long courseId) {
            this.courseId = courseId;
        }

        public Date getDateInscription() {
            return dateInscription;
        }

        public void setDateInscription(Date dateInscription) {
            this.dateInscription = dateInscription;
        }
    }
}

