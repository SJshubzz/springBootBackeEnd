package net.javaguide.springboot.serviceImpl;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.javaguide.springboot.POJO.User;
import net.javaguide.springboot.constents.Constance;
import net.javaguide.springboot.dao.UserDao;
import net.javaguide.springboot.jwt.CustomrUserDetailService;
import net.javaguide.springboot.jwt.JwtUtils;
import net.javaguide.springboot.service.UserService;
import net.javaguide.springboot.utills.Utils;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	CustomrUserDetailService customrUserDetailService;

	@Autowired
	JwtUtils jwtUtils;

	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		log.info("inside signup {}", requestMap);
		try {

			if (validateSignUpMap(requestMap)) {
				User user = userDao.findByEmailId(requestMap.get("email"));
				if (Objects.isNull(user)) {
					userDao.save(getUserFormMap(requestMap));
					return Utils.getResponseEntity("Successfully Registerd .", HttpStatus.OK);
				} else {
					return Utils.getResponseEntity("email already exist.", HttpStatus.BAD_REQUEST);
				}

			} else {
				return Utils.getResponseEntity(Constance.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	private boolean validateSignUpMap(Map<String, String> requestMap) {
		if (requestMap.containsKey("first_name") && requestMap.containsKey("last_name")
				&& requestMap.containsKey("contact") && requestMap.containsKey("email")
				&& requestMap.containsKey("role") && requestMap.containsKey("status")) {
			return true;

		}
		return false;
	}

	private User getUserFormMap(Map<String, String> requestMap) {
		User user = new User();

		user.setFirst_name(requestMap.get("first_name"));
		user.setLast_name(requestMap.get("last_name"));
		user.setEmail(requestMap.get("email"));
		user.setContact(requestMap.get("contact"));
		user.setPassword(requestMap.get("password"));

		user.setRole("user");
		return user;
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		log.info("Inside login");
		try {
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));

			if (auth.isAuthenticated()) {
				if (customrUserDetailService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
					return new ResponseEntity<String>(
							"{\"token\":\""
									+ jwtUtils.generateToken(customrUserDetailService.getUserDetail().getEmail(),
											customrUserDetailService.getUserDetail().getRole())
									+ "\"}",

							HttpStatus.OK);
				} else {
					return new ResponseEntity<String>("{\"message\":\"" + "wait for admin approval." + "\"}",
							HttpStatus.BAD_REQUEST);

				}

			}

		} catch (Exception e) {
			log.error("{}", e);

		}
		return new ResponseEntity<String>("{\"message\":\"" + "Bad Credentials." + "\"}", HttpStatus.BAD_REQUEST);

	}

}
