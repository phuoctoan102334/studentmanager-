package com.example.demo.integration;

import com.example.demo.dto.student.StudentSaveDTO;
import com.example.demo.dto.studentclass.StudentClassSaveDTO;
import com.example.demo.entity.StudentClassSection;
import com.example.demo.repository.StudentClassSectionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@SuppressWarnings("null")
public class BusinessFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentClassSectionRepository sectionRepo;

    @Test
    void testFullStudentLifecycleAndHistory() throws Exception {
        // 1. Tạo 2 lớp học
        String class1Id = createClass("C001", "Class 1");
        String class2Id = createClass("C002", "Class 2");

        // 2. Tạo sinh viên thuộc lớp 1
        StudentSaveDTO studentDTO = new StudentSaveDTO();
        studentDTO.setCode("STU001");
        studentDTO.setFullName("Test Student");
        studentDTO.setDateOfBirth(LocalDate.of(2000, 1, 1));
        studentDTO.setGender("1");
        studentDTO.setStudentClasseId(UUID.fromString(class1Id));

        MvcResult createResult = mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isOk())
                .andReturn();
        
        String studentId = (String) objectMapper.readValue(createResult.getResponse().getContentAsString(), Map.class).get("id");

        // Kiểm tra đã có 1 bản ghi section active
        List<StudentClassSection> sections = sectionRepo.findAll();
        assertThat(sections).hasSize(1);
        assertThat(sections.get(0).getStudentClass().getId().toString()).isEqualTo(class1Id);
        assertThat(sections.get(0).getIsActive()).isTrue();

        // 3. Cập nhật sinh viên sang lớp 2 thông qua API update (Kiểm tra lỗi mất lịch sử - Point 1)
        studentDTO.setStudentClasseId(UUID.fromString(class2Id));
        mockMvc.perform(put("/api/students/" + studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isOk());

        // Kiểm tra lịch sử: Phải có 2 bản ghi section (1 cũ đã đóng, 1 mới đang active)
        sections = sectionRepo.findAll();
        assertThat(sections).hasSize(2);
        
        StudentClassSection oldSection = sections.stream()
                .filter(s -> s.getStudentClass().getId().toString().equals(class1Id))
                .findFirst().orElseThrow();
        assertThat(oldSection.getIsActive()).isFalse();
        assertThat(oldSection.getEndDate()).isNotNull();

        StudentClassSection newSection = sections.stream()
                .filter(s -> s.getStudentClass().getId().toString().equals(class2Id))
                .findFirst().orElseThrow();
        assertThat(newSection.getIsActive()).isTrue();

        // 4. Kiểm tra lỗi xóa lớp khi có sinh viên (Point 2)
        mockMvc.perform(delete("/api/classes/" + class2Id))
                .andExpect(status().isBadRequest()); // Phải ném lỗi nghiệp vụ
    }

    private String createClass(String code, String name) throws Exception {
        StudentClassSaveDTO dto = new StudentClassSaveDTO();
        dto.setCode(code);
        dto.setName(name);

        MvcResult result = mockMvc.perform(post("/api/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();
        
        return (String) objectMapper.readValue(result.getResponse().getContentAsString(), Map.class).get("id");
    }
}