package com.example.productservice.mongodb.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.domain.Product;
import com.example.productservice.mongodb.dto.ProductDto;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InsufficientDataException;
import com.example.productservice.mongodb.exception.InvalidProductException;
import com.example.productservice.mongodb.exception.MethodNotAllowedException;
import com.mongodb.client.result.DeleteResult;

public interface ProductService {

	ProductDto addProduct(ProductDto productDto, Status status, String userId)
			throws InvalidProductException, InsufficientDataException, EntityDoesNotExistException;

	List<ProductDto> getAllProducts();

	ProductDto deleteProduct(ProductDto productDto);

	DeleteResult deleteProductById(String id,String userId) 
			throws EntityDoesNotExistException, InsufficientDataException, MethodNotAllowedException;

	ProductDto updateProduct(String name, ProductDto productDto);

	ProductDto updateProductInfo(ProductDto productDto, String userId)
			throws InvalidProductException, EntityDoesNotExistException, InsufficientDataException, MethodNotAllowedException;

	List<ProductDto> getPaginatedProducts(int page, int size);

	void deleteById(ProductDto productDto);

	List<ProductDto> getProductByName(String name) throws EntityDoesNotExistException;

	Page<ProductDto> getProductList(String search, Status status, Pageable pageable,String userId) 
			throws EntityDoesNotExistException, InsufficientDataException;

	List<ProductDto> getActiveProducts();

	List<ProductDto> getProductsByStatus(Status status, int page, int size);

	ProductDto updateProductStatus(String id, Status status);

	ProductDto getProductByIdd(String id,String userId) throws EntityDoesNotExistException, MethodNotAllowedException;

	Product getProductById(String productId) throws EntityDoesNotExistException;
}