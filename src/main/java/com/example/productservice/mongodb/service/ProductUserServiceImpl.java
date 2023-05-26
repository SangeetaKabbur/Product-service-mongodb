package com.example.productservice.mongodb.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.domain.ProductUser;
import com.example.productservice.mongodb.domain.User;
import com.example.productservice.mongodb.dto.ProductUserDto;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InsufficientDataException;
import com.example.productservice.mongodb.exception.InvalidProductException;
import com.example.productservice.mongodb.repository.ProductUserRepository;
import com.example.productservice.mongodb.validation.ProductUserValidation;

@Service
public class ProductUserServiceImpl implements ProductUserService{
	
	private final Logger logger=LogManager.getLogger(ProductUserServiceImpl.class);
	
	private final ProductUserRepository productUserRepository;

	public ProductUserServiceImpl(ProductUserRepository productUserRepository) {
		this.productUserRepository = productUserRepository;
	}

	@Override
	public ProductUserDto addProducts(ProductUserDto productUserDto,Status status) throws InvalidProductException, EntityDoesNotExistException, InsufficientDataException{
		logger.info("ProductUserServiceImpl.addProducts()");
		ProductUserValidation.validateProductUser(productUserDto);
		ProductUserValidation.validateProductUserId(productUserDto);
		User user=productUserRepository.findUserById(productUserDto.getUserId());
		if(user==null)
		{
			throw new EntityDoesNotExistException("user not found with id "+productUserDto.getUserId());
		}
        ProductUser productUser = new ProductUser(productUserDto);
        productUser.setStatus(status);
        productUserRepository.addProduct(productUser);
        productUserDto.setId(productUser.getId());
        return productUserDto;
	}
}
