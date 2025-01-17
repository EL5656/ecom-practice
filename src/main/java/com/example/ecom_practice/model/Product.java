package com.example.ecom_practice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String desc;
    private String brand;

    private Double price; // Change to BigDecimal if you need more precision
    private String category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date releaseDate;

    private boolean available;
    private int quantity;

    @JsonIgnore
    @Lob
    private Blob image;

    public Product(String name, String desc, String brand, Double price, String category, Date releaseDate, boolean available, int quantity) {
        this.name = name;
        this.desc = desc;
        this.brand = brand;
        this.price = price;
        this.category = category;
        this.releaseDate = releaseDate;
        this.available = available;
        this.quantity = quantity;
    }
}
