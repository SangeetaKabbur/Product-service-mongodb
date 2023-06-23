package com.example.productservice.mongodb.validation;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.example.productservice.mongodb.dto.SaveUser;
import com.example.productservice.mongodb.exception.InsufficientDataException;

public class UserValidation {
	
	public static void validateUser(SaveUser saveUser) throws InsufficientDataException {
		if(StringUtils.isBlank(saveUser.getEmail())) {
			throw new InsufficientDataException("email cannot be empty");
		}

		if(StringUtils.isBlank(saveUser.getPassword())) {
			throw new InsufficientDataException("password cannot be empty");
		}

		if(StringUtils.isBlank(saveUser.getName())) {
			throw new InsufficientDataException("Name cannot be empty");
		}

		List<String> errorMessages=new ArrayList<>();

		if(saveUser.getName().length()<2) {
			errorMessages.add("User name should contain at least 2 characters.");
		}

		if (!isPasswordValid(saveUser.getPassword())) {
			errorMessages.add("Password must contain at least 8 characters, "
					+ "one special character, both upper and lower case letters, and one numeric character.");
		}

		if(!isEmailValid(saveUser.getEmail())) {
			errorMessages.add("invalid email.");
		}

		if (!errorMessages.isEmpty()) {
			String errorMessage = String.join(", ", errorMessages);
			throw new InsufficientDataException(errorMessage);
		}
	}

	static String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=.*[a-zA-Z\\d@#$%^&+=]).{8,}$";

	public static boolean isPasswordValid(String password) {
		return password.matches(passwordRegex);
	}

	static String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

	public static boolean isEmailValid(String password) {
		return password.matches(emailRegex);
	}
}