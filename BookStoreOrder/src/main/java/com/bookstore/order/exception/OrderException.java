package com.bookstore.order.exception;

@SuppressWarnings("serial")
public class OrderException extends RuntimeException{

	@SuppressWarnings("unused")
	private int exceptionCode;
	@SuppressWarnings("unused")
	private String message;
	
	public OrderException(int exceptionCode ,String message) {
		super(message);
		this.exceptionCode = exceptionCode;
	}
	
}

