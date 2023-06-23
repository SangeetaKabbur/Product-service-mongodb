package com.example.productservice.mongodb.exception;

public class AccessDeniedException extends Exception {

	public AccessDeniedException(){}

	public AccessDeniedException(String message){
		super(message);
	}
}
