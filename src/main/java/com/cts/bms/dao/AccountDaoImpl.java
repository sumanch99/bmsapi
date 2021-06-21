package com.cts.bms.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cts.bms.dto.AccountMapper;
import com.cts.bms.exception.AdhaarNumberNotFoundException;
import com.cts.bms.exception.BmsException;
import com.cts.bms.model.Account;
import com.cts.bms.model.Customer;

@Repository
public class AccountDaoImpl implements AccountDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@SuppressWarnings("deprecation")
	@Override
	public List<Account> checkAdhaarNo(Customer customer) throws AdhaarNumberNotFoundException {

		/*
		 * This method checks wheather a bank account is available against the adhaar
		 * number of the user.
		 */

		String query = "select * from account where adhaar_no = ? and phonenumber = ?";

		try {

			List<Account> accounts = jdbcTemplate.query(query,
					new Object[] { customer.getAdhaarNo(), customer.getPhoneNumber() }, new AccountMapper());
			return accounts;

		} catch (DataAccessException e) {
			throw new AdhaarNumberNotFoundException("No Data available");
		}
	}

	@Override
	public boolean depositIntoAccount(Account account, double amount) throws BmsException {
		String query = "update account set balance = balance + ? where account_no = ?";
		try {
			jdbcTemplate.update(query, new Object[] { amount, account.getAccNo() });
			return true;
		} catch (DataAccessException e) {
			throw new BmsException("Deposit not possible");
		}
	}

	@Override
	public boolean withdrawFromAccount(Account account, double amount) throws BmsException {
		String query = "update account set balance = balance - ? where account_no = ?";
		try {
			jdbcTemplate.update(query, new Object[] { amount, account.getAccNo() });
			return true;
		} catch (DataAccessException e) {
			throw new BmsException("Withdraw not possible");

		}

	}

	
	@Override
	public Account getAccountWithAccountNumber(long accountNo) {
		try {
			String query = "select * from account where account_no = " + accountNo;
			Account account = jdbcTemplate.queryForObject(query, new AccountMapper());
			return account;
		} catch (DataAccessException e) {
			return null;
		}
	}

}
