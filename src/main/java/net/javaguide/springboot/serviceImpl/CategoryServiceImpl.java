package net.javaguide.springboot.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import ch.qos.logback.classic.pattern.Util;
import lombok.extern.slf4j.Slf4j;
import net.javaguide.springboot.POJO.category;
import net.javaguide.springboot.constents.Constance;
import net.javaguide.springboot.dao.CategoryDao;
import net.javaguide.springboot.jwt.jwtFilter;
import net.javaguide.springboot.service.CategoryService;
import net.javaguide.springboot.utills.Utils;


@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

	
	@Autowired
	CategoryDao categoryDao;
	
	@Autowired
	jwtFilter jwtFilter;
	
	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
		try {
			if(jwtFilter.isAdmin()) {
				if (ValidateCategoryMap(requestMap, false)) {
					categoryDao.save(getCaregoryfromMap(requestMap, false));
					return Utils.getResponseEntity("Category Added Sucssesfully.", HttpStatus.OK); 
					
					
				}
			}
			else {
				Utils.getResponseEntity(Constance.UNAUTHORIZED_ACCESS, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
				
	}

	private boolean ValidateCategoryMap(Map<String, String> requestMap, boolean validateId) {
		if (requestMap.containsKey("name")) {
			if (requestMap.containsKey("id") && validateId) {
				return true;
				
			}
			else if(!validateId) {
				return true;
			}
			
		}
		return false;
	}
	
	private category getCaregoryfromMap(Map<String, String> requestMap, Boolean isAdd) {
		category category=new category();
		if (isAdd) {
			category.setId(Integer.parseInt(requestMap.get("id")));
			
		}
		category.setName(requestMap.get("name"));
		category.setImage(requestMap.get("image"));
		return category;
	}

	@Override
	public ResponseEntity<List<category>> getAllCategory(String filterValue) {
		try {
			if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
				log.info("inside if");

				return new ResponseEntity<List<category>>(categoryDao.getAllCategory(),HttpStatus.OK);
			}
			return new ResponseEntity<List<category>>(categoryDao.findAll(),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (ValidateCategoryMap(requestMap, true)) {
					java.util.Optional<category> optional=categoryDao.findById(Integer.parseInt(requestMap.get("id")));
					if (!optional.isEmpty()) {
						categoryDao.save(getCaregoryfromMap(requestMap, true));
						return Utils.getResponseEntity("Category Updated Sucssesfully",HttpStatus.OK);
					}
					else {
						return Utils.getResponseEntity("Category ID dosen't exist .", HttpStatus.OK);
					}
				}
				return Utils.getResponseEntity(Constance.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
			else {
				Utils.getResponseEntity(Constance.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
