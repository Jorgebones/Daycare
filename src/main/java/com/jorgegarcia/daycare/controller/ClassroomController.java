package com.jorgegarcia.daycare.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jorgegarcia.daycare.dto.ClassroomCreateDTO;
import com.jorgegarcia.daycare.dto.ClassroomDTO;
import com.jorgegarcia.daycare.dto.ClassroomPatchDTO;
import com.jorgegarcia.daycare.dto.ClassroomUpdateDTO;
import com.jorgegarcia.daycare.service.ClassroomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {

    private final ClassroomService classService;

    // 👶 Constructor injection: clean, safe, best-practice
    public ClassroomController(ClassroomService service) {
        this.classService = service;
    }

    // ============================================================
    // 🍼 GET ALL CLASSROOMS
    // Called when frontend wants to show every classroom we have.
    //
    // URL: GET /api/classrooms
    // ============================================================
    @GetMapping
    public ResponseEntity<List<ClassroomDTO>> getAllClassrooms() {
        // 🧸 Just return the list that the service already makes for us
        return ResponseEntity.ok(classService.getAllClassrooms());
    }

    // ============================================================
    // 🍼 GET CLASSROOM BY ID
    // Used when frontend wants details of one specific classroom.
    //
    // URL: GET /api/classrooms/{id}
    // ============================================================
    @GetMapping("/{id}")
    public ResponseEntity<ClassroomDTO> getClassroomById(@PathVariable Long id) {
        // 🧸 Service handles the “not found” error
        return ResponseEntity.ok(classService.getClassromByClassroomId(id));
    }

    // ============================================================
    // 🍼 GET CLASSROOMS BY CHILD ID
    // Finds all classrooms where a certain child belongs.
    //
    // URL: GET /api/classrooms/children/{childId}
    // ============================================================
    @GetMapping("/children/{daycareChildId}")
    public ResponseEntity<List<ClassroomDTO>> getClassroomsByDaycareChildId(
            @PathVariable Long daycareChildId) {

        return ResponseEntity.ok(
                classService.getClassroomByDaycareChildId(daycareChildId)
        );
    }

    // ============================================================
    // 🍼 CREATE CLASSROOM (POST)
    // Used when a new classroom is registered.
    // Must include a "teacherId" to assign the teacher.
    //
    // URL: POST /api/classrooms
    //
    // Note: @Valid makes Spring validate your DTO annotations
    // ============================================================
    @PostMapping
    public ResponseEntity<ClassroomDTO> createClassroom(
            @Valid @RequestBody ClassroomCreateDTO dto) {

        ClassroomDTO created = classService.addClassroom(dto);

        // 201 Created — the official HTTP status for new resources
        return ResponseEntity.status(201).body(created);
    }

    // ============================================================
    // 🍼 UPDATE CLASSROOM (PUT)
    // Full replacement → requires ALL fields
    //
    // URL: PUT /api/classrooms/{id}
    // ============================================================
    @PutMapping("/{id}")
    public ResponseEntity<ClassroomDTO> updateClassroom(
            @PathVariable Long id,
            @Valid @RequestBody ClassroomUpdateDTO dto) {

        ClassroomDTO updated = classService.updateClassroom(id, dto);
        return ResponseEntity.ok(updated);
    }

    // ============================================================
    // 🍼 PATCH CLASSROOM (PATCH)
    // Partial update → only modifies fields you send
    //
    // URL: PATCH /api/classrooms/{id}
    // ============================================================
    @PatchMapping("/{id}")
    public ResponseEntity<ClassroomDTO> patchClassroom(
            @PathVariable Long id,
            @Valid @RequestBody ClassroomPatchDTO dto) {

        ClassroomDTO patched = classService.patchClassroom(id, dto);
        return ResponseEntity.ok(patched);
    }

    // ============================================================
    // 🍼 DELETE CLASSROOM
    // Default pattern: return 204 (no content)
    //
    // URL: DELETE /api/classrooms/{id}
    // ============================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {

      // 🍼 Service handles all the heavy lifting
      classService.deleteClassroom(id);

      // 204 = No Content (correct HTTP code for delete)
      return ResponseEntity.noContent().build();
    }
}
