package com.example.productservice.mongodb.service;

import java.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.productservice.mongodb.constants.Role;
import com.example.productservice.mongodb.domain.Company;
import com.example.productservice.mongodb.domain.User;
import com.example.productservice.mongodb.dto.UserInfoDto;
import com.example.productservice.mongodb.exception.InsufficientDataException;
import com.example.productservice.mongodb.exception.ResourseAlreadyExistException;
import com.example.productservice.mongodb.dto.CompanyDto;
import com.example.productservice.mongodb.dto.SaveUser;
import com.example.productservice.mongodb.repository.UserRepository;
import com.example.productservice.mongodb.validation.UserValidation;

@Service
public class UserServiceImpl implements UserService {

	private final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	// @Autowired
	private final UserRepository userRepository;
	private final Base64.Encoder base64Encoder;
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.base64Encoder = Base64.getEncoder();
	}

	@Override
	public SaveUser addUserInfo(SaveUser saveUser,Role role) throws ResourseAlreadyExistException, InsufficientDataException {
		logger.info("UserServiceImpl.addUserInfo()");
		UserValidation.validateUser(saveUser);
		// Check if email is unique
		if (userRepository.existsByEmail(saveUser.getEmail())) {
			throw new ResourseAlreadyExistException("Email is already registered");
		}
		// Check if mobile number is unique
		if (userRepository.existsByMobileNumber(saveUser.getMobileNumber())) {
			throw new ResourseAlreadyExistException("Mobile number is already registered");
		}

		String encodedString=base64Encoder.encodeToString(saveUser.getPassword().getBytes());
		saveUser.setPassword(encodedString);
		
		User user = new User(saveUser);
		user.setRole(role);
		userRepository.addUser(user);
		saveUser.setUserId(user.getUserId());
		return saveUser;
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

	@Override
	public UserInfoDto getUserById(String userId) {
		logger.info("UserServiceImpl.getUserById()");
		return userRepository.getUserById(userId);
	}
}