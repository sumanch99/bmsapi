package com.cts.bms.dao;

import java.util.List;

import com.cts.bms.exception.BmsException;
import com.cts.bms.model.Account;
import com.cts.bms.model.Customer;
import com.cts.bms.model.DebitCard;
import com.cts.bms.model.FixedDeposit;
import com.cts.bms.model.Loan;

public interface CustomerDao {
	public Customer addCustomer(Customer customer) throws BmsException;

	public Loan applyForLoan(Loan loan) throws BmsException;

	public boolean applyForFd(FixedDeposit fixedDeposit) throws BmsException;

	public Account getActiveAccount(long accountNo) throws BmsException;

	public List<Loan> viewAllApprovedLoans(String userId);

	public boolean applyForDebitCard(DebitCard debitCard) throws BmsException;

	public List<DebitCard> viewAllApprovedDebitCards(Account account) throws BmsException;

	public DebitCard getDebitCard(long cardNo,int cvvNo,int pin);
}
