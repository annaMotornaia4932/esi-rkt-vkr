package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DetailDTO {
    private Long id;
    private String name;
    private String idGost;
    private String code;
    private String material;
    private String priority;
    private Double weight;

    public DetailDTO(Detail detail){
        this.id = detail.getId();
        this.name = detail.getName();
        this.idGost = detail.getIdGost();
        this.code = detail.getCode();
        this.material = detail.getMaterial();
        this.weight = detail.getWeight();
        this.priority = detail.getPriority();
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
