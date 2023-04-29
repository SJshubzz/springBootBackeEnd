package net.javaguide.springboot.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import net.javaguide.springboot.POJO.category;
import net.javaguide.springboot.constents.Constance;
import net.javaguide.springboot.rest.CategoryRest;
import net.javaguide.springboot.service.CategoryService;
import net.javaguide.springboot.utills.Utils;


@RestController
public class CategoryRestImpl implements CategoryRest {
	
	@Autowired
	CategoryService categoryService;

	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
		try {
			return categoryService.addNewCategory(requestMap);
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<category>> getAllCategories(String filterValue) {
		try {
			return categoryService.getAllCategory(filterValue);
		} catch (Exception e) {
			e.printStackTrace();
			
			// TODO: handle exception
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		try {
			return categoryService.updateCategory(requestMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
