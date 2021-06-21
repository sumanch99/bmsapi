package com.cts.bms.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.exception.AdhaarNumberNotFoundException;
import com.cts.bms.exception.BmsException;
import com.cts.bms.model.Customer;
import com.cts.bms.response.CustomJsonResponse;
import com.cts.bms.service.CustomerService;

/*
 * Rest Controller to map all requests coming with customer prefix.
 */
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
	@PostMapping("/signup")
	public ResponseEntity<Object> createCustomerAccount(@Valid @RequestBody Customer customer, BindingResult result){
		if(result.hasErrors()) {
			/*
			 * checking whether all fields are not null in customer object.
			 * 
			 * Returning custom JSON response with message, status code and errors
			 */
			return CustomJsonResponse.generateResponse("Improper input", HttpStatus.BAD_REQUEST, result.getAllErrors());
		}
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
