package com.example.productservice.mongodb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.productservice.mongodb.constants.OrderStatus;
import com.example.productservice.mongodb.domain.Order;
import com.example.productservice.mongodb.dto.CustomerOrder;
import com.example.productservice.mongodb.dto.OrderDto;
import com.example.productservice.mongodb.dto.SellerItemsDto;
import com.example.productservice.mongodb.dto.SellerOrder;

public interface OrderRepository {

	Order save(Order order);

	Page<CustomerOrder> getCustomerOrders(String customerId, String search, Pageable pageable);

	Page<SellerOrder> getSellerOrders(String sellerId, String search, Pageable pageable, OrderStatus status);

	Page<SellerItemsDto> getSellerItems(String customerId, String search, Pageable pageable, OrderStatus status);

}
