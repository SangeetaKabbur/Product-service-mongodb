package com.example.productservice.mongodb.dto;

import java.time.LocalDateTime;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.example.productservice.mongodb.constants.OrderStatus;
import com.example.productservice.mongodb.domain.Item;
import com.example.productservice.mongodb.domain.Order;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrder {

	private String orderNumber;

	private String productId;

	private String productName; 

	private int quantity;

	private String sellerId;

	private String itemNumber;

	private OrderStatus itemStatus;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime createdDate;

	public CustomerOrder(Order order) {
		this.orderNumber=order.getOrderNumber();
	}

	public CustomerOrder(Item item) {
		this.productId=item.getProductId();
		this.productName=item.getProductName();
		this.quantity=item.getQuantity();
		this.sellerId=item.getSellerId();
		this.itemNumber=item.getItemNumber();
		this.itemStatus=item.getItemStatus();
		this.createdDate=item.getCreatedDate();

	}
}