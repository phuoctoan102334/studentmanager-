package com.example.demo.service;

import com.example.demo.dto.advisorsection.*;
import java.util.List;
import java.util.UUID;

public interface AdvisorClassSectionService {
    List<AdvisorSectionListDTO> getByClass(UUID classId);
    void assignAdvisor(AdvisorSectionSaveDTO dto, UUID createdBy);
    void endAssignment(UUID sectionId, UUID updatedBy);
}