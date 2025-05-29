package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter

@Component
public class DetailMapper {

    public DetailDTO toDTO (Detail detail) {
        DetailDTO detailDTO = new DetailDTO();
        detailDTO.setId(detail.getId());
        detailDTO.setName(detail.getName());
        detailDTO.setMaterial(detail.getMaterial());
        detailDTO.setWeight(detail.getWeight());
        detailDTO.setPriority(detail.getPriority());
        return detailDTO;
    }

    public Detail toDetail (DetailDTO detailDto) {
        Detail detail = new Detail();
        detail.setId(detailDto.getId());
        detail.setName(detailDto.getName());
        detail.setMaterial(detailDto.getMaterial());
        detail.setWeight(detailDto.getWeight());
        detail.setPriority(detailDto.getPriority());
        return detail;
    }
}
