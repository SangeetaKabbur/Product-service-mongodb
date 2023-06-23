package com.example.productservice.mongodb.dto;

import java.util.ArrayList;
import java.util.List;
import com.example.productservice.mongodb.domain.Item;
import com.example.productservice.mongodb.domain.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDto {

	private Order order;

	private List<Item> items=new ArrayList<>();

}