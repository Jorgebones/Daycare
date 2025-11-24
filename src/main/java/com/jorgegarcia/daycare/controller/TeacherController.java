package com.jorgegarcia.daycare.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jorgegarcia.daycare.dto.TeacherCreateDTO;
import com.jorgegarcia.daycare.dto.TeacherPatchDTO;
import com.jorgegarcia.daycare.dto.TeacherUpdateDTO;
import com.jorgegarcia.daycare.dto.TeacherDTO;
import com.jorgegarcia.daycare.service.TeacherService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    // 👶 Constructor injection — clean & recommended
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // ============================================================
    // 🍼 GET ALL TEACHERS
    // URL: GET /api/teachers
    // ============================================================
    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    // ============================================================
    // 🍼 GET TEACHER BY ID
    // URL: GET /api/teachers/{id}
    // ============================================================
    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherByTeacherId(id));
    }

    // ============================================================
    // 🍼 CREATE NEW TEACHER (POST)
    // Uses TeacherCreateDTO
    // URL: POST /api/teachers
    // ============================================================
    @PostMapping
    public ResponseEntity<TeacherDTO> createTeacher(
            @Valid @RequestBody TeacherCreateDTO dto) {

        TeacherDTO created = teacherService.addTeacher(dto);

        // 201 Created is the correct success response
        return ResponseEntity.status(201).body(created);
    }

    // ============================================================
    // 🍼 UPDATE TEACHER (PUT)
    // Full replacement (all fields required)
    // Uses TeacherUpdateDTO
    // URL: PUT /api/teachers/{id}
    // ============================================================
    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(
            @PathVariable Long id,
            @Valid @RequestBody TeacherUpdateDTO dto) {

        TeacherDTO updated = teacherService.updateTeacherById(id, dto);
        return ResponseEntity.ok(updated);
    }

    // ============================================================
    // 🍼 PATCH TEACHER (PATCH)
    // Partial update (only provided fields)
    // Uses TeacherPatchDTO
    // URL: PATCH /api/teachers/{id}
    // ============================================================
    @PatchMapping("/{id}")
    public ResponseEntity<TeacherDTO> patchTeacher(
            @PathVariable Long id,
            @Valid @RequestBody TeacherPatchDTO dto) {

        TeacherDTO patched = teacherService.patchTeacherById(id, dto);
        return ResponseEntity.ok(patched);
    }

    // ============================================================
    // 🍼 DELETE TEACHER
    // URL: DELETE /api/teachers/{id}
    //
    // 204 = No Content (no body returned)
    // ============================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {

        teacherService.deleteTeacher(id);

        return ResponseEntity.noContent().build();
    }
}
