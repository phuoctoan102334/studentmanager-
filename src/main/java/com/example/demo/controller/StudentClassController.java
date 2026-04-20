package com.example.demo.controller;

import com.example.demo.dto.studentclass.*;
import com.example.demo.service.StudentClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class StudentClassController {

    private final StudentClassService classService;

    @GetMapping
    public ResponseEntity<List<StudentClassListDTO>> getAll() {
        return ResponseEntity.ok(classService.getAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<StudentClassDetailDTO> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(classService.getByCode(code));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<StudentClassDetailDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(classService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentClassListDTO>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UUID departmentId,
            @RequestParam(required = false) UUID majorId) {
        return ResponseEntity.ok(classService.search(keyword, departmentId, majorId));
    }

    @PostMapping
    public ResponseEntity<StudentClassDetailDTO> create(@jakarta.validation.Valid @RequestBody StudentClassSaveDTO dto) {
        return ResponseEntity.ok(classService.create(dto, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentClassDetailDTO> update(@PathVariable UUID id, @jakarta.validation.Valid @RequestBody StudentClassSaveDTO dto) {
        return ResponseEntity.ok(classService.update(id, dto, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        classService.delete(id, null);
        return ResponseEntity.noContent().build();
    }
}