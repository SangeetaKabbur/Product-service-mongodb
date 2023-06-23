package com.example.productservice.mongodb.exception;

public class ResourseAlreadyExistException extends Exception{

	public ResourseAlreadyExistException() {
		
	}
	public ResourseAlreadyExistException(String message)
	{
		super(message);
	}
}
