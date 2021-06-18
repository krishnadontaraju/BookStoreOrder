package com.bookstore.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.order.dto.OrderDTO;
import com.bookstore.order.response.Response;
import com.bookstore.order.service.IOrderService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private IOrderService orderService;
	
	//End point to place order for customer
	@PostMapping("/placeOrder")
	public ResponseEntity<Response> placeOrder (@RequestBody OrderDTO orderDTO){
		
		Response placeOrderResponse = orderService.placeOrder(orderDTO);
		return new ResponseEntity<Response>(placeOrderResponse , HttpStatus.OK);
		
	}
	
	//End point to cancel an oder for customer
	@DeleteMapping("/cancelOrder")
	public ResponseEntity<Response> cancelOrder (@RequestParam long orderId){
		
		Response placeOrderResponse = orderService.cancelOrder(orderId);
		return new ResponseEntity<Response>(placeOrderResponse , HttpStatus.OK);
		
	}
	
	//End point to view All orders the Parameter is a token which is generated for an Admin
	@GetMapping("/allOrders")
	public ResponseEntity<Response> viewAllOrders(@RequestHeader String token){
		
		Response viewAllOrdersResponse = orderService.viewAllOrders(token);
		return new ResponseEntity<Response> (viewAllOrdersResponse ,HttpStatus.OK);
		
	}
	
	//End point to view All Orders by customer 
	//Parameter is a token which generated when customer is registered
	@GetMapping("/ordersByCustomer")
	public ResponseEntity<Response> viewOrdersByCustomer(@RequestHeader String token){
		
		Response viewAllOrdersResponse = orderService.viewOrderOfCustomer(token);
		return new ResponseEntity<Response> (viewAllOrdersResponse ,HttpStatus.OK);
		
	}
	
}
