package net.javaguide.springboot.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguide.springboot.POJO.category;

public interface CategoryDao extends JpaRepository<category,Integer>{
	
	List<category> getAllCategory();

}
