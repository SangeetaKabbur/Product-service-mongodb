package com.example.productservice.mongodb.dto;

import com.example.productservice.mongodb.domain.Company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
	
//	private String cid;

	private String companyName;

	private int totalEmployees;
	
	private String userId;
	
	public CompanyDto(Company company)
	{
		this.companyName=company.getCompanyName();
		this.totalEmployees=company.getTotalEmployees();
		this.userId=company.getUserId();
	
	}

}
