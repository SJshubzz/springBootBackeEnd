package net.javaguide.springboot.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import net.javaguide.springboot.wrapper.ProductWrapper;

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
				if (validateProductMap(requestMap, false)) {
					productDao.save(getProductMap(requestMap, false));
					return Utils.getResponseEntity("Product Added Sucssesfully.", HttpStatus.OK);
				}
				return Utils.getResponseEntity(Constance.INVALID_DATA, HttpStatus.BAD_REQUEST);
			} else {
				return Utils.getResponseEntity(Constance.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
		if (requestMap.containsKey("name")) {
			if (requestMap.containsKey("id") && validateId) {
				return true;
			} else if (!validateId) {
				return true;
			}
		}
		return false;
	}

	private Product getProductMap(Map<String, String> requestMap, boolean isAdd) {
		category category = new category();
		category.setId(Integer.parseInt(requestMap.get("categoryId")));
		Product product = new Product();

		if (isAdd) {
			product.setId(Integer.parseInt(requestMap.get("id")));
		} else {
			product.setStatus("true");
		}

		product.setCategory(category);
		product.setName(requestMap.get("name"));
		product.setPrice(Integer.parseInt(requestMap.get("price")));
		product.setDescription(requestMap.get("description"));

		return product;
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getAllProduct() {
		try {
			return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				if (validateProductMap(requestMap, true)) {
					Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
					if (!optional.isEmpty()) {
						Product product = getProductMap(requestMap, true);
						product.setStatus(optional.get().getStatus());
						productDao.save(product);
						return Utils.getResponseEntity("Product updated sucssesfully", HttpStatus.OK);
					} else {
						return Utils.getResponseEntity("product ID dosen't exist .", HttpStatus.OK);
					}
				} else {
					return Utils.getResponseEntity(Constance.INVALID_DATA, HttpStatus.BAD_REQUEST);
				}
			} else {
				return Utils.getResponseEntity(Constance.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> deleteProduct(Integer id) {
		try {
			if (jwtFilter.isAdmin()) {
				Optional optional = productDao.findById(id);
				if (!optional.isEmpty()) {
					productDao.deleteById(id);
					return Utils.getResponseEntity("Product deleted sucssesfully", HttpStatus.OK);
				}else {
					return Utils.getResponseEntity("Product Dosen't exist .", HttpStatus.OK);
				}
			} else {
				return Utils.getResponseEntity(Constance.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				Optional optional= productDao.findById(Integer.parseInt(requestMap.get("id")));
				if (!optional.isEmpty()) {
					productDao.updateProductStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
					return Utils.getResponseEntity("Product status updated sucssesfully .", HttpStatus.OK);
				}else {
					return Utils.getResponseEntity("Product dosen't exist .", HttpStatus.OK);
				}
			}
			else {
				return Utils.getResponseEntity(Constance.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
		try {
			return new ResponseEntity<>(productDao.getProductByCategory(id),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<ProductWrapper> getProductById(Integer id) {
		try {
			return new ResponseEntity<>(productDao.getProductById(id),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
