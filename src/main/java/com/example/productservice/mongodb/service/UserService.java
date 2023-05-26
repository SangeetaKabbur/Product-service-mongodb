package com.example.productservice.mongodb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.dto.UserInfoDto;
import com.example.productservice.mongodb.dto.CompanyDto;
import com.example.productservice.mongodb.dto.ProductDto;
import com.example.productservice.mongodb.dto.UserDto;

public interface UserService {

	UserDto addUserInfo(UserDto userDto);

	CompanyDto addCompanyInfo(CompanyDto companyDto);

	Page<UserInfoDto> getUserList(String search, Pageable pageable);

}
