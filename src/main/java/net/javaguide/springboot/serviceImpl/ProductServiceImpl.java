package net.javaguide.springboot.serviceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import net.javaguide.springboot.POJO.Product;
import net.javaguide.springboot.POJO.category;
import net.javaguide.springboot.constents.Constance;
import net.javaguide.springboot.dao.ProductDao;
import net.javaguide.springboot.jwt.jwtFilter;
import net.javaguide.springboot.service.ProductService;
import net.javaguide.springboot.utills.Utils;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	jwtFilter jwtFilter;
	
	@Autowired
	ProductDao productDao;

	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validateProductMap(requestMap ,false)) {
					productDao.save(getProductMap(requestMap,false));
					return Utils.getResponseEntity("Product Added Sucssesfully.", HttpStatus.OK);
				}
				return Utils.getResponseEntity(Constance.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
			else {
				return Utils.getResponseEntity(Constance.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR) ;
	}

	

	private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
		if (requestMap.containsKey("name")) {
			if (requestMap.containsKey("id") && validateId) {
				return true;
			}
			else if (!validateId) {
				return true;
			}
		}
		return false;
	}
	
	private Product getProductMap(Map<String, String> requestMap, boolean isAdd) {
		category category=new category();
		category.setId(Integer.parseInt(requestMap.get("categoryId")));
		Product product=new Product();
		
		if (isAdd) {
			product.setId(Integer.parseInt(requestMap.get("id")));
		}
		else {
			product.setStatus("true");
		}
		
		product.setCategory(category);
		product.setName(requestMap.get("name"));
		product.setPrice(Float.parseFloat(requestMap.get("price")));
		product.setDescription(requestMap.get("description"));

		return product;
	}

	
}
