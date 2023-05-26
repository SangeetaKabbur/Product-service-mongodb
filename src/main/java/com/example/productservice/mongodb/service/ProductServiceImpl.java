package com.example.productservice.mongodb.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.domain.Product;
import com.example.productservice.mongodb.dto.ProductDto;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InvalidProductException;
import com.example.productservice.mongodb.repository.ProductRepository;
import com.example.productservice.mongodb.validation.ProductValidation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ProductServiceImpl implements ProductService {


	private final Logger logger = LogManager.getLogger(ProductServiceImpl.class);
	//@Autowired
	private final ProductRepository productRepository;

	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	@Override
	public ProductDto addProducts(ProductDto productDto) throws InvalidProductException {
		logger.info("ProductServiceImpl.addProducts()");
		ProductValidation.validateProduct(productDto);
		Product product = new Product(productDto);
		productRepository.addProducts(product);
		productDto.setId(product.getId());
		return productDto;
	} 

	@Override
	public ProductDto addProduct(ProductDto productDto, Status status) throws InvalidProductException {
		logger.info("ProductServiceImpl.addProduct()");
		ProductValidation.validateProduct(productDto);
		Product product = new Product(productDto); // Convert ProductDto to Product entity
		product.setStatus(status);
		productRepository.addProducts(product);  // Save the product using the repository
		productDto.setId(product.getId());   // Set the generated ID in the ProductDto
		return productDto;
	}


	@Override
	public List<ProductDto> getAllProducts() {
		logger.info("ProductServiceImpl.getAllProducts()");
		List<Product> products= productRepository.find();
		return products.stream().map(p -> new ProductDto(p)).collect(Collectors.toList());


	}

	@Override
	public List<ProductDto> getActiveProducts() {
		logger.info("ProductServiceImpl.getActiveProducts()");
		List<Product> activeProducts = productRepository.findByStatus(Status.ACTIVE);
		return activeProducts.stream().map(p -> new ProductDto(p)).collect(Collectors.toList());
	}


	@Override
	public ProductDto deleteProduct(ProductDto productDto) {
		logger.info("ProductServiceImpl.deleteProduct()");
		Product product = new Product(productDto);
		productRepository.delete(product);
		return productDto;

	}

	@Override
	public ProductDto updateProduct(String name, ProductDto productDto) {
		logger.info("ProductServiceImpl.updateProduct()");
		productRepository.updateProduct(name, productDto);
		return productDto;
	}
	@Override
	public ProductDto updateProductInfo(ProductDto productDto) throws InvalidProductException, EntityDoesNotExistException {

		logger.info("ProductServiceImpl.updateProductInfo()");
		//check if product  id is null or empty
		if(productDto.getId()==null || productDto.getId().equals("")) {
			throw new InvalidProductException("id cant be null or empty");
		}

		//validation
		if(productDto.getPrice().doubleValue()<=0) {
			throw new InvalidProductException("Price should be more than 0");
		}

		Product product = productRepository.findByKeyNameAndValue("_id", productDto.getId());
		if(product==null) {
			throw new EntityDoesNotExistException("product not found with id :"+productDto.getId());
		}

		product.updateInfo(productDto);
		productRepository.save(product);
		return new ProductDto(product);
	}
	@Override
	public List<ProductDto> getPaginatedProducts(int page, int size) {
		logger.info("ProductServiceImpl.getPaginatedProducts()");
		List<Product> product=productRepository.getPaginatedProducts(page,size);
		return product.stream().map(ProductDto::new).collect(Collectors.toList());
	}
	@Override
	public void deleteById(ProductDto productDto) {
		logger.info("ProductServiceImpl.deleteById()");
		productRepository.deleteById("_id", productDto.getId());
	}
	
	//	@Override
	//	public ProductDto getProductByName(String name) throws EntityDoesNotExistException {
	//		Product product = productRepository.getProductByName("name",name);
	//		return new ProductDto(product);
	//	}
	@Override
	public List<ProductDto> getProductByName(String name) {
		logger.info("ProductServiceImpl.getProductByName()");
		List<Product> products= productRepository.findByName(name);
		return products.stream().map(p -> new ProductDto(p)).collect(Collectors.toList());
	}
	@Override
	public void deleteProduct(String id) {
		logger.info("ProductServiceImpl.deleteProduct()");
		productRepository.deleteById(id);
	}

	@Override
	public Page<ProductDto> getProductList(String search, Status status, Pageable pageable){
		logger.info("UserServiceImpl.getCustomerList()");
		return productRepository.getProductList(search, status, pageable);
	}
	@Override
	public List<ProductDto> getProductsByStatus(Status status, int page, int size) {

		if (status != null) {
			List<Product> products = productRepository.findByStatusAndPaginated(status, page, size);
			return products.stream().map(p -> new ProductDto(p)).collect(Collectors.toList());
		} else {
			List<Product> paginatedProducts = productRepository.getPaginatedProducts(page, size);
			return paginatedProducts.stream().map(p -> new ProductDto(p)).collect(Collectors.toList());
		}
	}
	@Override
	public ProductDto updateProductStatus(String id, Status status) {
		logger.info("UserServiceImpl.updateProductStatus()");
		Product product=productRepository.findByKeyNameAndValue("_id",id);
		product.setStatus(status);
		product.setUpdatedDate(LocalDateTime.now());
		productRepository.save(product);
		return new ProductDto(product);
	}
}





