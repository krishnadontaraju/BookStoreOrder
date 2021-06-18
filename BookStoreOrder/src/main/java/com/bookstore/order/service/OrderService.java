package com.bookstore.order.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bookstore.order.dto.OrderDTO;
import com.bookstore.order.exception.OrderException;
import com.bookstore.order.model.OrderModel;
import com.bookstore.order.repository.OrderRepository;
import com.bookstore.order.response.Response;
import com.bookstore.order.utility.EmailUtility;
import com.bookstore.order.utility.TokenUtility;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService implements IOrderService{
	
	@Autowired
	private TokenUtility tokenManager;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private EmailUtility emailUtility;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Response placeOrder(OrderDTO orderDTO) {
		
		log.info("Placing an Order has Started for user "+orderDTO.getCustomer());
		//Fetching Customer Name
		String customerName = restTemplate.getForObject("http://localhost:6001/customer/checkCustomerName/"+orderDTO.getCustomer() , String.class);
		//Fetching Customer Email
		String customerEmail = restTemplate.getForObject("http://localhost:6001/customer/checkCustomerName/"+orderDTO.getCustomer() , String.class);
		//Fetching Book title
		String bookTilte = restTemplate.getForObject("http://localhost:6002/customer/fetchTitle/"+orderDTO.getBook() , String.class);

		OrderModel order = mapper.map(orderDTO , OrderModel.class);
			
		//Sending an email stating order Confirmation
		emailUtility.sendEmail(customerEmail , "Order Placed" ,"Thank You ,"+customerName+" for your Order of Title "+bookTilte +"/nEnjoy Reading !");
			
		log.info("Successfully placed the order for Customer : "+customerName +" Email : "+customerEmail +" and Title : "+bookTilte);
		orderRepository.save(order);
		return new Response("Successfully placed the Order" ,bookTilte);
			
	}

	@Override
	public Response cancelOrder(long orderId) {
		log.info("Order cancel request has been Initiated ");
		Optional<OrderModel> isOrderPlaced = orderRepository.findById(orderId);
		
		if (isOrderPlaced.isPresent()) {
			log.info("Cancelling an Order has Started for user "+isOrderPlaced.get().getCustomer());
			//Fetching Customer Name
			String customerName = restTemplate.getForObject("http://localhost:6001/customer/checkCustomerName/"+isOrderPlaced.get().getCustomer() , String.class);
			//Fetching Customer Email
			String customerEmail = restTemplate.getForObject("http://localhost:6001/customer/checkCustomerName/"+isOrderPlaced.get().getCustomer() , String.class);
			//Fetching Book title
			String bookTilte = restTemplate.getForObject("http://localhost:6002/customer/fetchTitle/"+isOrderPlaced.get().getBook() , String.class);
				
			//Sending an email stating order Confirmation
			emailUtility.sendEmail(customerEmail , "Order Cancelled" ,"Sorry ,"+customerName+"not to see  ,"+bookTilte+"/nHope You can find an amazing read Again at Book Store /nThanks");
				
			log.info("Cancelled the order for Customer : "+customerName +" Email : "+customerEmail +" and Title : "+bookTilte);
			orderRepository.delete(isOrderPlaced.get());
			return new Response("Cancelled the Order" ,bookTilte);
		}else {
			log.error("Order could not be found with Id "+orderId);
			throw new OrderException(501 , "Order not found");
		}
	}

	@Override
	public Response viewAllOrders(String token) {
		log.info("Requested to view All Orders");
		
		long userId = tokenManager.decodeToken(token);
		
		boolean doesCustomerExist = restTemplate.getForObject("http://localhost:6001/customer/checkCustomer/"+userId , boolean.class);
		
		if (doesCustomerExist == true) {
			log.info("Access verified with token ,"+token+" now retrieving all Orders ");
			return new Response("All Orders" , orderRepository.findAll());
		}else {
			log.error("Token Could not be verified "+token);
			throw new OrderException(501, "Access cannot be Granted");
		}
		
	}

	@Override
	public Response viewOrderOfCustomer(String token) {
		log.info("Requested to view All Orders by Customer");
		
		long customerId = tokenManager.decodeToken(token);
		
		boolean doesCustomerExist = restTemplate.getForObject("http://localhost:6001/customer/checkCustomer/"+customerId , boolean.class);
		
		if (doesCustomerExist == true) {
			log.info("Access verified with token ,"+token+" now retrieving all Orders by Customer");
			return new Response("All Orders by requested Customer" , orderRepository.findAllByCustomer(customerId));
		}else {
			log.error("Token Could not be verified "+token);
			throw new OrderException(501, "Access cannot be Granted");
		}
	}

}
