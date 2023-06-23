package com.example.productservice.mongodb.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.productservice.mongodb.dto.UserDto;
import com.example.productservice.mongodb.exception.InsufficientDataException;
import com.example.productservice.mongodb.service.LoginService;

@RestController
@RequestMapping("/user")
public class LoginController {

	private final Logger logger=LogManager.getLogger(LoginController.class);

	private final LoginService loginService;

	public LoginController(LoginService loginService) {
		this.loginService=loginService;
	}

	@PostMapping("/login")
	public ResponseEntity<UserDto> login(@RequestParam String email, @RequestParam String password) throws InsufficientDataException {
		logger.info("LoginController.login()");
		return ResponseEntity.ok().body(loginService.login(email, password));

		//        boolean isValid = loginService.validatePassword(email, password);
		//        if (isValid) {
		//            return ResponseEntity.ok("Login successful");
		//        } else {
		//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
		//        }
	}

}
