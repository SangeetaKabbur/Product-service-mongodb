package com.example.productservice.mongodb.validation;

import java.security.SecureRandom;
import java.util.Calendar;

public class Generator {
    
    private Generator(){}

    public static String generateOrderNumber(){
        int lastTwoDigits = Calendar.getInstance().get(Calendar.YEAR) % 100;
		SecureRandom  rand = new SecureRandom();
		int num = rand.nextInt(8000000) + 7000000;
		return  "ORD"+num+"-"+lastTwoDigits;
    }
}
