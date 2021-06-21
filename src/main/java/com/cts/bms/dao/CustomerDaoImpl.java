package com.cts.bms.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cts.bms.dto.AccountMapper;
import com.cts.bms.dto.LoanMapper;
import com.cts.bms.exception.AdhaarNumberNotFoundException;
import com.cts.bms.exception.BmsException;
import com.cts.bms.model.Account;
import com.cts.bms.model.Customer;
import com.cts.bms.model.FixedDeposit;
import com.cts.bms.model.Loan;

@Repository
public class CustomerDaoImpl implements CustomerDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public Customer addCustomer(Customer customer) throws BmsException {
		
		String query = "insert into customer (userid,password,fname,lname,gender,address,town,district,state,pin,dateofbirth,"
				+ "emailid,phonenumber,adhaarno,approved)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			jdbcTemplate.update(query, new Object[] { customer.getUserId(), customer.getPassword(),
					customer.getFirstName(), customer.getLastName(), customer.getGender(), customer.getAddress(),
					customer.getTown(), customer.getDistrict(), customer.getState(), customer.getPinCode(),
					customer.getDateOfBirth(), customer.getEmailId(), customer.getPhoneNumber(), customer.getAdhaarNo(),
					customer.isApproved()

			});
			return customer;
		} catch (DataAccessException e) {
			throw new BmsException("Insertion not possible");
		}
	}

	@Override
	public Loan applyForLoan(Loan loan) throws BmsException {
		String query = "insert into loan (loan_code,account_no,amount,gurantor,tenure,start_date) values(?,?,?,?,?,?)";
		try {
			jdbcTemplate.update(query, new Object[] { loan.getLoanCode(), loan.getAccNo(), loan.getAmount(),
					loan.getGurantorAccNo(), loan.getTenure(), loan.getStartDate() });
			return loan;
		} catch (DataAccessException e) {
			throw new BmsException("Insertion not possible");
		}
	}

	@Override
	public boolean applyForFd(FixedDeposit fixedDeposit) throws BmsException {
		String query = "insert into fixed_deposit (amount,tenure,account_no,interest,start_date,is_matured,matured_amount) values(?,?,?,?,?,?)";
		try {
			jdbcTemplate.update(query,
					new Object[] { fixedDeposit.getAmount(), fixedDeposit.getTenure(), fixedDeposit.getAccount_number(),
							fixedDeposit.getInterest(), fixedDeposit.getStart_date(), fixedDeposit.getIsMatured(),
							fixedDeposit.getMatured_amount()

					});
			return true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			throw new BmsException("Fixed Deposit not possible");
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public Account getActiveAccount(long accountNo) throws BmsException {
		
		
		
		String query = "select * from account where account_no = ? and active = true";

		try {
			
			Account account = jdbcTemplate.queryForObject(query,new Object[]{accountNo},new AccountMapper());
			return account;

		} catch (DataAccessException e) {
			throw new BmsException("No Account available");
		}
	}
	
	@Override
	public List<Loan> viewAllApprovedLoans(String userId) {
		String sqlQuery = 
		"select * from loan " 
		+"where account_no in "
		+"(select account_no from account inner join customer where userid='"+userId+"') "
		+"and approved=true";
		
		List<Loan> approvedLoans = jdbcTemplate.query(sqlQuery, new LoanMapper());
		return approvedLoans;
	}

}
