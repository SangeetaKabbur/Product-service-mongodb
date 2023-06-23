package com.example.productservice.mongodb.controller;

import javax.validation.constraints.Min;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.productservice.mongodb.constants.Status;
import com.example.productservice.mongodb.dto.CartDto;
import com.example.productservice.mongodb.dto.CartInfo;
import com.example.productservice.mongodb.dto.CustomPagination;
import com.example.productservice.mongodb.dto.ProductDto;
import com.example.productservice.mongodb.exception.AccessDeniedException;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InsufficientDataException;
import com.example.productservice.mongodb.exception.InvalidQuantityException;
import com.example.productservice.mongodb.exception.InventoryNotAvailableException;
import com.example.productservice.mongodb.exception.MethodNotAllowedException;
import com.example.productservice.mongodb.service.CartService;
import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/cart")
public class CartController {

	private final CartService cartService ;
	public  CartController(CartService cartService) {
		this.cartService=cartService;
	}

	@PostMapping("/add/{productId}/{userId}")
	public ResponseEntity<CartDto> addToCart(@PathVariable(value = "productId") String productId,
			@PathVariable(value = "userId") String userId,
			@Min(value = 0, message = "The value must be greater than 0") @RequestParam int quantity,
			@RequestParam(required = false) String remark)
					throws EntityDoesNotExistException, MethodNotAllowedException, InvalidQuantityException,
					InventoryNotAvailableException, AccessDeniedException{
		log.info("CartController.addToCart()");
		return ResponseEntity.ok().body(cartService.addToCart(productId,userId,quantity,remark));
	}

	@GetMapping("/fetch")
	public ResponseEntity<CartInfo> getCartByUserId(@RequestParam String userId) 
			throws EntityDoesNotExistException, AccessDeniedException{
		log.info("CartController.getCartByUserId()");
		return new ResponseEntity<>(cartService.getCartByUserId(userId), HttpStatus.OK);
	}
	

	@GetMapping("/page/list")
	public CustomPagination getProductList(@PageableDefault(value = 6) Pageable pageable,
			@RequestParam String userId ,PagedResourcesAssembler pagedResourcesAssembler) throws EntityDoesNotExistException, AccessDeniedException{
		log.info("CartController.getProductList()");
		Page<CartInfo> cartInfo =cartService.getCartList(pageable,userId);
		PagedModel<CartInfo> pageResult = pagedResourcesAssembler.toModel(cartInfo);
		return new CustomPagination(pageResult.getContent(), cartInfo.getSize(), cartInfo.getTotalElements(),
				cartInfo.getNumber(), cartInfo.getTotalPages(), pageResult.getLinks());
	}
	
	@DeleteMapping("/product/remove/{productId}")
	public String removeProductFromCart(@PathVariable(value = "productId") String productId,@RequestParam String userId)
			throws EntityDoesNotExistException, AccessDeniedException{
		log.info("CartController.removeProductFromCart()");
		return cartService.removeProductFromCart(productId,userId);
	}

	@PutMapping("/update")
	public String updateCart(@RequestParam String productId, @RequestParam String userId, 
			@RequestParam int quantity) throws EntityDoesNotExistException, MethodNotAllowedException, InvalidQuantityException, InventoryNotAvailableException, AccessDeniedException{
		log.info("CartController.updateCart()");
		return cartService.updateProductInCart(productId,userId,quantity);
	}

	@DeleteMapping("clear-cart")
	public ResponseEntity<String> removeCart(@RequestParam String userId) throws EntityDoesNotExistException, AccessDeniedException {
		log.info("CartController.removeCart()");
		return new ResponseEntity<>(cartService.clearCart(userId), HttpStatus.OK);
	}

}