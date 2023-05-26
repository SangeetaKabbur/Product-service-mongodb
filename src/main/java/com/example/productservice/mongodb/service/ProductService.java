package com.example.productservice.mongodb.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.dto.ProductDto;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InvalidProductException;

public interface ProductService {

	ProductDto addProducts(ProductDto productDto) throws InvalidProductException;

	List<ProductDto> getAllProducts();

	ProductDto deleteProduct(ProductDto productDto);
	
	void deleteProduct(String id);

	ProductDto updateProduct(String name, ProductDto productDto);

	ProductDto updateProductInfo(ProductDto productDto) throws InvalidProductException, EntityDoesNotExistException;

	List<ProductDto> getPaginatedProducts(int page, int size);

	void deleteById(ProductDto productDto);

	List<ProductDto> getProductByName(String name) throws EntityDoesNotExistException;

	Page<ProductDto> getProductList(String search, Status status, Pageable pageable);

	ProductDto addProduct(ProductDto productDto, Status status) throws InvalidProductException;

	List<ProductDto> getActiveProducts();

	List<ProductDto> getProductsByStatus(Status status, int page, int size);

	ProductDto updateProductStatus(String id, Status status);
}