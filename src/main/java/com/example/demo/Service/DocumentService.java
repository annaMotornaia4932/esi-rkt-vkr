package com.example.demo.Service;

import com.example.demo.Entity.Document;
import com.example.demo.Repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {
    @Autowired
   private DocumentRepository documentRepository;

    public List<Document> getAllDocument (){return documentRepository.findAll();}
}
