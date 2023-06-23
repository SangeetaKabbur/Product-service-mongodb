package com.example.productservice.mongodb.util;

import java.util.Calendar;
import java.util.Random;

public class Generator  {

	private Generator() {
	}

	public static String generateOrderNumber() {
		int lastTwoDigits = Calendar.getInstance().get(Calendar.YEAR) % 100;
		Random rand = new Random();
		int num = rand.nextInt(8000000) + 7000000;
		return "ORD" + num + "-" + lastTwoDigits;
	}

	public static String generateItemNumber() {
		int lastTwoDigits = Calendar.getInstance().get(Calendar.YEAR) % 100;
		Random rand = new Random();
		int num = rand.nextInt(90000000) + 10000000;
		return "ITM" + num + "-" + lastTwoDigits;
		
	}
}
