package com.example.productservice.mongodb.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.example.productservice.mongodb.dto.CartDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

	@Id
	private String id;

	private String productId;

	//private int quantity;

	private Map<String,Integer> products=new HashMap<>();

	private String customerId;
	
	private String sellerId;
	
	private String remark;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime createdDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime updatedDate;
	
	public Cart(CartDto cartDto){
		this.id=cartDto.getId();
//		this.productId=cartDto.getProductId();
//		this.quantity=cartDto.getQuantity();
		this.products=cartDto.getProducts();
//		this.customerId=cartDto.getCustomerId();
//		this.sellerId=cartDto.getSellerId();
		this.remark=cartDto.getRemark();
		this.createdDate=LocalDateTime.now();
		this.updatedDate=LocalDateTime.now();
	}
}
