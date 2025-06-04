package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import com.example.demo.Repository.DetailRepository;
import com.example.demo.Entity.DocumentMapper;
import com.example.demo.Entity.DocumentDTO;
import com.example.demo.Entity.DetailMapper;

import java.util.stream.Collectors;
import java.util.List;

@Getter
@Setter

@Component
public class DetailMapper {

    public DetailDTO toDTO (Detail detail) {
        DetailDTO detailDTO = new DetailDTO();
        detailDTO.setId(detail.getId());
        if(detail.getParent()!=null){detailDTO.setParentId(detail.getParent().getId());}
        else {detailDTO.setParentId(null);}
        detailDTO.setName(detail.getName());
        detailDTO.setMaterial(detail.getMaterial());
        detailDTO.setWeight(detail.getWeight());
        detailDTO.setPriority(detail.getPriority());
        detailDTO.setIdGost(detail.getIdGost());
        detailDTO.setCode(detail.getCode());
        List<DocumentDTO> docs = detail.getDocuments().stream()
                .map(DocumentMapper::toDTO)
                .collect(Collectors.toList());
        detailDTO.setDocuments(docs);
        return detailDTO;
    }


    public Detail toDetail (DetailDTO detailDto, DetailRepository detailRepository) {
        Detail detail = new Detail();
        detail.setName(detailDto.getName());
        if(detailDto.getParentId()!=null){

            Detail parent = detailRepository.findById(detailDto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
            detail.setParent(parent);
        }
        detail.setMaterial(detailDto.getMaterial());
        detail.setWeight(detailDto.getWeight());
        detail.setPriority(detailDto.getPriority());
        detail.setIdGost(detailDto.getIdGost());
        detail.setCode(detail.getCode());
        return detail;
    }


    public void updateDetailFromDTO(Detail detail, DetailDTO detailDTO, DetailRepository detailRepository){
        if (detailDTO.getParentId()!=null){
            Detail parent = detailRepository.findById(detailDTO.getParentId())
                    .orElseThrow(()->new RuntimeException("Parent in updDetFrDTO not found"));
            detail.setParent(parent);
        }
        detail.setName(detailDTO.getName());
        detail.setMaterial(detailDTO.getMaterial());
        detail.setWeight(detailDTO.getWeight());
        detail.setPriority(detailDTO.getPriority());
        detail.setIdGost(detailDTO.getIdGost());
        detail.setCode(detail.getCode());
    }
}
