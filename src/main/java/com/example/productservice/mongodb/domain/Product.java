package com.example.productservice.mongodb.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.dto.ProductDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
	@Id
	private String id;

	private String userId;

	private String name;

	private BigDecimal price;

	private int inventry;

	private Status status;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime createdDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime updatedDate;


	public Product(ProductDto productDto)
	{
		this.id=productDto.getId();
		this.name=productDto.getName();
		this.price=productDto.getPrice();
		this.inventry=productDto.getInventory();

		if(productDto.getId()==null)
		{
			this.createdDate=LocalDateTime.now();
		}
		this.updatedDate=LocalDateTime.now();


	}

	public void updateInfo(ProductDto productDto) {
		if(productDto.getName()!=null && !productDto.getName().equals("")) {
			this.name=productDto.getName().trim();
		}
		this.price=productDto.getPrice();
		this.updatedDate=LocalDateTime.now();
	}
}