package com.example.productservice.mongodb.service;

import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.example.productservice.mongodb.domain.User;
import com.example.productservice.mongodb.dto.UserDto;
import com.example.productservice.mongodb.exception.InsufficientDataException;
import com.example.productservice.mongodb.repository.UserRepository;

@Service
public class LoginServiceImpl implements LoginService {


	private final Logger logger = LogManager.getLogger(LoginServiceImpl.class);
	private final UserRepository userRepository;
	private final Base64.Decoder base64Decoder;

	public LoginServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
		this.base64Decoder = Base64.getDecoder();
	}

	@Override
	public UserDto login(String email, String password) throws InsufficientDataException {
		logger.info("LoginServiceImpl.login()");
		User user = userRepository.findByEmail(email);
		if (user != null) {
			String storedPassword = decodePassword(user.getPassword());
			if(storedPassword.equals(password)){
				return new UserDto(user);
			}
		}
		throw new InsufficientDataException("Invalid email or password");
	}

	private String decodePassword(String encodedPassword) {
		byte[] decodedBytes = base64Decoder.decode(encodedPassword);
		return new String(decodedBytes);
	}
}