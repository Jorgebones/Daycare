package com.jorgegarcia.daycare.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data // auto-generates getters, setters, equals, toString
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    // One teacher can teach multiple classes
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Classroom> classes;
}
