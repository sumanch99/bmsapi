package com.cts.bms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cts.bms.model.InterestRate;
import com.cts.bms.dao.AdminDao;

@Service
public class PlanService {
	
	@Autowired
	AdminDao adminDao;
	
	public List<InterestRate> viewAllPlans() {
		return adminDao.viewAllPlans();
	}
}
