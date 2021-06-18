package com.bookstore.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bookstore.order.model.OrderModel;



public interface OrderRepository extends JpaRepository<OrderModel , Long>{

	@Query(value = "SELECT * FROM orders as o WHERE o.customer = ?")
	List<OrderModel> findAllByCustomer(long customer);

}
