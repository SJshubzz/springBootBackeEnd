package net.javaguide.springboot.utills;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Utils {
	private Utils() {
		
	}
	public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpstatus){
		return new ResponseEntity<String>( "{\"message\":\"" +responseMessage+ "\"}" , httpstatus);
	}

}
