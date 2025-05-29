package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DocumentDTO {
    public Long id;
    public String fileName;

    public DocumentDTO(Document doc) {
        this.id = doc.getId();
        this.fileName = doc.getFileName();
    }
    public DocumentDTO(){
    }
}