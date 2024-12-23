package com.example.demo.owner.store;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "categories")
public class Categories {

  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "categories_id")
    private Long categoryId;

    
    @Column(name = "name", nullable = false, length = 40)
    private String name;
}
