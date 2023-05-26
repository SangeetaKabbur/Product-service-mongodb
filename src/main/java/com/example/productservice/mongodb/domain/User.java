package com.example.productservice.mongodb.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.productservice.mongodb.dto.UserInfoDto;
import com.example.productservice.mongodb.dto.UserDto;

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

	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;
	
	public User(UserDto userDto)
	{
		this.userId=userDto.getUserId();
		this.name=userDto.getName();
		this.mobileNumber=userDto.getMobileNumber();
		this.email=userDto.getEmail();
		if(userDto.getUserId()==null)
		{
			this.createdDate=LocalDateTime.now();
		}
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
