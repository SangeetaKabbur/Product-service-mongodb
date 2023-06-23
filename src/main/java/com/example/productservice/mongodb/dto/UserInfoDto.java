package com.example.productservice.mongodb.dto;

import com.example.productservice.mongodb.domain.Company;
import com.example.productservice.mongodb.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

	private String userId;

	private String name;

	private long mobileNumber;

	private String email;
	
	private String companyId;

	private String companyName;

	private int totalEmployees;
	
	
	//private Object company;

	public UserInfoDto(Company company)
	{
		this.companyId=company.getCompanyId();
		this.companyName=company.getCompanyName();
		this.totalEmployees=company.getTotalEmployees();
		this.userId=company.getUserId();

	}
	public UserInfoDto(User user)
	{
		this.userId=user.getUserId();
		this.name=user.getName();
		this.mobileNumber=user.getMobileNumber();
		this.email=user.getEmail();
		
	}
}