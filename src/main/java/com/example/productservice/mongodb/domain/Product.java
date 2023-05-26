package com.example.productservice.mongodb.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Document
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	private String id;
	
	private String name;
	
	private BigDecimal price;
	
	private long inventry;
	
	private Status status;

	private LocalDateTime createdDate;
	
	private LocalDateTime updatedDate;
	

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Product(ProductDto productDto)
	{
		this.id=productDto.getId();
		this.name=productDto.getName();
		this.price=productDto.getPrice();
		this.inventry=productDto.getInventry();
		
		if(productDto.getId()==null)
		{
			this.createdDate=LocalDateTime.now();
		}
		this.updatedDate=LocalDateTime.now();
		
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public long getInventry() {
		return inventry;
	}
	public void setInventry(long inventry) {
		this.inventry = inventry;
	}
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price + ", inventry=" + inventry + ", status="
				+ status + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + "]";
	}

	public void updateInfo(ProductDto productDto) {
		if(productDto.getName()!=null && !productDto.getName().equals("")) {
			this.name=productDto.getName().trim();
		}
		this.price=productDto.getPrice();
		this.updatedDate=LocalDateTime.now();
	}

}
