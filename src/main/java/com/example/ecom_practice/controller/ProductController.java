package com.example.ecom_practice.controller;

import com.example.ecom_practice.dto.ProductResponseDto;
import com.example.ecom_practice.model.Product;
import com.example.ecom_practice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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


//    @GetMapping("product/{productId}/image")
//    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
//        Product product = service.getProductById(productId);
//        byte[] imageFile = product.getImage();
//        return ResponseEntity.ok()
//                .contentType(MediaType.valueOf((product.getImageType())))
//                .body(imageFile);
//    }

//    @PutMapping("/product/{id}")
//    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestPart Product product,
//                                                @RequestPart MultipartFile imageFile){
//        Product product1 = null;
//        try {
//            product1 = service.updateProduct(id, product, imageFile);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        if(product1!=null){
//            return new ResponseEntity<>("Updated", HttpStatus.OK);
//        }else{
//            return new ResponseEntity<>("Failed to update",HttpStatus.BAD_REQUEST);
//        }
//    }

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
