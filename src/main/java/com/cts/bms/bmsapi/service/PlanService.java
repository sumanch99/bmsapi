package com.cts.bms.bmsapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.bms.bmsapi.dao.AdminDao;
import com.cts.bms.bmsapi.model.InterestRate;



@Service
public class PlanService {
	
	@Autowired
	AdminDao adminDao;
	
	public List<InterestRate> viewAllPlans() {
		return adminDao.viewAllPlans();
	}
}
