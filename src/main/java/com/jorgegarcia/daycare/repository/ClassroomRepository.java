package com.jorgegarcia.daycare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jorgegarcia.daycare.model.Classroom;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom,Long>{

  //_ (underscore indica navegar a campo interno)
  List<Classroom> findByChildren_Id(Long id);
  
}
