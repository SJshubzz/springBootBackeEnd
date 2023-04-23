package net.javaguide.springboot.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import net.javaguide.springboot.POJO.User;
import net.javaguide.springboot.wrapper.UserWrapper;

public interface UserDao extends JpaRepository<User, Integer> {

	User findByEmailId(@Param("email") String email);

	List<UserWrapper> getAllUser();
	
	List<String> getAllAdmin();

	@Transactional
	@Modifying
	Integer updateStatus(@Param("status") String status, @Param("id") Integer id);
	
	User findByEmail(String email);
}
