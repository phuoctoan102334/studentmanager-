package com.example.demo.controller;

import com.example.demo.dto.student.*;
import com.example.demo.dto.studentsection.StudentTransferDTO;
import com.example.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<Page<StudentListDTO>> getAll(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(studentService.getAllPaged(pageable));
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
    public ResponseEntity<Page<StudentListDTO>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UUID departmentId,
            @RequestParam(required = false) UUID majorId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID classId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(studentService.searchPaged(keyword, departmentId, majorId, status, classId, pageable));
    }

    @PostMapping
    public ResponseEntity<StudentDetailDTO> create(@jakarta.validation.Valid @RequestBody StudentSaveDTO dto) {
        // Mặc định createdBy là null hoặc lấy từ session/auth nếu có
        return ResponseEntity.ok(studentService.create(dto, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDetailDTO> update(@PathVariable UUID id, @jakarta.validation.Valid @RequestBody StudentSaveDTO dto) {
        return ResponseEntity.ok(studentService.update(id, dto, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        studentService.delete(id, null);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@jakarta.validation.Valid @RequestBody StudentTransferDTO dto) {
        studentService.transfer(dto, null);
        return ResponseEntity.ok().build();
    }
}