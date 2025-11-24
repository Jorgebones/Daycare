package com.jorgegarcia.daycare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jorgegarcia.daycare.model.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long>{

  Optional<Teacher> findByEmail(String email);
  
}
