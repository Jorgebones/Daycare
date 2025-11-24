package com.jorgegarcia.daycare.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jorgegarcia.daycare.dto.DaycareChildDTO;
import com.jorgegarcia.daycare.service.DaycareChildService;

@RestController
@RequestMapping("/api/daycareKids")
public class DaycareChildController {

    private final DaycareChildService daycareService;

    public DaycareChildController(DaycareChildService service) {
        this.daycareService = service;
    }

    // ------------------ GET ALL ------------------
    @GetMapping
    public ResponseEntity<List<DaycareChildDTO>> getAllChildren() {
        return ResponseEntity.ok(daycareService.getAllChildren());
    }

    // ------------------ GET BY ID ------------------
    @GetMapping("/{id}")
    public ResponseEntity<DaycareChildDTO> getChildById(@PathVariable Long id) {
        return ResponseEntity.ok(daycareService.getChildById(id));
    }

    // ------------------ GET BY CLASSROOM ------------------
    @GetMapping("/classroom/{classId}")
    public ResponseEntity<List<DaycareChildDTO>> getChildrenByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(daycareService.getChildrenByClassroomId(classId));
    }

    // ------------------ CREATE CHILD ------------------
    @PostMapping
    public ResponseEntity<DaycareChildDTO> createChild(@RequestBody DaycareChildDTO dto) {
        DaycareChildDTO created = daycareService.addDaycareChildren(dto);
        return ResponseEntity.status(201).body(created);
    }

    // ------------------ UPDATE CHILD (PUT) ------------------
    @PutMapping("/{id}")
    public ResponseEntity<DaycareChildDTO> updateChild(
            @PathVariable Long id,
            @RequestBody DaycareChildDTO dto) {
              
        return ResponseEntity.ok(daycareService.updateChild(id, dto));
    }

    // ------------------ PARTIAL UPDATE (PATCH) ------------------
    @PatchMapping("/{id}")
    public ResponseEntity<DaycareChildDTO> patchChild(
            @PathVariable Long id,
            @RequestBody DaycareChildDTO dto) {

        return ResponseEntity.ok(daycareService.patchChild(id, dto));
    }

    // ------------------ DELETE CHILD ------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChild(@PathVariable Long id) {
        daycareService.deleteDaycareChild(id);
        return ResponseEntity.noContent().build();
    }
}
