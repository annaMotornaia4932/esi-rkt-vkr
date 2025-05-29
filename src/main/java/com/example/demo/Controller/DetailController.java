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
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

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
    public List<Detail> getAll(Model model) {
        detailList = detailService.getAll();
        return detailList;
    }

    @RequestMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        detailService.delete(id);
        return "redirect:/";
    }

    @PostMapping
    public Detail create(@RequestBody Detail detail) {
        return detailService.save(detail);
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

    //загрузить докоткрыть в новой вкладке
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

    // изменить инф о детали
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
        return ResponseEntity.ok(new DetailDTO( detail.getId(), detail.getName(),
                detail.getIdGost(), detail.getCode(), detail.getMaterial(), detail.getPriority(), detail.getWeight()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailDTO> getWeightMaterial (@PathVariable Long id) {
        Optional<Detail> optionalDetail = detailRepository.findById(id);
        if (!optionalDetail.isPresent()) {
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

    @PostMapping("/upload-model")
    public ResponseEntity<String> upload3D(
            @RequestParam("modelfile") MultipartFile modelfile,
            @RequestParam("nodeId") Long nodeId){
        if(modelfile.isEmpty()){
            return ResponseEntity.badRequest().body("Bad file");
        }
        try {
            Path uploadDir = Paths.get("upload/models");
            Files.createDirectories(uploadDir);
            String fileName = nodeId + "_" + modelfile.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);

            Optional<Detail> optionalDetail = detailRepository.findById(nodeId);
            Detail detail = optionalDetail.get();
            if (optionalDetail.isPresent()) {
                Document document = new Document();
                document.setData(modelfile.getBytes());
                document.setFileName(modelfile.getOriginalFilename());
                document.setContentType(modelfile.getContentType());
                document.setParentDetail(detail);
                detail.getDocuments().add(document);
                detailRepository.save(detail);
                List<DocumentDTO> docDTO = detail.getDocuments().stream()
                        .map(DocumentDTO::new)
                        .toList();
            }
            return ResponseEntity.ok("Файл Загружен");
        } catch (IOException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка сервера при загрузке 3д");
        }
    }


}
