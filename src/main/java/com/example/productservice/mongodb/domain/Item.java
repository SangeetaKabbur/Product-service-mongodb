package com.example.productservice.mongodb.domain;

import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.Document;
import com.example.productservice.mongodb.constants.OrderStatus;
import com.example.productservice.mongodb.dto.ItemDto;
import com.example.productservice.mongodb.dto.SellerItemsDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Item {

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


	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime createdDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime updatedDate;

	public Item(SellerItemsDto sellerItemsDto) {
		this.productId=sellerItemsDto.getProductId();
		this.productName=sellerItemsDto.getProductName();
		this.quantity=sellerItemsDto.getQuantity();
		this.sellerId=sellerItemsDto.getSellerId();
		this.itemNumber=sellerItemsDto.getItemNumber();
		this.orderNumber=sellerItemsDto.getOrderNumber();
		this.itemStatus=sellerItemsDto.getItemStatus();
		this.customerName=sellerItemsDto.getCustomerName();
		this.createdDate=LocalDateTime.now();
		this.updatedDate=LocalDateTime.now();
	}
	
	public Item(ItemDto itemDto) {
		this.id=itemDto.getId();
		this.productId=itemDto.getProductId();
		this.productName=itemDto.getProductName();
		this.quantity=itemDto.getQuantity();
		this.sellerId=itemDto.getSellerId();
		this.itemNumber=itemDto.getItemNumber();
		this.orderNumber=itemDto.getOrderNumber();
		this.itemStatus=itemDto.getItemStatus();
		this.customerId=itemDto.getCustomerId();
		this.customerName=itemDto.getCustomerName();
		this.customerMobile=itemDto.getCustomerMobile();
		this.createdDate=LocalDateTime.now();
		this.updatedDate=LocalDateTime.now();
	}
	
	
}