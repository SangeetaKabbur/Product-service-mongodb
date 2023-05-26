package com.example.productservice.mongodb.dto;

import java.math.BigDecimal;

import com.example.productservice.mongodb.domain.Product;

public class ProductDto {
	private String id;
	private String name;
	private BigDecimal price;
	private long inventory;



	public ProductDto(String id, String name, BigDecimal price, long inventry) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.inventory = inventry;
	}

	public ProductDto() {

	}

	public ProductDto(Product product)
	{
		this.id=product.getId();
		this.name=product.getName();
		this.price=product.getPrice();
		this.inventory=product.getInventry();


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
		return inventory;
	}
	public void setInventry(long inventry) {
		this.inventory = inventry;
	}

	@Override
	public String toString() {
		return "ProductDto [id=" + id + ", name=" + name + ", price=" + price + ", inventry=" + inventory + "]";
	}




}
