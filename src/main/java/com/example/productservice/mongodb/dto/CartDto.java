package com.example.productservice.mongodb.dto;


import java.util.HashMap;
import java.util.Map;

import com.example.productservice.mongodb.domain.Cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartDto  {
	
	private String id;

//	private String productId;
//
//	private Double quantity;

	private Map<String,Integer> products=new HashMap<>();

	private String remark;
	
	private String customerId;
	
	private String sellerId;
	
	public CartDto(Cart cart){
		this.id=cart.getId();
//		this.productId=cart.getProductId();
//		this.quantity=cart.getQuantity();
		this.products=cart.getProducts();
		this.remark=cart.getRemark();
		this.customerId=cart.getCustomerId();
		this.sellerId=cart.getSellerId();
	}
}
