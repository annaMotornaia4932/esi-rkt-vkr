package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

public class Document {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String contentType;

    @Lob
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "detail_id")
    private Detail parentDetail;
}
