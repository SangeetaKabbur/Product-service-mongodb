package com.example.productservice.mongodb.exception.errorhandler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.productservice.mongodb.exception.AccessDeniedException;
import com.example.productservice.mongodb.exception.EntityDoesNotExistException;
import com.example.productservice.mongodb.exception.InsufficientDataException;
import com.example.productservice.mongodb.exception.InvalidProductException;
import com.example.productservice.mongodb.exception.InvalidQuantityException;
import com.example.productservice.mongodb.exception.InventoryNotAvailableException;
import com.example.productservice.mongodb.exception.MethodNotAllowedException;
import com.example.productservice.mongodb.exception.ResourseAlreadyExistException;

import lombok.extern.java.Log;

@RestControllerAdvice
@Log
public class ExceptionProcessor extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(EntityDoesNotExistException.class)
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	@ResponseBody
	private ErrorResource gethandlerEntityNotFound(HttpServletRequest req, EntityDoesNotExistException ex) {
		log.info("ExceptionProcessor.gethandlerEntityNotFound()");
		ErrorResource resource = new ErrorResource(ex.getMessage());
		resource.setCode("404");
		return resource;
	}
	
	@ExceptionHandler(InsufficientDataException.class)
	@ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE)
	@ResponseBody
	private ErrorResource gethandlerInsufficientData(HttpServletRequest req, InsufficientDataException ex) {
		log.info("ExceptionProcessor.gethandlerInsufficientData()");
		ErrorResource resource = new ErrorResource(ex.getMessage());
		resource.setCode("406");
		return resource;
	}
	
	@ExceptionHandler(InvalidProductException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ResponseBody
	private ErrorResource gethandlerInvalidProduct(HttpServletRequest req, InvalidProductException ex) {
		log.info("ExceptionProcessor.gethandlerInvalidProduct()");
		ErrorResource resource = new ErrorResource(ex.getMessage());
		resource.setCode("400");
		return resource;
	}
	
	@ExceptionHandler(ResourseAlreadyExistException.class)
	@ResponseStatus(value=HttpStatus.FORBIDDEN)
	@ResponseBody
	private ErrorResource gethandlerResourseAlreadyExist(HttpServletRequest req, ResourseAlreadyExistException ex) {
		log.info("ExceptionProcessor.gethandlerResourseAlreadyExist()");
		ErrorResource resource = new ErrorResource(ex.getMessage());
		resource.setCode("403");
		return resource;
	}
	
	@ExceptionHandler(MethodNotAllowedException.class)
	@ResponseStatus(value=HttpStatus.METHOD_NOT_ALLOWED)
	@ResponseBody
	private ErrorResource gethandlerMethodNotAllowedException(HttpServletRequest req, MethodNotAllowedException ex) {
		log.info("ExceptionProcessor.gethandlerMethodNotAllowedException()");
		ErrorResource resource = new ErrorResource(ex.getMessage());
		resource.setCode("405");
		return resource;
	}
	
	
	@ExceptionHandler(InventoryNotAvailableException.class)
	@ResponseStatus(value=HttpStatus.METHOD_NOT_ALLOWED)
	@ResponseBody
	private ErrorResource gethandlerInventoryNotAvailableException(HttpServletRequest req, InventoryNotAvailableException ex) {
		log.info("ExceptionProcessor. gethandlerInventoryNotAvailableException()");
		ErrorResource resource = new ErrorResource(ex.getMessage());
		resource.setCode("405");
		return resource;
	}

	@ExceptionHandler(InvalidQuantityException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ResponseBody
	private ErrorResource gethandlerInvalidQuantityException(HttpServletRequest req, InvalidQuantityException ex) {
		log.info("ExceptionProcessor.gethandlerInvalidQuantityException()");
		ErrorResource resource = new ErrorResource(ex.getMessage());
		resource.setCode("400");
		return resource;
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ResponseBody
	private ErrorResource gethandlerAccessDeniedException(HttpServletRequest req, AccessDeniedException ex) {
		log.info("ExceptionProcessor.gethandlerAccessDeniedException()");
		ErrorResource resource = new ErrorResource(ex.getMessage());
		resource.setCode("400");
		return resource;
	}

}
