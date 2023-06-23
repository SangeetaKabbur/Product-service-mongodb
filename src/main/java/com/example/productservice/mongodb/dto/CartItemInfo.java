package com.example.productservice.mongodb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemInfo {
	
	private String productId;
	
    private String productName;
    
    private int quantity;
    
  
}
