package com.example.productservice.mongodb.domain;

import java.time.LocalDateTime;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.productservice.mongodb.dto.UserInfoDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Company {

	@Id
	private String companyId;

	private String companyName;

	private int totalEmployees;

	private String userId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime createdDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime updatedDate;

	public Company(String companyName, int totalEmployees, String userId) {
		super();
		this.companyName = companyName;
		this.totalEmployees = totalEmployees;
		this.userId = userId;
		this.createdDate = LocalDateTime.now();
		this.updatedDate = LocalDateTime.now();
	}

	/*
	 * public Company(CompanyDto companyDto) { this.cid=companyDto.getCid();
	 * this.companyName=companyDto.getCompanyName();
	 * this.totalEmployees=companyDto.getTotalEmployees();
	 * this.uid=companyDto.getUid(); if(companyDto.getCid()==null) {
	 * this.createdDate=LocalDateTime.now(); } this.updatedDate=LocalDateTime.now();
	 * }
	 */

	public Company(UserInfoDto commonDto)
	{
		this.companyId=commonDto.getCompanyId();
		this.companyName=commonDto.getCompanyName();
		this.totalEmployees=commonDto.getTotalEmployees();
		this.userId=commonDto.getUserId();
		this.createdDate = LocalDateTime.now();
		this.updatedDate = LocalDateTime.now();
	}
}
