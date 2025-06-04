package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DocumentMapper {
    public static DocumentDTO toDTO(Document doc){
        DocumentDTO dto = new DocumentDTO();
        dto.setId(doc.getId());
        dto.setFileName(doc.getFileName());
        return dto;
    }
}
