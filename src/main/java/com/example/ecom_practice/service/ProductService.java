package com.example.ecom_practice.service;

import com.example.ecom_practice.model.Product;
import com.example.ecom_practice.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

@Service
public class ProductService {
    @Autowired
    private ProductRepo repo;


    public List<Product> getAllProducts(){
        return repo.findAll();
    }

    public Product getProductById(int id) {
        return repo.findById(id).orElse(null);
    }

    public byte[] getImageByProductId(int productId) throws SQLException {
        Product product = repo.findById(productId).orElseThrow(() ->
                new RuntimeException("Product not found with ID: " + productId)
        );

        if (product.getImage() != null) {
            Blob imageBlob = product.getImage();
            return imageBlob.getBytes(1, (int) imageBlob.length());
        } else {
            throw new RuntimeException("No image found for product with ID: " + productId);
        }
    }

    public Product addProduct(
            String name, String desc, String brand, double price, String category,
            boolean available, int quantity, String releaseDateString, MultipartFile file
    ) throws IOException, ParseException, SQLException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate = sdf.parse(releaseDateString);

        Product product = new Product(name, desc, brand, price, category, releaseDate, available, quantity);

        // Check if the product with the same ID exists
        if (repo.existsById(product.getId())) {
            throw new RuntimeException("Product with this ID already exists!");
        } else {
            if (!file.isEmpty()) {
                byte[] imageBytes = file.getBytes();
                Blob photoBlob = new SerialBlob(imageBytes);
                product.setImage(photoBlob);
            }
            return repo.save(product);
        }
    }


    public Product updateProduct(
            int id, String name, String desc, String brand, double price,
            String category, boolean available, int quantity,
            String releaseDateString, MultipartFile file)
            throws IOException, SQLException, ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate = sdf.parse(releaseDateString);

        Product existingProduct = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(name);
        existingProduct.setDesc(desc);
        existingProduct.setBrand(brand);
        existingProduct.setPrice(price);
        existingProduct.setCategory(category);
        existingProduct.setAvailable(available);
        existingProduct.setQuantity(quantity);
        existingProduct.setReleaseDate(releaseDate);

        if (file != null && !file.isEmpty()) {
            byte[] imageBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(imageBytes);
            existingProduct.setImage(photoBlob);
        }
        return repo.save(existingProduct);
    }


    public void deleteProduct(int id) {
        repo.deleteById(id);
    }

    public List<Product> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }
}
