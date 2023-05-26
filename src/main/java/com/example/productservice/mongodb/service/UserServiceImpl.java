package com.example.productservice.mongodb.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.productservice.mongodb.domain.Company;
import com.example.productservice.mongodb.domain.User;
import com.example.productservice.mongodb.dto.UserInfoDto;
import com.example.productservice.mongodb.dto.CompanyDto;
import com.example.productservice.mongodb.dto.ProductDto;
import com.example.productservice.mongodb.dto.UserDto;
import com.example.productservice.mongodb.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	// @Autowired
	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDto addUserInfo(UserDto userDto) {
		logger.info("UserServiceImpl.addUserInfo()");
		User user = new User(userDto);
		userRepository.addUser(user);
		userDto.setUserId(user.getUserId());
		return userDto;
	}

	@Override
	public CompanyDto addCompanyInfo(CompanyDto companyDto) {
		logger.info("UserServiceImpl.addCompanyInfo()");
		Company company = new Company(companyDto.getCompanyName(), companyDto.getTotalEmployees(),
				companyDto.getUserId());
		userRepository.addCompany(company);
		return companyDto;
	}

	@Override
	public Page<UserInfoDto> getUserList(String search, Pageable pageable) {
		logger.info("UserServiceImpl.getUserList()");
		return userRepository.getUserList(search, pageable);
	}

}
