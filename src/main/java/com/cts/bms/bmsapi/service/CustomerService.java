package com.cts.bms.bmsapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.bms.bmsapi.dao.AccountDao;
import com.cts.bms.bmsapi.dao.CustomerDao;
import com.cts.bms.bmsapi.exception.AdhaarNumberNotFoundException;
import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.model.Customer;



@Service
public class CustomerService {
	
	@Autowired
	CustomerDao customerDao;
	
	@Autowired
	AccountDao accountDao;

	public Customer createNewUser(Customer customer) throws AdhaarNumberNotFoundException{		
		try {
			if(accountDao.checkAdhaarNo(customer).isEmpty()) {
				
				throw new AdhaarNumberNotFoundException("No accounts found");
			}
			customer.setApproved(true);
			return customerDao.addCustomer(customer);
		} catch (BmsException e) {
			return null;
		}
	}
	
	
}
