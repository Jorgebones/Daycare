package com.jorgegarcia.daycare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jorgegarcia.daycare.model.DaycareChild;
@Repository
public interface DaycareChildRepository extends JpaRepository<DaycareChild, Long>{
  
}
