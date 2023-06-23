package com.example.productservice.mongodb.dto;

import java.math.BigDecimal;

import com.example.productservice.mongodb.domain.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
	
	private String id;
	
	private String name;
	
	private BigDecimal price;
	
	private int inventory;

	public ProductDto(Product product)
	{
		this.id=product.getId();
		this.name=product.getName();
		this.price=product.getPrice();
		this.inventory=product.getInventry();


	}
}
