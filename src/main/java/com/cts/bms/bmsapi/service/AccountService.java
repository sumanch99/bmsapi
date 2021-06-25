package com.cts.bms.bmsapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.bms.bmsapi.dao.AccountDao;
import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.model.Account;



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
	
	public boolean accountToAccountTransfer(long payeeAccountNo,long receivingAccountNo,double amount) throws BmsException {
		
		Account receivingAccount = dao.getAccountWithAccountNumber(receivingAccountNo);
		Account payeeAccount = dao.getAccountWithAccountNumber(payeeAccountNo);
		if(receivingAccount!=null && payeeAccount!=null) {
			if(payeeAccount.getBalance()<amount) {
				throw new BmsException("Insufficient balance");
			}
			return dao.accountToAccountTransfer(payeeAccount, receivingAccount, amount);
		}
		return false;
			
	}
	
	public Account checkBalance(long accountNo) {
		
		return dao.getAccountWithAccountNumber(accountNo);
	}
	

}
