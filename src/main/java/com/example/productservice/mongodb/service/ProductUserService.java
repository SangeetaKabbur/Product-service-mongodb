package com.example.productservice.mongodb.service;

import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.dto.ProductUserDto;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InsufficientDataException;
import com.example.productservice.mongodb.exception.InvalidProductException;

public interface ProductUserService {

	ProductUserDto addProducts(ProductUserDto productUserDto, Status status) throws InvalidProductException, EntityDoesNotExistException, InsufficientDataException;

}
