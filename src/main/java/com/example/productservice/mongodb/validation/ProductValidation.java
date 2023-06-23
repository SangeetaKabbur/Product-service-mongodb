package com.example.productservice.mongodb.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;
import com.example.productservice.mongodb.dto.ProductDto;
import com.example.productservice.mongodb.exception.InsufficientDataException;
import com.example.productservice.mongodb.exception.InvalidProductException;

public class ProductValidation {

	private ProductValidation(){}


	public static void validateProduct(ProductDto product) throws InvalidProductException{
		List<String> errorMessages = new ArrayList<>();

		if(!StringUtils.hasText(product.getName())){
			errorMessages.add("name");
		}

		if(product.getPrice()==null || product.getPrice().longValue()<=0){
			errorMessages.add("price");
		}

		if(!errorMessages.isEmpty()){
			throw new InvalidProductException(String.join(",", errorMessages));
		}
	}

	public static void validateProductUserId(String userId) throws InsufficientDataException {
		if(userId==null){
			throw new InsufficientDataException("userId cannot be null");
		}
	}
	
}

