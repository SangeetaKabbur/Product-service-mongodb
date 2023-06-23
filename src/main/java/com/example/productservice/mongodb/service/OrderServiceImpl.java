package com.example.productservice.mongodb.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.productservice.mongodb.constants.OrderStatus;
import com.example.productservice.mongodb.constants.Role;
import com.example.productservice.mongodb.domain.Item;
import com.example.productservice.mongodb.domain.Order;
import com.example.productservice.mongodb.domain.Product;
import com.example.productservice.mongodb.domain.User;
import com.example.productservice.mongodb.dto.CartInfo;
import com.example.productservice.mongodb.dto.CartItemInfo;
import com.example.productservice.mongodb.dto.CustomerInfo;
import com.example.productservice.mongodb.dto.CustomerOrder;
import com.example.productservice.mongodb.dto.InfoValue;
import com.example.productservice.mongodb.dto.ItemDto;
import com.example.productservice.mongodb.dto.OrderDetailsDto;
import com.example.productservice.mongodb.dto.OrderDto;
import com.example.productservice.mongodb.dto.SellerItemsDto;
import com.example.productservice.mongodb.dto.SellerOrder;
import com.example.productservice.mongodb.exception.AccessDeniedException;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InventoryNotAvailableException;
import com.example.productservice.mongodb.exception.MethodNotAllowedException;
import com.example.productservice.mongodb.repository.ItemRepository;
import com.example.productservice.mongodb.repository.OrderRepository;
import com.example.productservice.mongodb.repository.ProductRepository;
import com.example.productservice.mongodb.repository.UserRepository;
import com.example.productservice.mongodb.util.Generator;
import lombok.extern.java.Log;

