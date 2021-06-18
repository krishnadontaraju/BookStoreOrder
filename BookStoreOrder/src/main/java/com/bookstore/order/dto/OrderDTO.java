package com.bookstore.order.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderDTO {
	
	private LocalDateTime orderDate;
	private int quantity;
	private float price;
	private String address;
	private long customer;
	private long book;

}
