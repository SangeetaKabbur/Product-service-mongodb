package com.example.productservice.mongodb.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartInfo {
	
    private List<CartItemInfo> cartProducts = new ArrayList<>();

    private int size = 0;
}
