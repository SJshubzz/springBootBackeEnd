package net.javaguide.springboot.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.javaguide.springboot.POJO.category;

@RequestMapping(path = "/category")
public interface CategoryRest {
	@PostMapping(path = "/add")
	ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String, String> requestMap);
	
	@GetMapping(path = "/get")
	ResponseEntity<List<category>> getAllCategories(@RequestParam(required = false) String filterValue);
	
	@PostMapping(path = "/update")
	ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String, String> requestMap);

}
