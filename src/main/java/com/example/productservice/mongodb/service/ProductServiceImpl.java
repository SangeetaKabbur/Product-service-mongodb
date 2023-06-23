package com.example.productservice.mongodb.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.productservice.mongodb.constants.Role;
import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.domain.Product;
import com.example.productservice.mongodb.domain.User;
import com.example.productservice.mongodb.dto.ProductDto;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InsufficientDataException;
import com.example.productservice.mongodb.exception.InvalidProductException;
import com.example.productservice.mongodb.exception.MethodNotAllowedException;
import com.example.productservice.mongodb.repository.ProductRepository;
import com.example.productservice.mongodb.repository.UserRepository;
import com.example.productservice.mongodb.validation.ProductValidation;
import com.mongodb.client.result.DeleteResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ProductServiceImpl implements ProductService {


	private final Logger logger = LogManager.getLogger(ProductServiceImpl.class);
	//@Autowired
	private final ProductRepository productRepository;

	private final UserRepository userRepository;

	public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository) {
		this.productRepository = productRepository;
		this.userRepository=userRepository;
	}

	@Override
	public ProductDto addProduct(ProductDto productDto, Status status,String userId) throws InvalidProductException, InsufficientDataException, EntityDoesNotExistException {
		logger.info("ProductServiceImpl.addProduct()");
		ProductValidation.validateProduct(productDto);
		ProductValidation.validateProductUserId(userId);
		validate(userId);
		Product product = new Product(productDto); // Convert ProductDto to Product entity
		product.setStatus(status);
		product.setUserId(userId);
		productRepository.addProduct(product); // Save the product using the repository
		productDto.setId(product.getId()); // Set the generated ID in the ProductDto
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
	public ProductDto updateProductInfo(ProductDto productDto,String userId) throws InvalidProductException, EntityDoesNotExistException, InsufficientDataException, MethodNotAllowedException {
		logger.info("ProductServiceImpl.updateProductInfo()");
		//check if product  id is null or empty
		if(productDto.getId()==null || productDto.getId().equals("")) {
			throw new InvalidProductException("id cant be null or empty");
		}

		//validation
		if(productDto.getPrice().doubleValue()<=0) {
			throw new InvalidProductException("Price should be more than 0");
		}

		Product product = productRepository.findByKeyNameAndValue("_id",productDto.getId());
		if (product == null) {
			throw new EntityDoesNotExistException("Product does not exist.");
		}
		// Check if the provided userId matches the userId of the product
		if (!product.getUserId().equals(userId)) {
			throw new MethodNotAllowedException("Access denied. This product does not belong to the specified user.");
		}
		validate(userId);
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

	@Override
	public List<ProductDto> getProductByName(String name) {
		logger.info("ProductServiceImpl.getProductByName()");
		List<Product> products= productRepository.findByName(name);
		return products.stream().map(p -> new ProductDto(p)).collect(Collectors.toList());
	}

	@Override
	public DeleteResult deleteProductById(String id,String userId) throws EntityDoesNotExistException, InsufficientDataException, MethodNotAllowedException {
		logger.info("ProductServiceImpl.deleteProduct()");
		Product product = productRepository.findByKeyNameAndValue("_id",id);
		if (product == null) {
			throw new EntityDoesNotExistException("Product does not exist.");
		}
		// Check if the provided userId matches the userId of the product
		if (!product.getUserId().equals(userId)) {
			throw new MethodNotAllowedException("Access denied. This product does not belong to the specified user.");
		}
		validate(userId);
		return productRepository.deleteProductById(userId);

	}

	@Override
	public Page<ProductDto> getProductList(String search, Status status, Pageable pageable,String userId) throws EntityDoesNotExistException, InsufficientDataException {
		logger.info("ProductServiceImpl.getCustomerList()");
		if(!(productRepository.findUserByIdd(userId))) {
			throw new EntityDoesNotExistException("user not found with id "+userId);
		}
		User user = userRepository.findById(userId);
		if (user.getRole() == Role.CUSTOMER) {
			return productRepository.getProductList(search,Status.ACTIVE, pageable,userId);
		}
		return productRepository.getProductList(search, status, pageable,userId);
	}

	@Override
	public List<ProductDto> getProductsByStatus(Status status, int page, int size) {
		logger.info("ProductServiceImpl.getProductsByStatus()");
		if (status != null) {
			List<Product> products = productRepository.findByStatusAndPaginated(status, page, size);
			return products.stream().map(ProductDto::new).collect(Collectors.toList());
		} else {
			List<Product> paginatedProducts = productRepository.getPaginatedProducts(page, size);
			return paginatedProducts.stream().map(ProductDto::new).collect(Collectors.toList());
		}
	}

	@Override
	public ProductDto updateProductStatus(String id, Status status) {
		logger.info("ProductServiceImpl.updateProductStatus()");
		Product product=productRepository.findByKeyNameAndValue("_id",id);
		product.setStatus(status);
		product.setUpdatedDate(LocalDateTime.now());
		productRepository.save(product);
		return new ProductDto(product);
	}

	private  void validate(String userId) throws EntityDoesNotExistException, InsufficientDataException {
		User user = userRepository.findById(userId);
		if(user==null) {
			throw new EntityDoesNotExistException("user not found with id "+userId);
		}

		if (user.getRole() != Role.SELLER) {
			throw new InsufficientDataException("Only sellers are allowed to add,update,delete products.");
		}
	}

	@Override
	public ProductDto getProductByIdd(String id,String userId) throws EntityDoesNotExistException, MethodNotAllowedException {
		logger.info("ProductServiceImpl.getProductById()");
		Product product = productRepository.findByKeyNameAndValue("_id",id);
		if (product == null) {
			throw new EntityDoesNotExistException("Product does not exist.");
		}
		if(userRepository.findById(userId)== null) {
			throw new EntityDoesNotExistException("user not found with id "+userId);
		}
		// Check if the provided userId matches the userId of the product
		if (!product.getUserId().equals(userId)) {
			throw new MethodNotAllowedException("Access denied. This product does not belong to the specified user.");
		}
		return new ProductDto(product);
	}

	@Override
	public Product getProductById(String productId) throws EntityDoesNotExistException {
		Product product = productRepository.findByKeyNameAndValue("_id",productId);
		if (product == null) {
			throw new EntityDoesNotExistException("Product does not exist.");
		}
		return product;
	}

}