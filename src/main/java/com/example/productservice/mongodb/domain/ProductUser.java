package com.example.productservice.mongodb.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.dto.ProductUserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class ProductUser {
	@Id
	private String id;
	
	private String userId;
	
	private String name;
	
	private BigDecimal price;
	
	private long inventory;
	
	private Status status;

	private LocalDateTime createdDate;
	
	private LocalDateTime updatedDate;
	
	public ProductUser(ProductUserDto productUserDto)
	{
		this.id=productUserDto.getId();
		this.userId=productUserDto.getUserId();
		this.name=productUserDto.getName();
		this.price=productUserDto.getPrice();
		this.inventory=productUserDto.getInventory();
		
		if(productUserDto.getId()==null)
		{
			this.createdDate=LocalDateTime.now();
		}
		this.updatedDate=LocalDateTime.now();
		
		
	}

}
