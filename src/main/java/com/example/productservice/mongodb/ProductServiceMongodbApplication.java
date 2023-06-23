package com.example.productservice.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableAutoConfiguration
@EnableWebMvc
public class ProductServiceMongodbApplication {
	public static void main(String[] args) {	
		SpringApplication.run(ProductServiceMongodbApplication.class, args);
	}

}
 