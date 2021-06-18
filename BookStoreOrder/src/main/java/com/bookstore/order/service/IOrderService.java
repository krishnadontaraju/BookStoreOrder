package com.bookstore.order.service;

import com.bookstore.order.dto.OrderDTO;
import com.bookstore.order.response.Response;

public interface IOrderService {
	
	Response placeOrder(OrderDTO orderDTO);
	Response cancelOrder(long orderId);
	Response viewAllOrders(String token);
	Response viewOrderOfCustomer(String token);

}
