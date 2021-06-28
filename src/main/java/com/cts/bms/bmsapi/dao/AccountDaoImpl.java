package com.cts.bms.bmsapi.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static final Logger LOGGER=LogManager.getLogger(AccountDaoImpl.class);
	@Autowired
	JdbcTemplate jdbcTemplate;

	@SuppressWarnings("deprecation")
	@Override
	public List<Account> checkAdhaarNo(Customer customer) throws AdhaarNumberNotFoundException {

		/*
		 * This method checks wheather a bank account is available against the adhaar
		 * number of the user.
		 */
		LOGGER.info("START");
		String query = "select * from account where adhaar_no = ? and phonenumber = ?";

		try {

			List<Account> accounts = jdbcTemplate.query(query,
					new Object[] { customer.getAdhaarNo(), customer.getPhoneNumber() }, new AccountMapper());
			LOGGER.info("END");
			return accounts;

		} catch (DataAccessException e) {
			LOGGER.error(e.getMessage());
			throw new AdhaarNumberNotFoundException("No Data available");
		}
	}

	@Override
	public boolean depositIntoAccount(Account account, double amount) throws BmsException {
		LOGGER.info("START");
		String query = "update account set balance = balance + ? where account_no = ?";
		String query2 = "insert into transaction (from_account, to_account, amount, flag, atm_flag) "+
		"values(?,?,?,?,?)";
		try {
			jdbcTemplate.update(query, new Object[] { amount, account.getAccNo() });
			jdbcTemplate.update(query2, new Object[] { account.getAccNo(),null,amount,true,false });
			LOGGER.info("END");
			return true;
		} catch (DataAccessException e) {
			LOGGER.error(e.getMessage());
			throw new BmsException("Deposit not possible");
		}
	}

	@Override
	public boolean withdrawFromAccount(Account account, double amount) throws BmsException {
		LOGGER.info("START");
		String query = "update account set balance = balance - ? where account_no = ?";
		String query2 = "insert into transaction (from_account, to_account, amount, flag, atm_flag) "+
				"values(?,?,?,?,?)";
		try {
			jdbcTemplate.update(query, new Object[] { amount, account.getAccNo() });
			jdbcTemplate.update(query2, new Object[] { account.getAccNo(),null,amount,false,false });
			LOGGER.info("END");
			return true;
		} catch (DataAccessException e) {
			LOGGER.error(e.getMessage());
			throw new BmsException("Withdraw not possible");

		}

	}
	
	@Override
	public boolean withDrawThroughDebitCard(Account account,double amount) throws BmsException{
		LOGGER.info("START");
		String query = "update account set balance = balance - ? where account_no = ?";
		String query2 = "insert into transaction (from_account, to_account, amount, flag, atm_flag) "+
				"values(?,?,?,?,?)";
		try {
			jdbcTemplate.update(query, new Object[] { amount, account.getAccNo() });
			jdbcTemplate.update(query2, new Object[] { account.getAccNo(),null,amount,false,true });
			LOGGER.info("END");
			return true;
		} catch (DataAccessException e) {
			LOGGER.error(e.getMessage());
			throw new BmsException("ATM Withdraw not possible");

		}
	}
	
	@Override
	public Account getAccountWithAccountNumber(long accountNo) {
		LOGGER.info("START");
		try {
			String query = "select * from account where account_no = " + accountNo;
			Account account = jdbcTemplate.queryForObject(query, new AccountMapper());
			LOGGER.info("END");
			return account;
		} catch (DataAccessException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	@Override
	public List<Account> getAllAccounts() {
		LOGGER.info("START");
		try {
			String query = "select * from account";
			List<Account> accounts = jdbcTemplate.query(query, new AccountMapper());
			LOGGER.info("END");
			return accounts;
		} catch (DataAccessException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@Override
	public boolean accountToAccountTransfer(Account payeeAccount, Account receivingAccount, double amount) {
		LOGGER.info("START");
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
			LOGGER.info("END");
			return true;
		}catch(DataAccessException e) {
			LOGGER.error(e.getMessage());
			return false;
		}
	}


}