@Service
@Log
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	private final CartService cartService;

	private final ProductService productService;

	private final ProductRepository productRepository;

	private final UserRepository userRepository;

	private final ItemRepository itemRepository;

	public OrderServiceImpl(OrderRepository orderRepository, CartService cartService, ProductService productService,
			UserRepository userRepository,ProductRepository productRepository,ItemRepository itemRepository ) {
		this.orderRepository=orderRepository;
		this.cartService=cartService;
		this.productService=productService;
		this.userRepository=userRepository;
		this.productRepository=productRepository;
		this.itemRepository=itemRepository;
	}

	@Override
	public String placeOrder(String userId) throws EntityDoesNotExistException, AccessDeniedException, InventoryNotAvailableException {
		log.info("OrderServiceImpl.placeOrder()");
		CartInfo cartInfo = cartService.getCartByUserId(userId);
		User user = userRepository.findById(userId);
		if (user.getRole() != Role.CUSTOMER) {
			throw new AccessDeniedException("Access Denied");
		}

		CustomerInfo customerInfo = new CustomerInfo(user.getName(), user.getMobileNumber(), user.getEmail());

		Order order = new Order();
		order.setCustomerId(userId);
		order.setCustomer(customerInfo);
		order.setOrderNumber(Generator.generateOrderNumber());
		order.setOrderStatus(OrderStatus.NEW_ORDER);
		order.setCreatedDate(LocalDateTime.now());
		order.setUpdatedDate(LocalDateTime.now());

		List<Item> items = convertToItems(cartInfo.getCartProducts(), order.getOrderNumber(),customerInfo,userId);
		reduceInventory(items);

		Order savedOrder = orderRepository.save(order);
		itemRepository.saveAll(items, Item.class);

		// Clear the cart after placing the order
		cartService.clearCart(userId);

		OrderDetailsDto orderDetailsDto = new OrderDetailsDto(savedOrder, items);

		return orderDetailsDto.getOrder().getOrderNumber();
	}

	private List<Item> convertToItems(List<CartItemInfo> cartProducts, String orderNumber,CustomerInfo customerInfo,String userId) 
			throws EntityDoesNotExistException, InventoryNotAvailableException {
		List<Item> items = new ArrayList<>();

		for (CartItemInfo cartProduct : cartProducts) {
			Item item = createItem(cartProduct, orderNumber,customerInfo,userId);
			items.add(item);
		}

		return items;
	}

	private Item createItem(CartItemInfo cartProduct, String orderNumber,CustomerInfo customerInfo,String userId) throws EntityDoesNotExistException, InventoryNotAvailableException {
		Product product = productService.getProductById(cartProduct.getProductId());

		Item item = new Item();
		item.setProductId(product.getId());
		item.setProductName(product.getName());
		item.setQuantity(cartProduct.getQuantity());
		item.setSellerId(productRepository.getSellerIdByProductId(product.getId()));
		item.setItemNumber(Generator.generateItemNumber());
		item.setItemStatus(OrderStatus.NEW_ORDER);
		item.setCreatedDate(LocalDateTime.now());
		item.setUpdatedDate(LocalDateTime.now());
		item.setOrderNumber(orderNumber);
		item.setCustomerId(userId);
		item.setCustomerName(customerInfo.getName());
		item.setCustomerMobile(customerInfo.getMobileNumber());

		return item;
	}

	private void reduceInventory(List<Item> items) throws EntityDoesNotExistException, InventoryNotAvailableException {

		Map<String, Integer> originalInventoryMap = new HashMap<>();

		for (Item item : items) {
			Product product = productService.getProductById(item.getProductId());
			int currentInventory = product.getInventry();
			int orderedQuantity = item.getQuantity();

			if (currentInventory < orderedQuantity) {
				updateOriginalInventory(originalInventoryMap);
				throw new InventoryNotAvailableException("Insufficient inventory for product: " + product.getName());
			}

			// Store original inventory
			originalInventoryMap.put(product.getId(), currentInventory);

			// Update inventory
			int updatedInventory = currentInventory - orderedQuantity;
			product.setInventry(updatedInventory);
			productRepository.save(product);
		}
	}

	private void updateOriginalInventory(Map<String, Integer> originalInventoryMap) throws EntityDoesNotExistException {
		for (Map.Entry<String, Integer> entry : originalInventoryMap.entrySet()) {
			String productId = entry.getKey();
			int originalInventory = entry.getValue();
			Product product = productService.getProductById(productId);
			product.setInventry(originalInventory);
			productRepository.save(product);
		}
	}

	@Override
	public Page<CustomerOrder> getCustomerOrders(String customerId, String search, Pageable pageable) {
		log.info("OrderServiceImpl.getCustomerOrders()");
		return orderRepository.getCustomerOrders(customerId,search,pageable);
	}

	@Override
	public Page<SellerOrder> getSellerOrders(String sellerId, String search, Pageable pageable, OrderStatus status) {
		log.info("OrderServiceImpl.getSellerOrders()");
		return orderRepository.getSellerOrders(sellerId,search,pageable,status);
	}

	@Override
	public Page<SellerItemsDto> getSellerItems(String customerId, String search, Pageable pageable, OrderStatus status) {
		log.info("OrderServiceImpl.getSellerItems()");
		return orderRepository.getSellerItems(customerId,search,pageable,status);
	}

	@Override
	public OrderDto updateItemStatus(String sellerId, List<String> itemNumbers, OrderStatus status) 
			throws MethodNotAllowedException, EntityDoesNotExistException {
		log.info("OrderServiceImpl.updateItemStatus()");

		List<Item> items = itemRepository.getSellerDocuments(itemNumbers, sellerId);
		if (items.size() != itemNumbers.size()) {
			throw new MethodNotAllowedException("Some items do not belong to you");
		}
		// Update the status of each item
		for (Item item : items) {
			item.setItemStatus(status);
			itemRepository.save(item);
		}

		if (OrderStatus.CANCELLED.equals(status) || OrderStatus.REJECTED.equals(status)) {
			updateInventoryForProducts(items);
		}

		if (OrderStatus.NEW_ORDER.equals(status)) {
			processOrder(items);
		}

		return new OrderDto(items);
	}

	private void updateInventoryForProducts(List<Item> items) throws EntityDoesNotExistException {

		for (Item item : items) {
			Product product = productService.getProductById(item.getProductId());
			int currentInventory = product.getInventry();
			int orderedQuantity = item.getQuantity();

			// Update inventory
			int updatedInventory = currentInventory+orderedQuantity;
			product.setInventry(updatedInventory);
			productRepository.save(product);
		}
	}

	private void processOrder(List<Item> items) throws EntityDoesNotExistException {
		for (Item item : items) {
			Product product = productService.getProductById(item.getProductId());
			if (product.getInventry() >= item.getQuantity()) {
				item.setItemStatus(OrderStatus.COMPLETED);
				itemRepository.save(item);
			} else {
				item.setItemStatus(OrderStatus.REJECTED);
				itemRepository.save(item);
			}
		}
	}
} 