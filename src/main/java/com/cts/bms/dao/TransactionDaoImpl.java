package com.cts.bms.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cts.bms.dto.TransactionMapper;
import com.cts.bms.model.Account;
import com.cts.bms.model.Transaction;

@Repository
public class TransactionDaoImpl implements TransactionDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@SuppressWarnings("deprecation")
	@Override
	public List<Transaction> viewAllTransaction(Account account) {
		try {
			String query = "select * from transaction where from_account = ? or to_account = ?";
			List<Transaction> transactions = jdbcTemplate.query(query,new Object[] {
					account.getAccNo(),
					account.getAccNo()
			}, new TransactionMapper());
			return transactions;
		}catch(DataAccessException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public List<Transaction> viewAllTransaction() {
		try {
			String query = "select * from transaction";
			List<Transaction> transactions = jdbcTemplate.query(query, new TransactionMapper());
			return transactions;
		}catch(DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

}
