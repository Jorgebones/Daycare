package com.jorgegarcia.daycare.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className;

    // Each class has one teacher, declared so JPA build the relations
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    // Each class can have multiple children
    //mappedBy referes to the name of the field inside DayCareChild
    //is the JOIN ON, cascade to update it if clasroom, saved, delted or updated
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    private List<DaycareChild> children;
}
