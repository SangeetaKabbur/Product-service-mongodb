package com.example.productservice.mongodb.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.domain.Product;
import com.example.productservice.mongodb.dto.ProductDto;
import com.mongodb.client.result.DeleteResult;

public interface ProductRepository {

	Product addProducts(Product product);

	//List<Product> find(String name);

	Product update(Product product);

	Product delete(Product product);

	Product findById(String id);

	Product findByName(String name, String key, String value);

	Product save(Product product);

	List<Product> getByPrice(BigDecimal price);
	
	void updateProduct(String name, ProductDto productDto);

	public Product findByKeyNameAndValue(String key, String value);
	
	public List<Product> findProductsByKeyNameAndValue(String key, String value);

	List<Product> getPaginatedProducts(int page, int size);

	void deleteById(String key, String value);

	Product getProductByName(String key, String value);
	
	List<Product> find();
	
    List<Product> findByName(String name);

	DeleteResult deleteById(String id);

	Page<ProductDto> getProductList(String search, Status status, Pageable pageable);

	List<Product> findByStatus(Status active);

	List<Product> findByStatusAndPaginated(Status status, int page, int size);

}