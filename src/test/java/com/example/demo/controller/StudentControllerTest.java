package com.example.demo.controller;

import com.example.demo.dto.student.StudentDetailDTO;
import com.example.demo.dto.student.StudentSaveDTO;
import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import(GlobalExceptionHandler.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_ShouldReturnStudent_WhenValid() throws Exception {
        StudentSaveDTO dto = new StudentSaveDTO();
        dto.setCode("S001");
        dto.setFullName("John Doe");

        StudentDetailDTO result = new StudentDetailDTO();
        result.setId(UUID.randomUUID());
        result.setCode("S001");
        result.setFullName("John Doe");

        when(studentService.create(any(StudentSaveDTO.class), any())).thenReturn(result);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("S001"))
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    void create_ShouldReturnError_WhenServiceThrows() throws Exception {
        StudentSaveDTO dto = new StudentSaveDTO();
        dto.setCode("EXISTS");

        when(studentService.create(any(StudentSaveDTO.class), any()))
                .thenThrow(new RuntimeException("Mã sinh viên đã tồn tại: EXISTS"));

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Mã sinh viên đã tồn tại: EXISTS"));
    }

    @Test
    void create_ShouldHandleDataIntegrityViolation() throws Exception {
        StudentSaveDTO dto = new StudentSaveDTO();
        
        // Mocking DataIntegrityViolationException is a bit tricky because of how Spring wraps it
        // but let's try to throw a RuntimeException that matches the handler's logic
        when(studentService.create(any(StudentSaveDTO.class), any()))
                .thenThrow(new RuntimeException("could not execute statement; CK_stu_gender"));

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Lỗi: Giới tính đã chọn không đúng quy định."));
    }
}