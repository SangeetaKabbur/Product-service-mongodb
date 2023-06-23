package com.example.productservice.mongodb.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.productservice.mongodb.constants.Role;
import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.domain.Cart;
import com.example.productservice.mongodb.domain.Product;
import com.example.productservice.mongodb.domain.User;
import com.example.productservice.mongodb.dto.CartDto;
import com.example.productservice.mongodb.dto.CartInfo;
import com.example.productservice.mongodb.exception.AccessDeniedException;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InvalidQuantityException;
import com.example.productservice.mongodb.exception.InventoryNotAvailableException;
import com.example.productservice.mongodb.exception.MethodNotAllowedException;
import com.example.productservice.mongodb.repository.CartRepository;
import com.example.productservice.mongodb.repository.ProductRepository;
import com.example.productservice.mongodb.repository.UserRepository;
import lombok.extern.java.Log;

@Service
@Log
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;

	public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}

	@Override
	public CartDto addToCart(String productId,String userId,int quantity,String remarks) 
			throws EntityDoesNotExistException, MethodNotAllowedException, InvalidQuantityException, InventoryNotAvailableException, AccessDeniedException {
		log.info("CartServiceImpl.addToCart()");
		 String sellerId = productRepository.getSellerIdByProductId(productId);
		verifyProductBeforeAddingToCart(quantity, productId,userId);

		Cart cart = cartRepository.findByUserId(userId);
		if (cart == null) {
			cart = setCart(productId, userId, sellerId, quantity,remarks);
			cartRepository.save(cart);
		} else {
			updateCart(productId,userId,sellerId, quantity, cart,remarks);
		}
		return new CartDto(cart);
	}

	private void verifyProductBeforeAddingToCart(int quantity,String productId,String userId) 
			throws EntityDoesNotExistException, MethodNotAllowedException, InvalidQuantityException, InventoryNotAvailableException, AccessDeniedException {
		Product product = productRepository.findByKeyNameAndValue("_id", productId);
		if (product == null) {
			throw new EntityDoesNotExistException("Product does not exist");
		}
		User user=userRepository.findById(userId);
		if(user== null) {
			throw new EntityDoesNotExistException("user not found with id "+userId);
		}
		if (product.getStatus() != Status.ACTIVE) {
			throw new MethodNotAllowedException("Cannot add product to cart. Product is not active");
		}
		if (quantity <= 0) {
			throw new InvalidQuantityException("Invalid quantity. Quantity must be greater than zero");
		}
		if (product.getInventry() < quantity) {
			throw new InventoryNotAvailableException("Insufficient stock. Product is out of stock");
		}
		if (user.getRole() != Role.CUSTOMER) {
			throw new AccessDeniedException("Access Denied");
		}
	}
	//Create a new cart
	private Cart setCart(String productId,String userId, String sellerId, int quantity,String remarks) {
		Map<String, Integer> sellerProducts = new HashMap<>();
		sellerProducts.put(productId, quantity);
		Cart cart = new Cart();
		cart.setProducts(sellerProducts);
		cart.setCustomerId(userId);
		cart.setSellerId(sellerId);
		cart.setRemark(remarks);
		cart.setCreatedDate(LocalDateTime.now());
		cart.setUpdatedDate(LocalDateTime.now());
		return cart;
	}

	// Update existing cart
	private void updateCart(String productId,String userId,String sellerId, int quantity, Cart cart,String remarks) {
		cart.getProducts().put(productId, quantity);
		cart.setUpdatedDate(LocalDateTime.now());
		cart.setCustomerId(userId);
		cart.setSellerId(sellerId);
		cart.setRemark(remarks);
		cartRepository.save(cart);
	}

	@Override
	public CartInfo getCartByUserId(String userId) throws EntityDoesNotExistException, AccessDeniedException  {
		log.info("CartServiceImpl.getCartByUserId()");
		User user=userRepository.findById(userId);
		if(user== null) {
			throw new EntityDoesNotExistException("user not found with id "+userId);
		}
		if (user.getRole() != Role.CUSTOMER) {
			throw new AccessDeniedException("Access Denied");
		}
		return cartRepository.getCartByUserId(userId);
	}
	//		Cart cart = cartRepository.findByUserId(userId);
	//		if (cart != null) {
	//			List<CartItemInfo> cartItems = new ArrayList<>();
	//			Map<String, Integer> products = cart.getProducts();
	//
	//			for (Map.Entry<String, Integer> entry : products.entrySet()) {
	//				String productId = entry.getKey();
	//				int quantity = entry.getValue();
	//				Product product = productRepository.findById(productId);
	//
	//				if (product != null) {
	//					CartItemInfo cartItem = new CartItemInfo();
	//					cartItem.setProductId(productId);
	//					cartItem.setProductName(product.getName());
	//					cartItem.setQuantity(quantity);
	//					cartItems.add(cartItem);
	//				}
	//			}
	//
	//			CartInfo cartInfo = new CartInfo();
	//			cartInfo.setCartProducts(cartItems);
	//			cartInfo.setSize(cartItems.size());
	//			return cartInfo;
	//		}
	//		return new CartInfo();

	@Override
	public String removeProductFromCart(String productId,String userId)
			throws EntityDoesNotExistException, AccessDeniedException{
		log.info("CartServiceImpl.removeProductFromCart()");
		Product product = productRepository.findByKeyNameAndValue("_id", productId);
		if (product == null) {
			throw new EntityDoesNotExistException("Product does not exist");
		}
		User user=userRepository.findById(userId);
		if(user== null) {
			throw new EntityDoesNotExistException("user not found with id "+userId);
		}
		if (user.getRole() != Role.CUSTOMER) {
			throw new AccessDeniedException("Access Denied");
		}
		Cart cart = cartRepository.findByUserId(userId);
		if (cart == null) {
			throw new EntityDoesNotExistException("Cart not found for user with ID: " + userId);
		}

		// Remove the product from the cart
		if (cart.getProducts().containsKey(productId)) {
			cart.getProducts().remove(productId);
		} else {
			throw new EntityDoesNotExistException("Product not found in the cart: " + productId);
		}
		// Save the updated cart
		cart.setUpdatedDate(LocalDateTime.now());
		cartRepository.save(cart);
		return "Product removed from cart successfully";
	}

	@Override
	public String updateProductInCart(String productId, String userId, int quantity) 
			throws EntityDoesNotExistException, MethodNotAllowedException, InvalidQuantityException,
			InventoryNotAvailableException, AccessDeniedException {
		log.info("CartServiceImpl.updateProductInCart()");

		verifyProductBeforeAddingToCart(quantity, productId,userId);
		Cart cart = cartRepository.findByUserId(userId);
		if (cart == null) {
			throw new EntityDoesNotExistException("Cart not found for user with ID: " + userId);
		}

		// Update the quantity of the specified product in the cart
		if (cart.getProducts().containsKey(productId)) {
			cart.getProducts().put(productId, quantity);
		} else {
			throw new EntityDoesNotExistException("Product not found in the cart: " + productId);
		}

		// Save the updated cart
		cart.setUpdatedDate(LocalDateTime.now());
		cartRepository.save(cart);
		return "Product updated in cart successfully";
	}

	@Override
	public String clearCart(String userId) throws EntityDoesNotExistException, AccessDeniedException {
		log.info("CartServiceImpl.clearCart()");
		User user=userRepository.findById(userId);
		if(user== null) {
			throw new EntityDoesNotExistException("user not found with id "+userId);
		}
		if (user.getRole() != Role.CUSTOMER) {
			throw new AccessDeniedException("Access Denied");
		}
		Cart cart = cartRepository.findByUserId(userId);
		if (cart == null) {
			throw new EntityDoesNotExistException("Cart not found for user with ID: " + userId);
		}
		cart.getProducts().clear();
		cartRepository.save(cart);

		return "Success";
	}

	@Override
	public Page<CartInfo> getCartList( Pageable pageable, String userId) 
			throws EntityDoesNotExistException, AccessDeniedException {
		log.info("CartServiceImpl.getCartList()");
		User user=userRepository.findById(userId);
		if(user== null) {
			throw new EntityDoesNotExistException("user not found with id "+userId);
		}
		if (user.getRole() != Role.CUSTOMER) {
			throw new AccessDeniedException("Access Denied");
		}
		return cartRepository.getCartList(pageable,userId);
	}
}