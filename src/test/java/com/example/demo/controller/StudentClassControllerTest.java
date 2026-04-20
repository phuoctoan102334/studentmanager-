package com.example.demo.controller;

import com.example.demo.dto.studentclass.StudentClassDetailDTO;
import com.example.demo.dto.studentclass.StudentClassSaveDTO;
import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.service.StudentClassService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentClassController.class)
@Import(GlobalExceptionHandler.class)
@SuppressWarnings("null")
public class StudentClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentClassService classService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
        StudentClassSaveDTO dto = new StudentClassSaveDTO();
        dto.setCode(""); // Blank
        dto.setName(""); // Blank

        mockMvc.perform(post("/api/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.code").value("Mã lớp không được để trống"))
                .andExpect(jsonPath("$.name").value("Tên lớp không được để trống"));
    }

    @Test
    void create_ShouldReturnClass_WhenValid() throws Exception {
        StudentClassSaveDTO dto = new StudentClassSaveDTO();
        dto.setCode("C001");
        dto.setName("Class 001");

        StudentClassDetailDTO result = new StudentClassDetailDTO();
        result.setId(UUID.randomUUID());
        result.setCode("C001");
        result.setName("Class 001");

        when(classService.create(any(StudentClassSaveDTO.class), any())).thenReturn(result);

        mockMvc.perform(post("/api/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("C001"))
                .andExpect(jsonPath("$.name").value("Class 001"));
    }
}