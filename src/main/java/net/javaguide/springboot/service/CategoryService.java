package net.javaguide.springboot.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import net.javaguide.springboot.POJO.category;

public interface CategoryService {
	
	ResponseEntity<String> addNewCategory(Map<String, String> requestMap);
	
	ResponseEntity<List<category>>getAllCategory(String filterValue);

	ResponseEntity<String> updateCategory(Map<String, String> requestMap);

}
