package com.example.ecom_practice;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

@SpringBootApplication
public class EcomPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomPracticeApplication.class, args);
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();

		// Use DataSize to specify file size and request size
		factory.setMaxFileSize(DataSize.ofMegabytes(10));  // 10MB
		factory.setMaxRequestSize(DataSize.ofMegabytes(10));  // 10MB

		return factory.createMultipartConfig();
	}
}
