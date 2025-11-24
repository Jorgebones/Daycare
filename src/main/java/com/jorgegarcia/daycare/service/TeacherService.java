package com.jorgegarcia.daycare.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jorgegarcia.daycare.dto.ClassroomSummaryDTO;
import com.jorgegarcia.daycare.dto.TeacherCreateDTO;
import com.jorgegarcia.daycare.dto.TeacherDTO;
import com.jorgegarcia.daycare.dto.TeacherPatchDTO;
import com.jorgegarcia.daycare.dto.TeacherUpdateDTO;
import com.jorgegarcia.daycare.exception.DuplicateEntryException;
import com.jorgegarcia.daycare.exception.ResourceNotFoundException;
import com.jorgegarcia.daycare.model.Classroom;
import com.jorgegarcia.daycare.model.Teacher;
import com.jorgegarcia.daycare.repository.TeacherRepository;

@Service
public class TeacherService {
  private final TeacherRepository teachRep;

  public TeacherService(TeacherRepository repo){
    this.teachRep = repo;
  }
  // 👶 Begginer version: extra clear, no streams
public TeacherDTO toDTO_Baby(Teacher teacher) {

    // 1️⃣ Create an empty List where we will store the ClassroomSummaryDTO objects
    List<ClassroomSummaryDTO> classroomDtos = new ArrayList<>();

    // 2️⃣ Check if teacher has any classrooms (avoid null pointer errors)
    if (teacher.getClasses() != null) {

        // 3️⃣ Loop through each Classroom entity the teacher teaches
        for (var classroom : teacher.getClasses()) {

            // 4️⃣ Convert one Classroom → one ClassroomSummaryDTO
            ClassroomSummaryDTO summary = ClassroomSummaryDTO.builder()
                .id(classroom.getId())              // copy the ID
                .className(classroom.getClassName()) // copy the class name
                .build();

            // 5️⃣ Add that DTO to our list
            classroomDtos.add(summary);
        }
    }

    // 6️⃣ Build the final TeacherDTO including the list we just filled
    return TeacherDTO.builder()
        .id(teacher.getId())
        .firstName(teacher.getFirstName())
        .lastName(teacher.getLastName())
        .classrooms(classroomDtos)  // the list we built above
        .build();
}
// 👶 Beginner version: easiest to understand
public Teacher toEntity_Baby(TeacherCreateDTO dto) {

    // 1️⃣ Make an empty list for classrooms (new teachers don't have classes yet)
    List<Classroom> emptyClassrooms = new ArrayList<>();

    // 2️⃣ Build the Teacher entity
    Teacher teacher = Teacher.builder()
        .firstName(dto.getFirstName())   // copy first name
        .lastName(dto.getLastName())     // copy last name
        .classes(emptyClassrooms)        // no classrooms yet
        .build();

    // 3️⃣ Return the new entity
    return teacher;
}

// 👑 Advanced version using streams (compact + pro style)
public TeacherDTO toDTO(Teacher teacher) {

    // Convert teacher.getClasses() to List<ClassroomSummaryDTO> in one line:
    List<ClassroomSummaryDTO> classroomDtos =
        teacher.getClasses() == null
            ? List.of()    // if null, return empty list
            : teacher.getClasses().stream()
                .map(c -> ClassroomSummaryDTO.builder()
                        .id(c.getId())
                        .className(c.getClassName())
                        .build()
                )
                .toList(); // convert the stream into a normal list

    return TeacherDTO.builder()
        .id(teacher.getId())
        .firstName(teacher.getFirstName())
        .lastName(teacher.getLastName())
        .email(teacher.getEmail())
        .classrooms(classroomDtos)  // mapped list
        .build();
}
// 👑 Advanced version: compact + clean
public Teacher toEntity(TeacherCreateDTO dto) {
    return Teacher.builder()
        .firstName(dto.getFirstName())
        .lastName(dto.getLastName())
        .email(dto.getEmail())
        .classes(List.of())     // new teacher starts with NO classrooms
        .build();
}
  public List<TeacherDTO> getAllTeachers(){
    return teachRep.findAll()
    .stream()
    .map(this::toDTO)
    .toList();
  }
  public TeacherDTO getTeacherByTeacherId(long teacherId){
    Teacher found = teachRep.findById(teacherId)
    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
    return toDTO(found);
  }

  public TeacherDTO addTeacher(TeacherCreateDTO dto){
    /* First version
    var exists = teachRep.findByEmail(dto.getEmail());
    if(exists.isPresent()){
        throw new DuplicateEntryException("Email already exists");
    }
    Teacher tbCreateed = toEntity(dto);
    Teacher created = teachRep.save(tbCreateed);
    return toDTO(created);*/
    teachRep.findByEmail(dto.getEmail())
        .ifPresent(t -> {
            throw new DuplicateEntryException("Email already exists");
        });

    Teacher saved = teachRep.save(toEntity(dto));
    return toDTO(saved);
  }

  public TeacherDTO updateTeacherById(Long id,TeacherUpdateDTO dto){
    Teacher tobeUpdated = teachRep.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Teacher doesn't exist"));

    tobeUpdated.setFirstName(dto.getFirstName());
    tobeUpdated.setLastName(dto.getLastName());

    Teacher updated = teachRep.save(tobeUpdated);
    return toDTO(updated);
  }

  public TeacherDTO patchTeacherById(Long id, TeacherPatchDTO dto){
    Teacher toBePatched = teachRep.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Teacher doesn't exist"));

    if(dto.getFirstName() != null) toBePatched.setFirstName(dto.getFirstName());
    if(dto.getLastName() != null) toBePatched.setLastName(dto.getLastName());
    if(dto.getEmail() != null) toBePatched.setEmail(dto.getEmail());

    Teacher patched = teachRep.save(toBePatched);
    return toDTO(patched);
  }
  public void deleteTeacher(Long teacherId){
    Teacher exists = teachRep.findById(teacherId)
    .orElseThrow(() -> new ResourceNotFoundException("Teacher doesn't exist"));
    teachRep.delete(exists);
  }
}
