package com.ecole.gestion_scolaire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Entité représentant une inscription d'un étudiant à un cours
 * Cette entité fait le lien entre Student et Course (relation Many-to-Many)
 */
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

    // Relation Many-to-One avec Student
    // Plusieurs inscriptions peuvent appartenir à un même étudiant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // Relation Many-to-One avec Course
    // Plusieurs inscriptions peuvent appartenir à un même cours
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}

