package net.javaguide.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import net.javaguide.springboot.POJO.User;

public interface UserDao extends JpaRepository<User, Integer> {
	
	User findByEmailId(@Param("email") String email);

}
