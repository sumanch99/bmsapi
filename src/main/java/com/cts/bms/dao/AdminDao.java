package com.cts.bms.dao;

import java.util.List;

import com.cts.bms.exception.BmsException;
import com.cts.bms.model.Account;
import com.cts.bms.model.Branch;
import com.cts.bms.model.Customer;
import com.cts.bms.model.DebitCard;
import com.cts.bms.model.InterestRate;
import com.cts.bms.model.Loan;

public interface AdminDao {

	public List<Customer> viewAllCustomer();

	public List<Loan> viewAllPendingLoanRequests();

	public List<Loan> viewAllApprovedLoans();
	
	public boolean deleteRejectedLoanRequest(long loanId) throws BmsException;

	public boolean enableAccount(long accountNo) throws BmsException;

	public boolean disableAccount(long accountNo) throws BmsException;

	public boolean createAccount(Account account) throws BmsException;

	public boolean approveLoanRequest(long loan_id, boolean isLoanApproved) throws BmsException;

	public boolean changeInterestRate(InterestRate irate) throws BmsException;

	public boolean addBankBranch(Branch branch) throws BmsException;
	
	public List<Account> viewAllAccountsOfCustomer(Customer customer);
	
	public List<InterestRate> viewAllPlans();
	
	public List<DebitCard> viewAllPendingDebitCards() throws BmsException;
	
	public boolean approveDebitCard(DebitCard card) throws BmsException;
	  
	public boolean deleteRejectedDebitCard(DebitCard card) throws BmsException;

}
