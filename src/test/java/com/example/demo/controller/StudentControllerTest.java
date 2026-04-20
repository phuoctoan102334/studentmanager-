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

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import(GlobalExceptionHandler.class)
@SuppressWarnings("null")
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentSaveDTO createValidDTO() {
        StudentSaveDTO dto = new StudentSaveDTO();
        dto.setCode("S001");
        dto.setFullName("John Doe");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));
        dto.setGender("1");
        return dto;
    }

    @Test
    void create_ShouldReturnStudent_WhenValid() throws Exception {
        StudentSaveDTO dto = createValidDTO();

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
    void create_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
        StudentSaveDTO dto = new StudentSaveDTO();
        dto.setCode(""); // Blank code
        dto.setFullName(""); // Blank name
        // dateOfBirth is null

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("Mã sinh viên không được để trống"))
                .andExpect(jsonPath("$.fullName").value("Họ tên không được để trống"))
                .andExpect(jsonPath("$.dateOfBirth").value("Ngày sinh không được để trống"));
    }

    @Test
    void create_ShouldReturnError_WhenServiceThrows() throws Exception {
        StudentSaveDTO dto = createValidDTO();

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
        StudentSaveDTO dto = createValidDTO();
        
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