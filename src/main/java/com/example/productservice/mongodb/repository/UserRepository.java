package com.example.productservice.mongodb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.productservice.mongodb.domain.Company;
import com.example.productservice.mongodb.domain.User;
import com.example.productservice.mongodb.dto.UserInfoDto;

public interface UserRepository {

	User addUser(User user);

	Company addCompany(Company company);

	Page<UserInfoDto> getUserList(String search, Pageable pageable);

	boolean existsByEmail(String email);

	boolean existsByMobileNumber(long mobileNumber);

	User findByEmail(String email);

	UserInfoDto getUserById(String userId);

	User findById(String userId);


}
