package com.jorgegarcia.daycare.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jorgegarcia.daycare.exception.ResourceNotFoundException;
import com.jorgegarcia.daycare.model.Classroom;
import com.jorgegarcia.daycare.model.DaycareChild;
import com.jorgegarcia.daycare.repository.ClassroomRepository;
import com.jorgegarcia.daycare.repository.DaycareChildRepository;
import com.jorgegarcia.daycare.dto.*;

@Service
public class DaycareChildService {

  private final DaycareChildRepository daycarekids;
  private final ClassroomRepository classRep;

  //Best practice to not use Autowired and insert it in the constructor
  public DaycareChildService(DaycareChildRepository daycarekids,ClassroomRepository classRep){
    this.daycarekids = daycarekids;
    this.classRep = classRep;
  }
  // ✅ Entity → DTO
  private DaycareChildDTO toDTO(DaycareChild child) {
    return DaycareChildDTO.builder()
        .id(child.getId())
        .firstName(child.getFirstName())
        .lastName(child.getLastName())
        .age(child.getAge())
        .classroom(
            ClassroomSummaryDTO.builder()
                .id(child.getClassroom().getId())
                .className(child.getClassroom().getClassName())
                .build()
        )
        .build();
  }
  // ✅ DTO → Entity
  private DaycareChild toEntity(DaycareChildDTO dto, Classroom classroom) {
    return DaycareChild.builder()
        .id(dto.getId())
        .firstName(dto.getFirstName())
        .lastName(dto.getLastName())
        .age(dto.getAge())
        .classroom(classroom)
        .build();
  }
  // ✅ Entity → SummaryDTO
  private DaycareChildSummaryDTO toSummary(DaycareChild child) {
    return DaycareChildSummaryDTO.builder()
        .id(child.getId())
        .fullName(child.getFirstName() + " " + child.getLastName())
        .build();
}

  public List<DaycareChildDTO> getAllChildren(){

    return daycarekids.findAll()
    .stream()
    .map(this::toDTO)
    .toList();
  }

  public DaycareChildDTO getChildById(Long id){
  
    DaycareChild child = daycarekids.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Child %d not found".formatted(id)));

    return toDTO(child);
  }

  public List<DaycareChildDTO> getChildrenByClassroomId(Long classroomId){

    return daycarekids.findByClassroom_Id(classroomId)
    .stream()
    .map(this::toDTO)
    .toList();
  }

  public DaycareChildDTO addDaycareChildren(DaycareChildDTO dto){
    // 1. find the classroom by ID
    Classroom classroom = classRep.findById(dto.getClassroom().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

    // 2. convert dto → entity
    DaycareChild entity = toEntity(dto, classroom);

    // 3. save entity
    DaycareChild saved = daycarekids.save(entity);

    // 4. return dto
    return toDTO(saved);
  }

  public void  deleteDaycareChild(Long childId){
    DaycareChild exists = daycarekids.findById(childId)
    .orElseThrow(() -> new ResourceNotFoundException("Child not found"));
    daycarekids.delete(exists);
  }


  public DaycareChildDTO updateChild(Long id, DaycareChildDTO dto) {
    DaycareChild existing = daycarekids.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Child " + id + " not found"));
    // find classroom
    Classroom classroom = classRep.findById(dto.getClassroom().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

    existing.setFirstName(dto.getFirstName());
    existing.setLastName(dto.getLastName());
    existing.setAge(dto.getAge());
    existing.setClassroom(classroom);

    DaycareChild updated = daycarekids.save(existing);

    return toDTO(updated);
  }


  public DaycareChildDTO patchChild(Long id, DaycareChildDTO dto) {
    DaycareChild existing = daycarekids.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Child " + id + " not found"));

     if (dto.getFirstName() != null) existing.setFirstName(dto.getFirstName());
    if (dto.getLastName() != null) existing.setLastName(dto.getLastName());
    if (dto.getAge() != null) existing.setAge(dto.getAge());

    if (dto.getClassroom() != null) {
        Classroom classroom = classRep.findById(dto.getClassroom().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));
        existing.setClassroom(classroom);
    }

    DaycareChild patched = daycarekids.save(existing);

    return toDTO(patched);
  }
}
