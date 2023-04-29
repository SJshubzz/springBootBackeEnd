package net.javaguide.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguide.springboot.POJO.Product;

public interface ProductDao extends JpaRepository<Product, Integer> {

}
