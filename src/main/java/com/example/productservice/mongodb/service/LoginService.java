package com.example.productservice.mongodb.service;

import com.example.productservice.mongodb.dto.UserDto;
import com.example.productservice.mongodb.exception.InsufficientDataException;

public interface LoginService {

	UserDto login(String email, String password) throws InsufficientDataException;

	//boolean validatePassword(String email, String password);

}
