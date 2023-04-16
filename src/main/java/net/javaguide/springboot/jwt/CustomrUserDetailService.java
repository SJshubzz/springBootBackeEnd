package net.javaguide.springboot.jwt;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.javaguide.springboot.dao.UserDao;

@Service
@Slf4j
public class CustomrUserDetailService implements UserDetailsService {

	@Autowired
	UserDao userDao;
	private net.javaguide.springboot.POJO.User userDetails;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		log.info("inside loadUserByUsername {}", username);

		userDetails = userDao.findByEmailId(username);
		if (!Objects.isNull(userDetails))
			return new User(userDetails.getEmail(), userDetails.getPassword(), new ArrayList<>());
		else
			throw new UsernameNotFoundException("user not found .");

	}

	public net.javaguide.springboot.POJO.User getUserDetail() {
		return userDetails;

	}

}
