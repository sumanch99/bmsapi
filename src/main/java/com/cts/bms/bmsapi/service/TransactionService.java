package com.cts.bms.bmsapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.bms.bmsapi.dao.AccountDao;
import com.cts.bms.bmsapi.dao.TransactionDao;
import com.cts.bms.bmsapi.model.Account;
import com.cts.bms.bmsapi.model.Transaction;



@Service
public class TransactionService {
	
	@Autowired
	TransactionDao dao;
	
	@Autowired
	AccountDao accountDao;
	
	public List<Transaction> viewAllTransactions(){
		return dao.viewAllTransaction();
		
	}
	
	public List<Transaction> viewAllTransactions(long accountNo){
		Account account = accountDao.getAccountWithAccountNumber(accountNo);
		if(account!=null) {
			return dao.viewAllTransaction(account);
		}
		return null;
		
	}
}
