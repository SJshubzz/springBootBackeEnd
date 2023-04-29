package net.javaguide.springboot.restImpl;

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
		// TODO Auto-generated method stub
		return null;
	}

}
