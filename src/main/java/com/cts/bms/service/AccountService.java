package com.cts.bms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.bms.dao.AccountDao;
import com.cts.bms.exception.BmsException;
import com.cts.bms.model.Account;

@Service
public class AccountService {

	@Autowired
	AccountDao dao;

	public boolean depositIntoAccount(Account account, double amount) {
		try {
			return dao.depositIntoAccount(account, amount);
		} catch (BmsException e) {
			return false;
		}
	}

	public boolean withdrawFromAccount(Account account, double amount) {
		try {
			if(account.getBalance()<amount) {
				return false;
			}
			return dao.withdrawFromAccount(account, amount);
		} catch (BmsException e) {
			return false;
		}
	}
	
	public boolean sendAmountToAccount(Account payeeAccount,Account receivingAccount,double amount) {
		try {
			dao.depositIntoAccount(receivingAccount, amount);
			dao.withdrawFromAccount(payeeAccount, amount);
			return true;
		} catch (BmsException e) {
			return false;
		}
	}
	
	public Account checkBalance(long accountNo) {
		
		return dao.getAccountWithAccountNumber(accountNo);
	}
	
	

}
