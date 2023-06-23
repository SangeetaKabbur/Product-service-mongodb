package com.example.productservice.mongodb.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerOrder {

	private String customerId;
	
	private String customerName;

	private long customerMobile;

	private int numberOfItems;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	private LocalDateTime createdDate;

}

