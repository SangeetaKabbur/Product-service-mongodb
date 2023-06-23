package com.example.productservice.mongodb.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.productservice.mongodb.constants.OrderStatus;
import com.example.productservice.mongodb.dto.CustomerOrder;
import com.example.productservice.mongodb.dto.InfoValue;
import com.example.productservice.mongodb.dto.ItemDto;
import com.example.productservice.mongodb.dto.OrderDto;
import com.example.productservice.mongodb.dto.SellerItemsDto;
import com.example.productservice.mongodb.dto.SellerOrder;
import com.example.productservice.mongodb.exception.AccessDeniedException;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InventoryNotAvailableException;
import com.example.productservice.mongodb.exception.MethodNotAllowedException;

public interface OrderService {

	String placeOrder(String userId) throws EntityDoesNotExistException, AccessDeniedException, InventoryNotAvailableException;

	Page<CustomerOrder> getCustomerOrders(String customerId, String search, Pageable pageable);

	Page<SellerOrder> getSellerOrders(String sellerId, String search, Pageable pageable, OrderStatus status);

	Page<SellerItemsDto> getSellerItems(String customerId, String search, Pageable pageable, OrderStatus status);

	public OrderDto updateItemStatus(String sellerId, List<String> itemNumbers, OrderStatus status) 
			throws MethodNotAllowedException, EntityDoesNotExistException;
}
