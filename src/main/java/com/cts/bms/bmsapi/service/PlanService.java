package com.cts.bms.bmsapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.bms.bmsapi.dao.AccountDao;
import com.cts.bms.bmsapi.dao.AdminDao;
import com.cts.bms.bmsapi.dao.CustomerDao;
import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.model.Account;
import com.cts.bms.bmsapi.model.FixedDeposit;
import com.cts.bms.bmsapi.model.InterestRate;
import com.cts.bms.bmsapi.util.FixedDepositUtil;



@Service
public class PlanService {
	
	@Autowired
	AdminDao adminDao;
	
	@Autowired
	AccountDao accountDao;
	
	@Autowired
	CustomerDao custDao;
	
	public List<InterestRate> viewAllPlans() {
		return adminDao.viewAllPlans();
	}
	
	public boolean generateFixedDeposit(FixedDeposit fd) {
		fd.setInterest(custDao.getFdInterestRate());
		double maturedAmount = FixedDepositUtil.getMaturedAmount(fd.getAmount(), fd.getInterest(), fd.getTenure());
		fd.setMatured_amount(maturedAmount);
		try {
			custDao.applyForFd(fd);
			return true;
		} catch (BmsException e) {
			return false;
		}
	}
	
	public List<FixedDeposit> getAllFixedDeposits(long accountNo) {
		Account account = accountDao.getAccountWithAccountNumber(accountNo);
		if(account!=null) {
			try {
				return custDao.getAllFixedDeposits(account);
			} catch (BmsException e) {
				return null;
			}
		}
		return null;
	}
}
