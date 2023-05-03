package net.javaguide.springboot.restImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import net.javaguide.springboot.constents.Constance;
import net.javaguide.springboot.rest.ProductRest;
import net.javaguide.springboot.service.ProductService;
import net.javaguide.springboot.utills.Utils;
import net.javaguide.springboot.wrapper.ProductWrapper;

@RestController
public class ProductRestImpl implements ProductRest {

	@Autowired
	ProductService productService;

	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		try {
			return productService.addNewProduct(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProduct() {
		try {
			return productService.getAllProduct();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
		try {
			return productService.updateProduct(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity("error", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> deleteProduct(Integer id) {
		try {
			return productService.deleteProduct(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
		try {
			return productService.updateStatus(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
		try {
			return productService.getByCategory(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<ProductWrapper> getProductById(Integer id) {
		try {
			return productService.getProductById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
