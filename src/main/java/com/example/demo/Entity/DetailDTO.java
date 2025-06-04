package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter

public class DetailDTO {
        private Long id;
        private Long parentId;
        private String name;
        private String idGost;
        private String code;
        private String material;
        private String priority;
        private Double weight;
        private List<DocumentDTO> documents;

    public DetailDTO(Detail detail){
            this.id = detail.getId();
            this.parentId = (detail.getParent() != null) ? detail.getParent().getId() :null;
            this.name = detail.getName();
            this.idGost = detail.getIdGost();
            this.code = detail.getCode();
            this.material = detail.getMaterial();
            this.weight = detail.getWeight();
            this.priority = detail.getPriority();
            this.documents = detail.getDocuments().stream()
                    .map(DocumentMapper::toDTO)
                    .collect(Collectors.toList());
        }
    public DetailDTO(){}

    public DetailDTO(Long id, String name, String idGost,
                     String code, String material, String priority, Double weight) {
        this.id = id;
        this.name = name;
        this.idGost = idGost;
        this.code = code;
        this.material = material;
        this.priority = priority;
        this.weight = weight;
    }

}
