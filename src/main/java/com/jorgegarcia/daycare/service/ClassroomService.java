package com.jorgegarcia.daycare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jorgegarcia.daycare.exception.ResourceNotFoundException;
import com.jorgegarcia.daycare.model.Classroom;
import com.jorgegarcia.daycare.repository.ClassroomRepository;

@Service
public class ClassroomService {

  @Autowired
  private ClassroomRepository classRep;

  public List<Classroom> getAllClassrooms(){
    return classRep.findAll();
  }

  public Classroom getClassromByClassroomId(long classroomId){
    return classRep.findById(classroomId)
     .orElseThrow(() -> new ResourceNotFoundException("Classroom %d not found".formatted(classroomId)));
  }

  public List<Classroom> getClassroomByDaycareChildId(long daycareChildId){
    return classRep.findByChildrenId(daycareChildId);
  }
}
