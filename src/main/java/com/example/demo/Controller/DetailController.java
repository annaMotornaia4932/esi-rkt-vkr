package com.example.demo.Controller;

import com.example.demo.Entity.*;
import com.example.demo.Repository.DetailRepository;
import com.example.demo.Repository.DocumentRepository;
import com.example.demo.Service.DetailService;
import jakarta.transaction.Transactional;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestController
@RequestMapping("/details")
public class DetailController {
    @Autowired
    private DetailService detailService;

    @Autowired
    private DetailRepository detailRepository;

    @Autowired
            private DocumentRepository documentRepository;

    @Autowired
            private DetailMapper detailMapper;

    List<Detail> detailList;

    @GetMapping
    public List<DetailDTO> getAll() {
        return detailRepository.findAll()
                .stream()
                .map(detailMapper::toDTO)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNode (@PathVariable Long id) {
        Optional<Detail> optionalDetail = detailRepository.findById(id);
        if (optionalDetail.isPresent()) {
            detailRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<DetailDTO> create(@RequestBody DetailDTO dto) {
        Detail detail = detailMapper.toDetail(dto, detailRepository);
        Detail saved = detailRepository.save(detail);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(detailMapper.toDTO(saved));
    }

    //загрузить документ в базу сохранить
    @PostMapping("/{id}/upload")
    public ResponseEntity<List<DocumentDTO>> uploadFile(@PathVariable Long id, @RequestParam("files") MultipartFile[] files){
        Optional <Detail> optionalDetail = detailRepository.findById(id);
        if (!optionalDetail.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Detail detail = optionalDetail.get();
        for (MultipartFile file: files){
        try {
            Document document = new Document();
            document.setData(file.getBytes());
            document.setFileName(file.getOriginalFilename());
            document.setContentType(file.getContentType());
            document.setParentDetail(detail);
            detail.getDocuments().add(document);
            detailRepository.save(detail);
        } catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();}
    }
        List<DocumentDTO> docDTO = detail.getDocuments().stream()
                .map(DocumentDTO::new)
                .toList();
        return ResponseEntity.ok(docDTO);
    }

    //отобразить список документов
    @GetMapping("/{id}/documents")
    public ResponseEntity<List<DocumentDTO>> getDocs (@PathVariable Long id) {
        Optional<Detail> optionalDetail = detailRepository.findById(id);
        if (!optionalDetail.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<DocumentDTO> documents = optionalDetail.get()
                        .getDocuments()
                                .stream()
                                        .map(DocumentDTO::new)
                                                .toList();
        return ResponseEntity.ok(documents);
    }

    //загрузить док открыть в новой вкладке
    @GetMapping("/documents/{id}")
    public ResponseEntity<byte[]> gownloadDoc (@PathVariable Long id) {
        Optional<Document> optionalDocument = documentRepository.findById(id);
        if (!optionalDocument.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Document document = optionalDocument.get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+ document.getFileName() + "\"")
                .body(document.getData());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetailDTO> updateDetail(@PathVariable Long id, @RequestBody DetailDTO detailDto){
        Optional<Detail> optionalDetail = detailRepository.findById(id);
        if (!optionalDetail.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Detail detail = optionalDetail.get();
        if(detailDto.getName() != null) detail.setName(detailDto.getName());
        if(detailDto.getIdGost() != null) detail.setIdGost(detailDto.getIdGost());
        if(detailDto.getCode() != null) detail.setCode(detailDto.getCode());
        if(detailDto.getMaterial() != null) detail.setMaterial(detailDto.getMaterial());
        if(detailDto.getWeight() != null) detail.setWeight(detailDto.getWeight());
        if(detailDto.getPriority() != null) detail.setPriority(detailDto.getPriority());

        detailRepository.save(detail);
        return ResponseEntity.ok(new DetailDTO(detail));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailDTO> getWeightMaterial (@PathVariable Long id) {
        Optional<Detail> optionalDetail = detailRepository.findById(id);
        if (optionalDetail.isPresent()) {
            DetailDTO detailDto = detailMapper.toDTO(optionalDetail.get());
            return ResponseEntity.ok(detailDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<DetailDTO>> overSearch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String idGost,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Double minWeight,
            @RequestParam(required = false) Double maxWeight){
        if (name != null && name.isBlank()) name = null;
        if (idGost != null && idGost.isBlank()) idGost = null;
        if (code != null && code.isBlank()) code = null;
        if (material != null && material.isBlank()) material = null;
        if (priority != null && priority.isBlank()) priority = null;
        List<Detail> results = detailRepository.findByParam(name, idGost,
                code, material, priority, minWeight, maxWeight);
List<DetailDTO> dtos = results.stream().map(DetailDTO::new).toList();
System.out.println(dtos);
return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        Optional<Document> optionalDoc = documentRepository.findById(id);
        if (optionalDoc.isPresent()) {
            documentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
