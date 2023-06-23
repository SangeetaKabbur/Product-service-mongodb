package com.example.productservice.mongodb.domain;

import java.time.LocalDateTime;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.productservice.mongodb.dto.UserInfoDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.example.productservice.mongodb.constants.Role;
import com.example.productservice.mongodb.dto.SaveUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {

	@Id
	private String userId;

	private String name;

	private long mobileNumber;

	private String email;

	private String password;
	
	private Role role;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime createdDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime updatedDate;

	public User(SaveUser saveUser)
	{
		//this.userId=userDto.getUserId();
		this.name=saveUser.getName();
		this.mobileNumber=saveUser.getMobileNumber();
		this.email=saveUser.getEmail();
		this.password=saveUser.getPassword();

		this.createdDate=LocalDateTime.now();

		this.updatedDate=LocalDateTime.now();
	}

	public User(UserInfoDto commonDto)
	{
		this.userId=commonDto.getUserId();
		this.name=commonDto.getName();
		this.email=commonDto.getEmail();
		this.createdDate=LocalDateTime.now();
		this.updatedDate=LocalDateTime.now();
	}


}
