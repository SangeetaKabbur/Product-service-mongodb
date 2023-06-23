package com.example.productservice.mongodb.exception;

public class InvalidQuantityException extends Exception {

	public InvalidQuantityException(){}

	public InvalidQuantityException(String message){
		super(message);
	}
}
