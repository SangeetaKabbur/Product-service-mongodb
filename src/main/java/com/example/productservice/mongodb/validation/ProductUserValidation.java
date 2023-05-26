package com.example.productservice.mongodb.validation;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;


import com.example.productservice.mongodb.dto.ProductUserDto;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InsufficientDataException;
import com.example.productservice.mongodb.exception.InvalidProductException;

public class ProductUserValidation {
	
	 private ProductUserValidation(){}


	    public static void validateProductUser(ProductUserDto productUser) throws InvalidProductException{
	        List<String> errorMessages = new ArrayList<>();

	        if(!StringUtils.hasText(productUser.getName())){
	            errorMessages.add("name");
	        }

	        if(productUser.getPrice()==null || productUser.getPrice().longValue()<=0){
	            errorMessages.add("price");
	        }

	        if(!errorMessages.isEmpty()){
	            throw new InvalidProductException(String.join(",", errorMessages));
	        }
	    }
	    
	    public static void validateProductUserId(ProductUserDto productUser) throws InsufficientDataException {
	    	if(productUser.getUserId()==null){
	    		throw new InsufficientDataException("userId cannot be null");
	    	}
	    }
}