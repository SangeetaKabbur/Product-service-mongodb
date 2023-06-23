package com.example.productservice.mongodb.dto;

import com.example.productservice.mongodb.domain.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	
	private String userId;
	
    private String name;
	
	private long mobileNumber;
	
	private String email;
	
	
	public UserDto(User user) {
		this.userId=user.getUserId();
		this.name=user.getName();
		this.mobileNumber=user.getMobileNumber();
		this.email=user.getEmail();
	}
	
	
	

}
