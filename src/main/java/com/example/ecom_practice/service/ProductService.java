package com.example.ecom_practice.service;

import com.example.ecom_practice.model.Product;
import com.example.ecom_practice.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.rowset.serial.SerialBlob;

@Service
public class ProductService {
    @Autowired
    private ProductRepo repo;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public List<Product> getAllProducts(){
        return repo.findAll();
    }

    public Product getProductById(int id) {
        return repo.findById(id).orElse(null);
    }

    public Product addProduct(
            String name, String desc, String brand, double price, String category,
            boolean available, int quantity, String releaseDateString, MultipartFile file
    ) throws IOException, ParseException, SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate = sdf.parse(releaseDateString);

        Product product = new Product();
        product.setName(name);
        product.setDesc(desc);
        product.setBrand(brand);
        product.setPrice(price);
        product.setCategory(category);
        product.setAvailable(available);
        product.setQuantity(quantity);
        product.setReleaseDate(releaseDate);

        // Check if the product with the same ID exists
        if (repo.existsById(product.getId())) {
            // Handle the case where the product ID already exists.
            // Example: You can either update the product or throw an exception
            throw new RuntimeException("Product with this ID already exists!");
        } else {
            // If the product does not exist, save the new product
            if (!file.isEmpty()) {
                byte[] imageBytes = file.getBytes();
                Blob photoBlob = new SerialBlob(imageBytes);
                product.setImage(photoBlob);
            }

            return repo.save(product);
        }
    }



//    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
//        product.setImage(imageFile.getBytes());
//        product.setImageName(imageFile.getOriginalFilename());
//        product.setImageType(imageFile.getContentType());
//        return repo.save(product);
//    }

    public void deleteProduct(int id) {
        repo.deleteById(id);
    }

    public List<Product> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }
}
