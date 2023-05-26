package com.example.productservice.mongodb.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.dto.ProductUserDto;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InsufficientDataException;
import com.example.productservice.mongodb.exception.InvalidProductException;
import com.example.productservice.mongodb.service.ProductUserService;



@RestController
@RequestMapping("/product/user")
public class ProductUserController {

	private final Logger logger=LogManager.getLogger(ProductUserController.class);

	private final ProductUserService productUserService;


	public ProductUserController(ProductUserService productUserService) {
		this.productUserService = productUserService;

	}

	@PostMapping("/add")
	public ResponseEntity<ProductUserDto> addProducts(@RequestBody ProductUserDto productUserDto, @RequestParam Status status) throws InvalidProductException, EntityDoesNotExistException, InsufficientDataException{
		logger.info("ProductUserController.addProducts()");
		return ResponseEntity.ok().body(productUserService.addProducts(productUserDto,status));
	}


}
