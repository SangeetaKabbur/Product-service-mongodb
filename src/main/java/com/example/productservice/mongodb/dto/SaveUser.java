package com.example.productservice.mongodb.dto;


import com.example.productservice.mongodb.domain.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaveUser extends UserDto {
	
	private String password;

    public SaveUser(User user)
    {
    	this.password=user.getPassword();
    }
}
