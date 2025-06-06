package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

public class Document {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id; //автоинкремент id
    private String fileName; //наименование
    private String contentType; //тип документа

    @Lob
    private byte[] data; //битовые данные Large Object

    @ManyToOne
    @JoinColumn(name = "detail_id")
    private Detail parentDetail;
}
