package com.jorgegarcia.daycare.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DaycareChild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private int age;

    // Each child belongs to one classroom
    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
}
