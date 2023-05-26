package com.example.productservice.mongodb.exception;


public class InsufficientDataException extends Exception{


	public InsufficientDataException(){}

	public InsufficientDataException(String message){
		super(message);
	}
}

