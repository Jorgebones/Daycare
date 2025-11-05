package com.jorgegarcia.daycare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jorgegarcia.daycare.exception.ResourceNotFoundException;
import com.jorgegarcia.daycare.model.DaycareChild;
import com.jorgegarcia.daycare.repository.ClassroomRepository;
import com.jorgegarcia.daycare.repository.DaycareChildRepository;
import com.jorgegarcia.daycare.repository.TeacherRepository;

@Service
public class DaycareChildService {

  @Autowired
  private TeacherRepository teacherRep;

  @Autowired
  private ClassroomRepository classRep;

  @Autowired
  DaycareChildRepository daycarekids;

  public List<DaycareChild> getAllChildren(){

    return daycarekids.findAll();
  }

  public DaycareChild getChildById(Long id){
    
    return daycarekids.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Child %d not found".formatted(id)));
  }
  
}
