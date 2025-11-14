package com.ecole.gestion_scolaire.controller;

import com.ecole.gestion_scolaire.model.Course;
import com.ecole.gestion_scolaire.model.Enrollment;
import com.ecole.gestion_scolaire.model.Student;
import com.ecole.gestion_scolaire.repository.EnrollmentRepository;
import com.ecole.gestion_scolaire.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour gérer les opérations CRUD sur les étudiants
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*") // Permet les requêtes depuis n'importe quelle origine
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    /**
     * Ajouter un nouvel étudiant
     * POST /api/students
     * Retourne 201 (Created) si succès, 400 (Bad Request) si erreur de validation
     */
    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody Student student) {
        try {
            // Vérifier si l'email existe déjà
            if (studentRepository.existsByEmail(student.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur : Un étudiant avec cet email existe déjà.");
            }

            Student savedStudent = studentRepository.save(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erreur lors de la création : " + e.getMessage());
        }
    }

    /**
     * Lister tous les étudiants
     * GET /api/students
     * Retourne 200 (OK) avec la liste des étudiants
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(students);
    }

    /**
     * Obtenir un étudiant par son ID
     * GET /api/students/{id}
     * Retourne 200 (OK) si trouvé, 404 (Not Found) si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentRepository.findById(id);
        
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Erreur 404 : Étudiant avec l'ID " + id + " non trouvé.");
        }
    }

    /**
     * Modifier un étudiant existant
     * PUT /api/students/{id}
     * Retourne 200 (OK) si modifié, 404 (Not Found) si non trouvé, 400 (Bad Request) si erreur
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @Valid @RequestBody Student studentDetails) {
        try {
            Optional<Student> optionalStudent = studentRepository.findById(id);
            
            if (optionalStudent.isPresent()) {
                Student student = optionalStudent.get();
                
                // Vérifier si l'email est modifié et s'il existe déjà pour un autre étudiant
                if (!student.getEmail().equals(studentDetails.getEmail()) && 
                    studentRepository.existsByEmail(studentDetails.getEmail())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Erreur : Un autre étudiant avec cet email existe déjà.");
                }
                
                // Mettre à jour les champs
                student.setNom(studentDetails.getNom());
                student.setEmail(studentDetails.getEmail());
                student.setTelephone(studentDetails.getTelephone());
                
                Student updatedStudent = studentRepository.save(student);
                return ResponseEntity.ok(updatedStudent);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur 404 : Étudiant avec l'ID " + id + " non trouvé.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erreur lors de la modification : " + e.getMessage());
        }
    }

    /**
     * Supprimer un étudiant
     * DELETE /api/students/{id}
     * Retourne 200 (OK) si supprimé, 404 (Not Found) si non trouvé
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        Optional<Student> student = studentRepository.findById(id);
        
        if (student.isPresent()) {
            studentRepository.deleteById(id);
            return ResponseEntity.ok("Étudiant avec l'ID " + id + " a été supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Erreur 404 : Étudiant avec l'ID " + id + " non trouvé.");
        }
    }

    /**
     * Lister les cours auxquels un étudiant est inscrit
     * GET /api/students/{id}/courses
     * Retourne 200 (OK) avec la liste des cours, 404 si étudiant non trouvé
     */
    @GetMapping("/{id}/courses")
    public ResponseEntity<?> getCoursesByStudent(@PathVariable Long id) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Erreur 404 : Étudiant avec l'ID " + id + " non trouvé.");
        }

        Student student = studentOpt.get();
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        List<Course> courses = enrollments.stream()
            .map(Enrollment::getCourse)
            .collect(Collectors.toList());

        return ResponseEntity.ok(courses);
    }
}

