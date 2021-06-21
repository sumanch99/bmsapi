package com.cts.bms.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cts.bms.dto.AccountMapper;
import com.cts.bms.dto.CustomerMapper;
import com.cts.bms.dto.InterestRateMapper;
import com.cts.bms.dto.LoanMapper;
import com.cts.bms.exception.BmsException;
import com.cts.bms.model.Account;
import com.cts.bms.model.Branch;
import com.cts.bms.model.Customer;

import com.cts.bms.model.InterestRate;
import com.cts.bms.model.Loan;

@Repository
public class AdminDaoImpl implements AdminDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Customer> viewAllCustomer() {
		String sqlQuery = "select * from customer where approved=1";
		List<Customer> customers = jdbcTemplate.query(sqlQuery, new CustomerMapper());
		return customers;
	}

	@Override
	public boolean createAccount(Account account) throws BmsException {
		String query = "insert into account (adhaar_no,account_type,active,ifsc_code,phonenumber,introducerAccountNo,nomineeAdhaarNo)"
				+ " values(?,?,?,?,?,?,?)";
		try {
			jdbcTemplate.update(query,
					new Object[] { account.getAdhaarNumber(), account.getAccType(), account.isActive(),
							account.getIfscCode(), account.getPhoneNumber(), account.getIntroducerAccountNo(),
							account.getNomineeAdhaarNo() });
			return true;
		} catch (DataAccessException e) {
			throw new BmsException("Insertion not possible");
		}
	}

	@Override
	public boolean enableAccount(long accountNo) throws BmsException {

		String sqlQuery = "update account set active = true where account_no =" + accountNo;
		try {
			jdbcTemplate.update(sqlQuery);
			return true;
		} catch (DataAccessException e) {
			throw new BmsException("Updation not possible");
		}
	}

	@Override
	public boolean disableAccount(long accountNo) throws BmsException {
		String sqlQuery = "update account set active = false where account_no =" + accountNo;
		try {
			jdbcTemplate.update(sqlQuery);
			return true;
		} catch (DataAccessException e) {
			throw new BmsException("Updation not possible");
		}
	}

	@Override
	public boolean approveLoanRequest(long loanId, boolean isLoanApproved) throws BmsException {
		String sqlQuery = "update loan set approved = ? where loan_id = ? ";
		
		try {
			jdbcTemplate.update(sqlQuery, new Object[] { isLoanApproved, loanId });
			return true;
		} catch (DataAccessException e) {
			throw new BmsException("Approve not Possible");
		}
	}

	@Override
	public boolean changeInterestRate(InterestRate irate) throws BmsException {
		String sqlQuery = "update interest_rate set rate = ? where plan=?";

		try {

			jdbcTemplate.update(sqlQuery, new Object[] { irate.getRate(), irate.getPlan() });
			return true;
		} catch (DataAccessException e) {
			throw new BmsException("Provide appropriate value");
		}

	}

	@Override
	public boolean addBankBranch(Branch branch) throws BmsException {
		String query = "insert into branch (ifsc_code,branch_name,address,pincode,branch_fund)" + " values(?,?,?,?,?)";
		try {
			jdbcTemplate.update(query, new Object[] { branch.getIfscCode(), branch.getBranchName(), branch.getAddress(),
					branch.getPincode(), branch.getBranchFund() });
			return true;
		} catch (DataAccessException e) {
			throw new BmsException("Insertion not possible");
		}
	}

	@Override
	public List<Loan> viewAllPendingLoanRequests() {
		String sqlQuery = "select * from loan where approved = 0";
		List<Loan> pendingLoans = jdbcTemplate.query(sqlQuery, new LoanMapper());
		return pendingLoans;
	}

	@Override
	public List<Loan> viewAllApprovedLoans() {
		String sqlQuery = "select * from loan where approved = 1";
		List<Loan> loans = jdbcTemplate.query(sqlQuery, new LoanMapper());
		return loans;
		
	}
 
	@Override
	public boolean deleteRejectedLoanRequest(long loanId) throws BmsException {
		String query = "delete from loan where loan_id = " + loanId;
		try {
			jdbcTemplate.update(query);
			return true;
		} catch (DataAccessException e) {
			throw new BmsException("Deletion of loan request not possible");
		}
	}

	@Override
	public List<Account> viewAllAccountsOfCustomer(Customer customer) {
		String sqlQuery = "select * from account where adhaar_no = "+customer.getAdhaarNo();
		List<Account> accounts = jdbcTemplate.query(sqlQuery, new AccountMapper());
		return accounts;
	}

	@Override
	public List<InterestRate> viewAllPlans() {
		
		String query = "select * from interest_rate";
		List<InterestRate> plans = jdbcTemplate.query(query, new InterestRateMapper());
		return plans;
	}
	
	

}
