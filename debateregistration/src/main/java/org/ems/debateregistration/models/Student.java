package org.ems.debateregistration.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "First Name is mandatory")
    private String firstName;
    @NotBlank(message = "Last Name is mandatory")
    private String lastName;
    @NotBlank(message = "Course is mandatory")
    private String course;
    @NotBlank(message = "Country is mandatory")
    private String country;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp created_at;


    @Transient
    private boolean isNew = true;

}
