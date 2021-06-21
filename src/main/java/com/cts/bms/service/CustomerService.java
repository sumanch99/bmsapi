package com.cts.bms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.bms.dao.AccountDao;
import com.cts.bms.dao.CustomerDao;
import com.cts.bms.exception.AdhaarNumberNotFoundException;
import com.cts.bms.exception.BmsException;
import com.cts.bms.model.Customer;

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
