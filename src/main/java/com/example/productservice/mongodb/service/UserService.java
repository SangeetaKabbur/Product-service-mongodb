package com.example.productservice.mongodb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.productservice.mongodb.dto.UserInfoDto;
import com.example.productservice.mongodb.exception.InsufficientDataException;
import com.example.productservice.mongodb.exception.ResourseAlreadyExistException;
import com.example.productservice.mongodb.constants.Role;
import com.example.productservice.mongodb.dto.CompanyDto;
import com.example.productservice.mongodb.dto.SaveUser;


public interface UserService {

	SaveUser addUserInfo(SaveUser saveUser,Role role) throws ResourseAlreadyExistException, InsufficientDataException;

	CompanyDto addCompanyInfo(CompanyDto companyDto);

	Page<UserInfoDto> getUserList(String search, Pageable pageable);

	UserInfoDto getUserById(String userId);

}
