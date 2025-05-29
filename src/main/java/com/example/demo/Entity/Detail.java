package com.example.demo.Entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity

//Деталь
public class Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String idGost;
    private String code;
    private String material;
    private String priority;
    private Double weight;

    @OneToMany(mappedBy = "parentDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private  List<Document> documents = new ArrayList<>();
}
