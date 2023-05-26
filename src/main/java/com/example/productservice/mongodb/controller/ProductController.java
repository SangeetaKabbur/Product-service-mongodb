package com.example.productservice.mongodb.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.dto.CustomPagination;
import com.example.productservice.mongodb.dto.ProductDto;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InvalidProductException;
import com.example.productservice.mongodb.service.ProductService;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/product")
public class ProductController {

	private final Logger logger = LogManager.getLogger(ProductController.class);

	//	@Autowired
	private final ProductService productService;


	public ProductController(ProductService productService) {
		this.productService = productService;

	}

	@PostMapping("/add")
	public ResponseEntity<ProductDto> addProducts(@RequestBody ProductDto productDto) throws InvalidProductException
	{
		logger.info("ProductController.addProducts()");
		return ResponseEntity.ok().body(productService.addProducts(productDto));
	}

	@PostMapping("/addstatus")
	public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto,
			@RequestParam Status status) throws InvalidProductException {
		logger.info("ProductController.addProducts()");
		return ResponseEntity.ok().body(productService.addProduct(productDto, status));
	}

	@GetMapping("/get")
	public ResponseEntity<List<ProductDto>> getAllProducts(){

		logger.info("ProductController.getAllProducts()");
		return ResponseEntity.ok().body(productService.getAllProducts());
	}

	@GetMapping("/getByName")
	public ResponseEntity<List<ProductDto>> getProductByName(@RequestParam(required = false) String name) throws EntityDoesNotExistException{
		logger.info("ProductController.getProductByName()");
		return ResponseEntity.ok().body(productService.getProductByName(name));
	}

	@GetMapping("/active")
	public ResponseEntity<List<ProductDto>> getActiveProducts() {
		logger.info("ProductController.getActiveProducts()");
		return ResponseEntity.ok().body(productService.getActiveProducts());
	}

	@GetMapping("/getByStatus/pagination")
	public ResponseEntity<List<ProductDto>> getProductsByStatus(@RequestParam(required = false) Status status,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		logger.info("ProductController.getProductsByStatus()");
		List<ProductDto> products = productService.getProductsByStatus(status, page, size);
		return ResponseEntity.ok().body( products);
	}

	@PutMapping("/update/status")
	public ResponseEntity<ProductDto> updateProductStatus(@RequestParam String id, @RequestParam Status status)
	{
		logger.info("ProductController.updateProductStatus()");
		return ResponseEntity.ok().body(productService.updateProductStatus(id,status));
	}

	@PutMapping("/{name}")
	public ResponseEntity<ProductDto> updateProduct(@PathVariable String name, @RequestBody ProductDto productDto) {
		logger.info("ProductController.updateProduct()");
		return ResponseEntity.ok().body(productService.updateProduct(name, productDto));

	}

	@PutMapping("/update-info")
	public ResponseEntity<ProductDto> updateProductInfo(@RequestBody ProductDto productDto) throws InvalidProductException, EntityDoesNotExistException {
		logger.info("ProductController.updateProductInfo()");
		return ResponseEntity.ok().body(productService.updateProductInfo(productDto));
	}

	@GetMapping("/pagination/{page}/{size}")
	public ResponseEntity<List<ProductDto>> getProducts(@PathVariable int page,@PathVariable int size) {
		logger.info("ProductController.getProducts()");
		return ResponseEntity.ok().body(productService.getPaginatedProducts(page, size));

	}

	@DeleteMapping("/delete")
	public void deleteById(@RequestBody ProductDto productDto)
	{
		logger.info("ProductController.deleteById()");
		productService.deleteById(productDto);
	}

	@DeleteMapping("/deleteById")
	public ResponseEntity<HttpStatus> deleteProduct(@RequestParam String id)
	{
		logger.info("ProductController.deleteProduct()");
		try
		{
			this.productService.deleteProduct(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception e)
		{
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/page/list")
	public CustomPagination getProductList(@RequestParam(required = false) String search,
			@RequestParam(required = false) Status status, @PageableDefault(value = 6) Pageable pageable,
			PagedResourcesAssembler pagedResourcesAssembler) {
		logger.info("UserResource.getClientLists()");
		Page<ProductDto> product =productService.getProductList(search, status, pageable);
		PagedModel<ProductDto> pageResult = pagedResourcesAssembler.toModel(product);
		return new CustomPagination(pageResult.getContent(), product.getSize(), product.getTotalElements(),
				product.getNumber(), product.getTotalPages(), pageResult.getLinks());
	}

}
