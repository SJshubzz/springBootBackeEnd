package net.javaguide.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import net.javaguide.springboot.POJO.Bill;

public interface BillDao extends JpaRepository<Bill, Integer> {

}
