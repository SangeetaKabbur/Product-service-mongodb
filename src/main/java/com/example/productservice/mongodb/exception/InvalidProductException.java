package com.example.productservice.mongodb.exception;

public class InvalidProductException extends Exception{
    
    public InvalidProductException(){}

    public InvalidProductException(String message){
        super(message);
    }
}
