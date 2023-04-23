package net.javaguide.springboot.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;
import net.javaguide.springboot.POJO.User;
import net.javaguide.springboot.constents.Constance;
import net.javaguide.springboot.dao.UserDao;
import net.javaguide.springboot.jwt.CustomrUserDetailService;
import net.javaguide.springboot.jwt.JwtUtils;
import net.javaguide.springboot.jwt.jwtFilter;
import net.javaguide.springboot.service.UserService;
import net.javaguide.springboot.utills.EmailUtils;
import net.javaguide.springboot.utills.Utils;
import net.javaguide.springboot.wrapper.UserWrapper;

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

	@Autowired
	jwtFilter jwtFilter;

	@Autowired
	EmailUtils emailUtils;

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
				&& requestMap.containsKey("contact") && requestMap.containsKey("email")) {
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

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		try {
			if (jwtFilter.isAdmin()) {
				return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);

			} else {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
				if (!optional.isEmpty()) {
					userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
					sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());
					return Utils.getResponseEntity("User Status Updated succesfully.", HttpStatus.OK);
				}

				else {
					Utils.getResponseEntity("user id dosen't exist .", HttpStatus.OK);
				}
			}

			else {
				Utils.getResponseEntity(Constance.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {

		allAdmin.remove(jwtFilter.getCurrentUser());
		if (status != null && status.equalsIgnoreCase("true")) {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "account approved",
					"USER:-" + user + "\n is approved by \nADMIN:-" + jwtFilter.getCurrentUser(), allAdmin);
		}

		else {
			emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "account disabled",
					"USER:-" + user + "\n is approved by \nADMIN:-" + jwtFilter.getCurrentUser(), allAdmin);
		}

	}

	@Override
	public ResponseEntity<String> checkToken() {
		return Utils.getResponseEntity("true", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		try {
			User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
			if (!userObj.equals(null)) {
				if (userObj.getPassword().equals(requestMap.get("oldPassword"))) {
					userObj.setPassword(requestMap.get("newPassword"));
					userDao.save(userObj);
					return Utils.getResponseEntity("Password Updated Succsesfully .", HttpStatus.OK);
				}

				return Utils.getResponseEntity("Old Password is incorrect.", HttpStatus.BAD_REQUEST);
			}

			return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
		try {
			User user = userDao.findByEmail(requestMap.get("email"));
			if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
				emailUtils.forgotMail(user.getEmail(), "Credential By Persist tech", user.getPassword());

			return Utils.getResponseEntity("Check your mail for Credentials .", HttpStatus.OK);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return Utils.getResponseEntity(Constance.SOMETHING_WNT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
