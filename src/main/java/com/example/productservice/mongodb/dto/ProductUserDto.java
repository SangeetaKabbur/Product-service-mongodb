package com.example.productservice.mongodb.dto;

import java.math.BigDecimal;

import com.example.productservice.mongodb.domain.Product;
import com.example.productservice.mongodb.domain.ProductUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUserDto {
	
	private String id;
	
	private String userId;
	
	private String name;
	
	private BigDecimal price;
	
	private long inventory;
	

	public ProductUserDto(ProductUser productUser)
	{
		this.id=productUser.getId();
		this.userId=productUser.getUserId();
		this.name=productUser.getName();
		this.price=productUser.getPrice();
		this.inventory=productUser.getInventory();

	}
	
}
