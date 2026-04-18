package com.example.demo.controller;

import com.example.demo.dto.student.*;
import com.example.demo.dto.studentsection.StudentTransferDTO;
import com.example.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentListDTO>> getAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<StudentDetailDTO> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(studentService.getByCode(code));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<StudentDetailDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentListDTO>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UUID departmentId,
            @RequestParam(required = false) UUID majorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID classId) {
        return ResponseEntity.ok(studentService.search(keyword, departmentId, majorId, status, classId));
    }

    @PostMapping
    public ResponseEntity<StudentDetailDTO> create(@RequestBody StudentSaveDTO dto) {
        // Mặc định createdBy là null hoặc lấy từ session/auth nếu có
        return ResponseEntity.ok(studentService.create(dto, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDetailDTO> update(@PathVariable UUID id, @RequestBody StudentSaveDTO dto) {
        return ResponseEntity.ok(studentService.update(id, dto, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        studentService.delete(id, null);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody StudentTransferDTO dto) {
        studentService.transfer(dto, null);
        return ResponseEntity.ok().build();
    }
}