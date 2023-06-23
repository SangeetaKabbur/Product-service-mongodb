package com.example.productservice.mongodb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.productservice.mongodb.domain.Cart;
import com.example.productservice.mongodb.dto.CartInfo;


public interface CartRepository {

	Cart findByUserId(String userId);

	void save(Cart cart);

	CartInfo getCartByUserId(String userId);

	Page<CartInfo> getCartList(Pageable pageable, String userId);

}
