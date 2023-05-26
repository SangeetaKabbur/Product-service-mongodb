package com.example.productservice.mongodb.exception;

public class EntityDoesNotExistException extends Exception{
    
    public EntityDoesNotExistException(String message){
        super(message);
    }
}
