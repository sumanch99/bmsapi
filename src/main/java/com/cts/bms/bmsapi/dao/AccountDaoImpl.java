package com.cts.bms.bmsapi.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cts.bms.bmsapi.dto.AccountMapper;
import com.cts.bms.bmsapi.exception.AdhaarNumberNotFoundException;
import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.model.Account;
import com.cts.bms.bmsapi.model.Customer;


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
		String query2 = "insert into transaction (from_account, to_account, amount, flag, atm_flag) "+
		"values(?,?,?,?,?)";
		try {
			jdbcTemplate.update(query, new Object[] { amount, account.getAccNo() });
			jdbcTemplate.update(query2, new Object[] { account.getAccNo(),null,amount,true,false });
			return true;
		} catch (DataAccessException e) {
			throw new BmsException("Deposit not possible");
		}
	}

	@Override
	public boolean withdrawFromAccount(Account account, double amount) throws BmsException {
		String query = "update account set balance = balance - ? where account_no = ?";
		String query2 = "insert into transaction (from_account, to_account, amount, flag, atm_flag) "+
				"values(?,?,?,?,?)";
		try {
			jdbcTemplate.update(query, new Object[] { amount, account.getAccNo() });
			jdbcTemplate.update(query2, new Object[] { account.getAccNo(),null,amount,false,false });
			return true;
		} catch (DataAccessException e) {
			throw new BmsException("Withdraw not possible");

		}

	}
	
	@Override
	public boolean withDrawThroughDebitCard(Account account,double amount) throws BmsException{
		String query = "update account set balance = balance - ? where account_no = ?";
		String query2 = "insert into transaction (from_account, to_account, amount, flag, atm_flag) "+
				"values(?,?,?,?,?)";
		try {
			jdbcTemplate.update(query, new Object[] { amount, account.getAccNo() });
			jdbcTemplate.update(query2, new Object[] { account.getAccNo(),null,amount,false,true });
			return true;
		} catch (DataAccessException e) {
			throw new BmsException("ATM Withdraw not possible");

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

	@Override
	public boolean accountToAccountTransfer(Account payeeAccount, Account receivingAccount, double amount) {
		
		try {
			String query = "update account set balance = balance - ? where account_no = ?";
			jdbcTemplate.update(query, new Object[] { amount, payeeAccount.getAccNo() });
			query = "update account set balance = balance + ? where account_no = ?";
			jdbcTemplate.update(query, new Object[] { amount, receivingAccount.getAccNo() });
			query = "insert into transaction (from_account, to_account, amount, flag, atm_flag) "+
					"values(?,?,?,?,?)";
			jdbcTemplate.update(query, new Object[] {
					payeeAccount.getAccNo(), 
					receivingAccount.getAccNo(),
					amount, 
					null,
					false
			});
			return true;
		}catch(DataAccessException e) {
			e.printStackTrace();
			return false;
		}
	}

}
