package com.example.productservice.mongodb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.productservice.mongodb.dto.CartDto;
import com.example.productservice.mongodb.dto.CartInfo;
import com.example.productservice.mongodb.exception.AccessDeniedException;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InvalidQuantityException;
import com.example.productservice.mongodb.exception.InventoryNotAvailableException;
import com.example.productservice.mongodb.exception.MethodNotAllowedException;

public interface CartService {

	CartDto addToCart (String productId,String userId,int quantity, String remarks) 
			throws EntityDoesNotExistException, MethodNotAllowedException, InvalidQuantityException, InventoryNotAvailableException, AccessDeniedException;

	CartInfo getCartByUserId(String userId) throws EntityDoesNotExistException, AccessDeniedException;

	String removeProductFromCart(String productId,String userId) throws EntityDoesNotExistException, AccessDeniedException;

	String updateProductInCart(String productId, String userId, int quantity) throws EntityDoesNotExistException, MethodNotAllowedException, InvalidQuantityException, InventoryNotAvailableException, AccessDeniedException;

	String clearCart(String userId) throws EntityDoesNotExistException, AccessDeniedException;

    Page<CartInfo> getCartList(Pageable pageable, String userId) throws EntityDoesNotExistException, AccessDeniedException;
}
