package com.cts.bms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.bms.dao.AccountDao;
import com.cts.bms.dao.AdminDao;
import com.cts.bms.dao.BranchDao;
import com.cts.bms.dao.CustomerDao;
import com.cts.bms.exception.BmsException;
import com.cts.bms.model.Account;
import com.cts.bms.model.Loan;


@Service
public class LoanService {
	
	@Autowired
	CustomerDao customerDao;
	
	@Autowired
	AdminDao adminDao;
	
	@Autowired
	BranchDao branchDao;
	
	@Autowired
	AccountDao accountDao;
	
	public boolean applyForLoan(Loan loan) {
		try {
			if(customerDao.getActiveAccount(loan.getAccNo())!=null && customerDao.getActiveAccount(loan.getGurantorAccNo())!=null) {
				customerDao.applyForLoan(loan);
				return true;
			}
			return false;
		}catch(BmsException e) {
			return false;
		}
		
	}
	
	public boolean grantLoan(Loan loan,String ifscCode) {
		try {
			if(branchDao.deductFromBranch(ifscCode,loan.getAmount())) {
				if(adminDao.approveLoanRequest(loan.getLoan_id(), true)) {
					//get account with account number
					Account account = accountDao.getAccountWithAccountNumber(loan.getAccNo());
					if(account==null) {
						branchDao.depositIntoBranch(ifscCode, loan.getAmount());
					}
					else {
						//logic for deposit to customer account
						
						accountDao.depositIntoAccount(account, loan.getAmount());
						
					}
					return true;
				}
			}
		} catch (BmsException e) {
			return false;
		}
		return false;
	}

	public List<Loan> getAllApprovedLoans() {
		return adminDao.viewAllApprovedLoans();
	}
	
	
	
}
