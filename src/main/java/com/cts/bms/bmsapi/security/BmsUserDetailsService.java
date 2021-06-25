package com.cts.bms.bmsapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cts.bms.bmsapi.dao.AdminDao;
import com.cts.bms.bmsapi.dao.CustomerDao;
import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.model.Admin;
import com.cts.bms.bmsapi.model.Customer;

@Service
public class BmsUserDetailsService implements UserDetailsService {

	@Autowired
	AdminDao adminDao;
	
	@Autowired
	CustomerDao customerDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if ( username == null || username.isEmpty() ){
	        throw new UsernameNotFoundException("username is empty");
	    }
		
		try {
			Admin admin = adminDao.loadAdmin(username);
			if(admin!=null) {
				return new SecurityUser(Long.toString(admin.getEmpId()), admin.getPassword(),"ADMIN");
			}
			Customer customer = customerDao.loadCustomer(username);
			if(customer!=null) {
				return new SecurityUser(customer.getUserId(), customer.getPassword(),"CUSTOMER");
			}
		} catch (BmsException e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("Credential not found");
		}
		
		throw new UsernameNotFoundException("Credential not found");
	}

}
