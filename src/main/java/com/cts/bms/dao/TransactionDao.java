package com.cts.bms.dao;

import java.util.List;

import com.cts.bms.model.Account;
import com.cts.bms.model.Transaction;

public interface TransactionDao {
	
	public List<Transaction> viewAllTransaction(Account account);
	public List<Transaction> viewAllTransaction();
	
}
