package com.example.ecom_practice.dto;

import java.util.Base64;

public class ProductResponseDto {

    private int id;
    private String name;
    private String desc;
    private String brand;
    private Double price;
    private String category;
    private String releaseDate; // Optionally, format this to "yyyy-MM-dd"
    private boolean available;
    private int quantity;
    private String image;//get String from database

    public ProductResponseDto(int id, String name, String desc, String brand, Double price, String category, String releaseDate, boolean available, int quantity, byte [] imageBytes) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.brand = brand;
        this.price = price;
        this.category = category;
        this.releaseDate = releaseDate;
        this.available = available;
        this.quantity = quantity;
        //convert image to byte or base64 if exist in database
        //for display purpose
        this.image = imageBytes != null ? Base64.getEncoder().encodeToString(imageBytes) : null;

    }

    public ProductResponseDto(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ProductResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", available=" + available +
                ", quantity=" + quantity +
                ", image='" + image + '\'' +
                '}';
    }
}
