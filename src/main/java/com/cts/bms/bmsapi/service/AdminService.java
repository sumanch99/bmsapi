package com.cts.bms.bmsapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.bms.bmsapi.dao.AdminDao;
import com.cts.bms.bmsapi.dao.CustomerDao;
import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.exception.IntroducerNotFoundException;
import com.cts.bms.bmsapi.model.Account;
import com.cts.bms.bmsapi.model.Admin;
import com.cts.bms.bmsapi.model.Branch;
import com.cts.bms.bmsapi.model.Customer;
import com.cts.bms.bmsapi.model.Employee;
import com.cts.bms.bmsapi.model.InterestRate;
import com.cts.bms.bmsapi.model.Loan;



@Service
public class AdminService {

	@Autowired
	AdminDao dao;
	
	@Autowired
	CustomerDao customerDao;
	
	public boolean createNewAdmin(Admin admin) {
		try {
		Employee emp = dao.getEmployee(admin.getEmpId(),admin.getAdhaarNo(), admin.getPhoneNo());
		if(emp!=null) {
			dao.addAdmin(admin);
			return true;
		}
		}catch(BmsException e) {
			return false;
		}
		return false;
	}
	
	public List<Customer> getAllCustomers() {
		return dao.viewAllCustomer();
	}
	
	public List<Loan> getAllPendingLoanRequests(){
		return dao.viewAllPendingLoanRequests();
	}
	
	public List<Loan> getAllApprovedLoans(String userId){
		return customerDao.viewAllApprovedLoans(userId);
	}
	
	public List<Account> getAllAccountsFromCustomer(Customer customer){
		return dao.viewAllAccountsOfCustomer(customer);
	}

	public boolean createNewAccount(Account account) throws IntroducerNotFoundException {
		try {
				customerDao.getActiveAccount(account.getIntroducerAccountNo());
				account.setActive(true);
				return dao.createAccount(account);
			}catch(BmsException e) {
				throw new IntroducerNotFoundException("No customer found with the given introducer account number");
			}
	}

	public boolean disableAccount(Account account) {

		try {
			dao.disableAccount(account.getAccNo());
			return true;
		} catch (BmsException e) {
			return false;
		}

	}

	public boolean enableAccount(Account account) {

		try {
			dao.enableAccount(account.getAccNo());
			return true;
		} catch (BmsException e) {
			return false;
		}

	}
	
	public boolean deleteLoanRequest(Long loanId) {
		try {
			return dao.deleteRejectedLoanRequest(loanId);
		} catch (BmsException e) {
			return false;
		}
	}

	public boolean changeInterestRate(InterestRate irate) {
		try {
			return dao.changeInterestRate(irate);
		} catch (BmsException e) {
			return false;
		}
	}
	
	public boolean addBankBranch(Branch branch) {
		try {
			return dao.addBankBranch(branch);
		} catch (BmsException e) {
			return false;
		}
	}
	
	

}
