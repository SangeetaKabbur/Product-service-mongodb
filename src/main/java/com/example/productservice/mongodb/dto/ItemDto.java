package com.example.productservice.mongodb.dto;

import com.example.productservice.mongodb.constants.OrderStatus;
import com.example.productservice.mongodb.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

	private String id;

	private String productId;

	private String productName; 

	private int quantity;

	private String sellerId;

	private String itemNumber;

	private String orderNumber;

	private OrderStatus itemStatus;

	private String customerId;

	private String customerName ;

	private long customerMobile;

	public ItemDto(Item item) {
		this.id=item.getId();
		this.productId=item.getProductId();
		this.productName=item.getProductName();
		this.quantity=item.getQuantity();
		this.sellerId=item.getSellerId();
		this.itemNumber=item.getItemNumber();
		this.orderNumber=item.getOrderNumber();
		this.itemStatus=item.getItemStatus();
		this.customerId=item.getCustomerId();
		this.customerName=item.getCustomerName();
		this.customerName=item.getCustomerName();
	}

}
