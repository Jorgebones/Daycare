package com.jorgegarcia.daycare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jorgegarcia.daycare.dto.ClassroomCreateDTO;
import com.jorgegarcia.daycare.dto.ClassroomDTO;
import com.jorgegarcia.daycare.dto.ClassroomPatchDTO;
import com.jorgegarcia.daycare.dto.ClassroomUpdateDTO;
import com.jorgegarcia.daycare.dto.DaycareChildSummaryDTO;
import com.jorgegarcia.daycare.dto.TeacherSummaryDTO;
import com.jorgegarcia.daycare.exception.DuplicateEntryException;
import com.jorgegarcia.daycare.exception.ResourceNotFoundException;
import com.jorgegarcia.daycare.model.Classroom;
import com.jorgegarcia.daycare.model.DaycareChild;
import com.jorgegarcia.daycare.model.Teacher;

import com.jorgegarcia.daycare.repository.ClassroomRepository;
import com.jorgegarcia.daycare.repository.DaycareChildRepository;
import com.jorgegarcia.daycare.repository.TeacherRepository;
@Service
public class ClassroomService {

    private final ClassroomRepository classRep;
    private final DaycareChildRepository daycareRep;
    private final TeacherRepository teacherRep;

    public ClassroomService(
            ClassroomRepository classRep,
            DaycareChildRepository daycareRep,
            TeacherRepository teacherRep
    ) {
        this.classRep = classRep;
        this.daycareRep = daycareRep;
        this.teacherRep = teacherRep;
    }

    // ============================================================
    // 🍼 toDto()
    // Converts Classroom ENTITY → ClassroomDTO (the response object)
    // ============================================================
    private ClassroomDTO toDto(Classroom classroom) {

        // 1️⃣ Convert Teacher → TeacherSummaryDTO
        TeacherSummaryDTO teacherDto = TeacherSummaryDTO.builder()
                .id(classroom.getTeacher().getId())
                .fullName(
                        classroom.getTeacher().getFirstName() + " " +
                        classroom.getTeacher().getLastName()
                )
                .build();

        // 2️⃣ Convert children list → ChildSummaryDTO list
        // (Avoid circular references!)
        List<DaycareChildSummaryDTO> childDtos = classroom.getChildren()
                .stream()
                .map(child -> DaycareChildSummaryDTO.builder()
                        .id(child.getId())
                        .fullName(child.getFirstName() + " " + child.getLastName())
                        .build()
                )
                .toList();

        // 3️⃣ Build final DTO
        return ClassroomDTO.builder()
                .id(classroom.getId())
                .className(classroom.getClassName())
                .teacher(teacherDto)
                .children(childDtos)
                .build();
    }

    // ============================================================
    // 🍼 GET ALL CLASSROOMS
    // ============================================================
    public List<ClassroomDTO> getAllClassrooms() {
        return classRep.findAll()
                .stream()
                .map(this::toDto) // Convert each entity to DTO
                .toList();
    }

    // ============================================================
    // 🍼 GET CLASSROOM BY ID
    // Throws error if not found
    // ============================================================
    public ClassroomDTO getClassromByClassroomId(long classroomId) {
        Classroom found = classRep.findById(classroomId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Classroom %d not found".formatted(classroomId))
                );

        return toDto(found);
    }

    // ============================================================
    // 🍼 GET CLASSROOMS BY CHILD ID
    // (Finds all classrooms where the child belongs)
    // ============================================================
    public List<ClassroomDTO> getClassroomByDaycareChildId(long daycareChildId) {

        List<Classroom> classes = classRep.findByChildren_Id(daycareChildId);

        // If no classes found → throw error
        if (classes.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No classrooms found for child %d".formatted(daycareChildId)
            );
        }

        return classes.stream()
                .map(this::toDto)
                .toList();
    }

    // ============================================================
    // 🍼 ADD NEW CLASSROOM (POST)
    // Requires a teacher ID (because classroom belongs to a teacher)
    // ============================================================
    public ClassroomDTO addClassroom(ClassroomCreateDTO dto) {

        // 1️⃣ Find teacher or error
        Teacher teacher = teacherRep.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        // 2️⃣ Build entity
        Classroom classroom = Classroom.builder()
                .className(dto.getClassName())
                .teacher(teacher)
                .children(List.of()) // brand-new class → empty children list
                .build();

        // 3️⃣ Save in DB
        Classroom saved = classRep.save(classroom);

        // 4️⃣ Convert to DTO
        return toDto(saved);
    }

    // ============================================================
    // 🍼 UPDATE CLASSROOM (PUT)
    // Requires ALL values (full replacement)
    // ============================================================
    public ClassroomDTO updateClassroom(Long id, ClassroomUpdateDTO dto) {

        // 1️⃣ Find the classroom
        Classroom classroom = classRep.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        // 2️⃣ Find the teacher (new teacher or updated teacher)
        Teacher teacher = teacherRep.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        // 3️⃣ Replace all fields
        classroom.setClassName(dto.getClassName());
        classroom.setTeacher(teacher);

        // 4️⃣ Save updated entity
        Classroom updated = classRep.save(classroom);

        // 5️⃣ Return new DTO
        return toDto(updated);
    }

    // ============================================================
    // 🍼 PATCH CLASSROOM (PATCH)
    // Only updates fields provided (partial update)
    // ============================================================
    public ClassroomDTO patchClassroom(Long id, ClassroomPatchDTO dto) {

        // 1️⃣ Find classroom
        Classroom classroom = classRep.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

        // 2️⃣ Update ONLY what was provided
        if (dto.getClassName() != null) {
            classroom.setClassName(dto.getClassName());
        }

        if (dto.getTeacherId() != null) {
            Teacher teacher = teacherRep.findById(dto.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
            classroom.setTeacher(teacher);
        }

        // 3️⃣ Save partial update
        Classroom patched = classRep.save(classroom);

        // 4️⃣ Convert to DTO
        return toDto(patched);
    }
    
    public void deleteClassroom(Long id) {

    // 1️⃣ Find the classroom or fail
    Classroom classroom = classRep.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Classroom not found"));

    // 2️⃣ Safety check: A classroom with children CANNOT be deleted
    if (classroom.getChildren() != null && !classroom.getChildren().isEmpty()) {
        throw new IllegalStateException(
            "Cannot delete classroom — children are still assigned to it."
        );
    }

    // 3️⃣ Optional: check if the teacher is allowed to have zero classrooms
    // (depends on business rules — usually allowed)
    // If not allowed, add a rule here.

    // 4️⃣ Delete safely
    classRep.delete(classroom);
}

}
