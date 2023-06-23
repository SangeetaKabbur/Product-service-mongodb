package com.example.productservice.mongodb.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.productservice.mongodb.dto.UserInfoDto;
import com.example.productservice.mongodb.exception.InsufficientDataException;
import com.example.productservice.mongodb.exception.ResourseAlreadyExistException;
import com.example.productservice.mongodb.constants.Role;
import com.example.productservice.mongodb.dto.CompanyDto;
import com.example.productservice.mongodb.dto.CustomPagination;
import com.example.productservice.mongodb.dto.SaveUser;
import com.example.productservice.mongodb.service.UserService;


@RestController
@RequestMapping("/user")
public class UserController {

	private final Logger logger = LogManager.getLogger(UserController.class);

	//@Autowired
	private final UserService userService;


	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/add/user")
	public ResponseEntity<SaveUser> addUserInfo(@RequestBody SaveUser saveUser,@RequestParam Role role) throws ResourseAlreadyExistException, InsufficientDataException {
		logger.info("UserController.addUserInfo()");
		return ResponseEntity.ok().body(userService.addUserInfo(saveUser,role));
	}

	@PostMapping("/add/company")
	public ResponseEntity<CompanyDto> addCompanyInfo(@RequestBody CompanyDto companyDto){
		logger.info("UserController.addCompanyInfo()");
		return ResponseEntity.ok().body(userService.addCompanyInfo(companyDto));
	}

	@GetMapping("/page/list")
	public CustomPagination getUserList(@RequestParam(required = false) String search,
			@PageableDefault(value = 6) Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler) {
		logger.info("UserController.getUserList()");
		Page<UserInfoDto> user =userService.getUserList(search , pageable);
		PagedModel<UserInfoDto> pageResult = pagedResourcesAssembler.toModel(user);
		return new CustomPagination(pageResult.getContent(), user.getSize(), user.getTotalElements(),
				user.getNumber(), user.getTotalPages(), pageResult.getLinks());
	}
	
	@GetMapping("/getUserById/{userId}")
	public ResponseEntity<UserInfoDto> getUserById(@PathVariable(value="userI d") String userId){
		logger.info("UserController.getUserById()");
		return ResponseEntity.ok().body(userService.getUserById(userId));
	}
}