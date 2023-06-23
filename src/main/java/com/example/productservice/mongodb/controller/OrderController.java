package com.example.productservice.mongodb.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.productservice.mongodb.constants.OrderStatus;
import com.example.productservice.mongodb.dto.CustomPagination;
import com.example.productservice.mongodb.dto.CustomerOrder;
import com.example.productservice.mongodb.dto.InfoValue;
import com.example.productservice.mongodb.dto.OrderDto;
import com.example.productservice.mongodb.dto.SellerItemsDto;
import com.example.productservice.mongodb.dto.SellerOrder;
import com.example.productservice.mongodb.exception.AccessDeniedException;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InventoryNotAvailableException;
import com.example.productservice.mongodb.exception.MethodNotAllowedException;
import com.example.productservice.mongodb.service.OrderService;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/order")
@Log
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService=orderService;
	}

	@PostMapping("/place-order")
	public ResponseEntity<String> placeOrder(@RequestParam String userId) 
			throws EntityDoesNotExistException, AccessDeniedException, InventoryNotAvailableException{
		log.info("OrderController.placeOrder()");
		return ResponseEntity.ok().body(orderService.placeOrder(userId));
	}
	
	@GetMapping("/customer/get")
	public ResponseEntity<CustomPagination> getCustomerOrders(@RequestParam String customerId,
			@RequestParam(required = false) String search, @PageableDefault(value = 10) Pageable pageable,
			PagedResourcesAssembler pageAssembler) {
		log.info("OrderController.getCustomerOrders()");
		Page<CustomerOrder> customerOrder = orderService.getCustomerOrders(customerId,search,pageable);
		PagedModel<CustomerOrder> pageCustomerOrder = pageAssembler.toModel(customerOrder);
		CustomPagination pagination = new CustomPagination(pageCustomerOrder.getContent(), customerOrder.getSize(),
				customerOrder.getTotalElements(), customerOrder.getNumber(),
				customerOrder.getTotalPages(), pageCustomerOrder.getLinks());
		return new ResponseEntity<>(pagination, HttpStatus.OK);
	}
	
	@GetMapping("/seller/get")
	public ResponseEntity<CustomPagination> getSellerOrders(@RequestParam String sellerId,
			@RequestParam(required = false) String search, @PageableDefault(value = 10) Pageable pageable,
			@RequestParam(required = false) OrderStatus status,PagedResourcesAssembler pageAssembler){
		log.info("OrderController.getSellerOrders()");
		Page<SellerOrder> sellerOrder=orderService.getSellerOrders(sellerId,search,pageable,status);
		PagedModel<SellerOrder> pageSellerOrder=pageAssembler.toModel(sellerOrder);
		CustomPagination pagination=new CustomPagination(pageSellerOrder.getContent(),sellerOrder.getSize(),
				sellerOrder.getTotalElements(),sellerOrder.getNumber(),
				sellerOrder.getTotalPages(),pageSellerOrder.getLinks());
		return new ResponseEntity<>(pagination,HttpStatus.OK);
	}
	
	@GetMapping("/items/get")
	public ResponseEntity<CustomPagination> getSellerItems(@RequestParam String customerId, 
			@RequestParam(required = false) String search, @PageableDefault(value = 10) Pageable pageable,
			@RequestParam(required = false) OrderStatus status,PagedResourcesAssembler pageAssembler){
		log.info("OrderController.getItems()");
		Page<SellerItemsDto> itemDto=orderService.getSellerItems(customerId,search,pageable,status);
		PagedModel<SellerItemsDto> pageItemDto=pageAssembler.toModel(itemDto);
		CustomPagination pagination=new CustomPagination(pageItemDto.getContent(),itemDto.getSize(),
				itemDto.getTotalElements(),itemDto.getNumber(),
				itemDto.getTotalPages(),pageItemDto.getLinks());
		return new ResponseEntity<>(pagination,HttpStatus.OK);
	}
	
	@PutMapping("/update-item-status")
	public ResponseEntity<OrderDto> updateItemStatus(@RequestParam String sellerId, @RequestBody InfoValue itemNumbers,
			@RequestParam OrderStatus status) 
					throws MethodNotAllowedException, EntityDoesNotExistException{
		log.info("OrderController.updateItemStatus()");
		return ResponseEntity.ok().body(orderService.updateItemStatus(sellerId,itemNumbers.getValues(), status));	
	}
}
