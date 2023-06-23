package com.example.productservice.mongodb.exception;

public class InventoryNotAvailableException extends Exception {
	
	public InventoryNotAvailableException(){}

	public InventoryNotAvailableException(String message){
		super(message);
	}
}
