package com.jorgegarcia.daycare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jorgegarcia.daycare.exception.ResourceNotFoundException;
import com.jorgegarcia.daycare.model.DaycareChild;
import com.jorgegarcia.daycare.repository.DaycareChildRepository;

@Service
public class DaycareChildService {

  @Autowired
  DaycareChildRepository daycarekids;

  public List<DaycareChild> getAllChildren(){

    return daycarekids.findAll();
  }

  public DaycareChild getChildById(Long id){
    x
    return daycarekids.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Child %d not found".formatted(id)));
  }

  public List<DaycareChild> getChildrenByClassroomId(Long classroomId){
    return daycarekids.findByClassroom_Id(classroomId);
  }

  public DaycareChild addDaycareChildren(DaycareChild child){
    return daycarekids.save(child);
  }
  public void  deleteDaycareChild(long childId){
    daycarekids.deleteById(childId);
  }
}
