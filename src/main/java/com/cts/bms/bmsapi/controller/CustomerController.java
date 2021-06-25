package com.cts.bms.bmsapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.bmsapi.exception.AdhaarNumberNotFoundException;
import com.cts.bms.bmsapi.model.Customer;
import com.cts.bms.bmsapi.response.CustomJsonResponse;
import com.cts.bms.bmsapi.service.CustomerService;



/*
 * Rest Controller to map all requests coming with customer prefix.
 */
@CrossOrigin(origins="*")
@RestController
@RequestMapping("/customer")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	/*
	 * Route to register new a customer.
	 * 
	 * handling post request with route "/signup"
	 */
	@PostMapping("/customer-signup")
	public ResponseEntity<Object> createCustomerAccount(@RequestBody Customer customer){
		try{
			/*
			 * calling service logic to insert customer data in DB.
			 * 
			 * This method will throw application specific exception BMSException on
			 * Unsuccessful DB operation.
			 */
			Customer cust = customerService.createNewUser(customer);
			if(cust==null) {
				return CustomJsonResponse.generateResponse("UserID already exists", HttpStatus.CONFLICT, customer);
			}
			
		}catch(AdhaarNumberNotFoundException e) {
			/*
			 * handling application specific exception BMSException and returning custom JSON response 
			 * with message UserID already exists because all other validations
			 * is done earlier.
			 */
			return CustomJsonResponse.generateResponse("Account not found", HttpStatus.CONFLICT, customer);
		}
		/*
		 * On successful execution success message is sent along with created customer object in
		 * custom JSON response.
		 */
		return CustomJsonResponse.generateResponse("User successfully created", HttpStatus.CREATED, customer);
	}
	
}
