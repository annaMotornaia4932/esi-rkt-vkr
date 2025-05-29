package com.example.demo.Repository;

import com.example.demo.Entity.Detail;
import com.example.demo.Entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {
}
