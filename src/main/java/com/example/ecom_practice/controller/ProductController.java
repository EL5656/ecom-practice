package com.example.ecom_practice.controller;

import com.example.ecom_practice.dto.ProductResponseDto;
import com.example.ecom_practice.model.Product;
import com.example.ecom_practice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){
        Product product = service.getProductById(id);
        if(product != null){
            return new ResponseEntity<>(product,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> createProduct(
                                                 @RequestParam("name") String name,
                                                 @RequestParam("desc") String desc,
                                                 @RequestParam("brand") String brand,
                                                 @RequestParam("price") double price,
                                                 @RequestParam("category") String category,
                                                 @RequestParam("releaseDate") String releaseDate,
                                                 @RequestParam(name = "available", required = false, defaultValue = "false") boolean available,
                                                 @RequestParam("quantity") int quantity,
                                                 @RequestParam("image") MultipartFile image)
            throws IOException, SQLException, ParseException {

            Product savedProduct = service.addProduct(name, desc, brand, price, category, available, quantity, releaseDate, image);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedReleaseDate = dateFormat.format(savedProduct.getReleaseDate());

            ProductResponseDto responseDto = new ProductResponseDto(savedProduct.getId(), savedProduct.getName(), savedProduct.getBrand(),
                    savedProduct.getDesc(), savedProduct.getPrice(), savedProduct.getCategory(), formattedReleaseDate,savedProduct.isAvailable(),
                    savedProduct.getQuantity(),  savedProduct.getImage() != null ? savedProduct.getImage().getBytes(1, (int) savedProduct.getImage().length()) : null);
       return ResponseEntity.ok(responseDto);
    }

    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId) {
        try {
            byte[] imageBytes = service.getImageByProductId(productId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return ResponseEntity.ok().headers(headers).body(imageBytes);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/product/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable int id,
            @RequestParam String name,
            @RequestParam String desc,
            @RequestParam String brand,
            @RequestParam double price,
            @RequestParam String category,
            @RequestParam boolean available,
            @RequestParam int quantity,
            @RequestParam String releaseDate,
            @RequestParam(required = false) MultipartFile file) {

        try {
            // Call service directly with parameters
            Product updatedProduct = service.updateProduct(id, name, desc, brand, price, category, available, quantity, releaseDate, file);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product = service.getProductById(id);
        if(product!=null){
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        }else
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
    }
    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(String keyword){
        System.out.println("searching with"+keyword);
        List<Product> products = service.searchProducts(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
}
