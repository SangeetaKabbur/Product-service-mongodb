package com.example.productservice.mongodb.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class OrderDto {

	private String customerId;

	private String orderNumber;

	private String customerName ;

	private long customerMobile ;

	private List<ItemDto> items = new ArrayList<>();
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime createdDate;


	public OrderDto(List<Item> itemsList) {
		if (!itemsList.isEmpty()) {
			Item item = itemsList.get(0);
			this.customerId=item.getCustomerId();
			this.customerName = item.getCustomerName();
			this.customerMobile = item.getCustomerMobile();
			this.orderNumber = item.getOrderNumber();
			this.createdDate = item.getCreatedDate();
			List<ItemDto> itemsDtoList = new ArrayList<>();
			itemsList.forEach(itemInList -> itemsDtoList.add(new ItemDto(itemInList)));
			this.items = itemsDtoList;
		}
	}
}
