package com.example.productservice.mongodb.domain;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.example.productservice.mongodb.constants.OrderStatus;
import com.example.productservice.mongodb.dto.CustomerInfo;
import com.example.productservice.mongodb.dto.OrderDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Order {
	
	@Id
	private String id;
    
    private String customerId;
    
    private CustomerInfo customer;
    
    private String orderNumber;
    
    private OrderStatus orderStatus;
    
    //private List<Item> items;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime createdDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    private LocalDateTime updatedDate;
    
    public Order(OrderDto orderDto) {
    	this.customerId=orderDto.getCustomerId();
    	this.orderNumber=orderDto.getOrderNumber();
    	//this.items=orderDto.getItems();
    	this.createdDate=LocalDateTime.now();
    	this.updatedDate=LocalDateTime.now();
    }
 
}