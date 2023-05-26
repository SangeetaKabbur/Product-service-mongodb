package com.example.productservice.mongodb.repository;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

import com.example.productservice.mongodb.domain.Company;
import com.example.productservice.mongodb.domain.User;
import com.example.productservice.mongodb.dto.UserInfoDto;

public interface UserRepository {

	User addUser(User user);

	Company addCompany(Company company);

	Page<UserInfoDto> getUserList(String search, Pageable pageable);


}
