package com.example.productservice.mongodb.exception.errorhandler;



import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class ErrorResource {

	private String  occerredOn;
	private String code;
	private String message;

	public ErrorResource(String message) {
		this.message = message;
		this.occerredOn=LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
}