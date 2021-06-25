package com.cts.bms.bmsapi.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cts.bms.bmsapi.dto.AccountMapper;
import com.cts.bms.bmsapi.dto.CustomerMapper;
import com.cts.bms.bmsapi.dto.DebitCardMapper;
import com.cts.bms.bmsapi.dto.FixedDepositMapper;
import com.cts.bms.bmsapi.dto.LoanMapper;
import com.cts.bms.bmsapi.dto.RateValueMapper;
import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.model.Account;
import com.cts.bms.bmsapi.model.Customer;
import com.cts.bms.bmsapi.model.DebitCard;
import com.cts.bms.bmsapi.model.FixedDeposit;
import com.cts.bms.bmsapi.model.Loan;



@Repository
public class CustomerDaoImpl implements CustomerDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public Customer loadCustomer(String username) throws BmsException {
		String query = "select * from customer where userid = ?";
		try {
			Customer customer  = jdbcTemplate.queryForObject(query, new CustomerMapper(), new Object[] {
				username	
			});
			return customer;
		}catch(DataAccessException e) {
			throw new BmsException("Selection not possible");
		}
	}

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
	public double getFdInterestRate() {
		String query = "select * from interest_rate where plan = ?";
		double rate = jdbcTemplate.queryForObject(query, new RateValueMapper(), new Object[] {
			"FD"	
		});
		return rate;
	}
	
	@Override
	public boolean applyForFd(FixedDeposit fixedDeposit) throws BmsException {
		String query = "insert into fixed_deposit (account_no,amount,tenure,interest,matured_amount) values(?,?,?,?,?)";
		try {
			jdbcTemplate.update(query,
					new Object[] { 
							fixedDeposit.getAccount_number(),
							fixedDeposit.getAmount(),
							fixedDeposit.getTenure(),
							fixedDeposit.getInterest(),
							fixedDeposit.getMatured_amount()
					});
			return true;
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new BmsException("Fixed Deposit not possible");
		}
	}
	
	@Override
	public List<FixedDeposit> getAllFixedDeposits(Account account) throws BmsException {
		String query = "select * from fixed_deposit where account_no = ?";
		try {
			return jdbcTemplate.query(query,new FixedDepositMapper(),new Object[] {
				account.getAccNo()
			});
		}catch(DataAccessException e) {
			throw new BmsException("Fixed Deposit selection not possible");
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
		+"and approved=true order by loan_id desc ";
		
		List<Loan> approvedLoans = jdbcTemplate.query(sqlQuery, new LoanMapper());
		return approvedLoans;
	}

	@Override
	public boolean applyForDebitCard(DebitCard debitCard) throws BmsException {
		
		String query = "insert into debit_card(pin, cvv_no, account_no) values(?,?,?)";
		try {
			jdbcTemplate.update(query, new Object[] {
					debitCard.getPin(),
					debitCard.getCvvNo(),
					debitCard.getAccountNo()
			});
			return true;
		}catch(DataAccessException e) {
			throw new BmsException("Insertion falied");
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public List<DebitCard> viewAllApprovedDebitCards(Account account) throws BmsException{
		String query = "select * from debit_card where approved = ? and account_no = ?";
		try {
			List<DebitCard> approvedCards = jdbcTemplate.query(query, new Object[] {
					true,
					account.getAccNo()
			},new DebitCardMapper());
			return approvedCards;
		}catch(DataAccessException e) {
			throw new BmsException("Selection falied");
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public DebitCard getDebitCard(long cardNo,int cvvNo,int pin) {
		String query = "select * from debit_card where card_no=? and pin=? and cvv_no=? and approved = ?";
		try {
			DebitCard debitCard = jdbcTemplate.queryForObject(query, new Object[] {
					cardNo,
					pin,
					cvvNo,
					true
			}, new DebitCardMapper());
			return debitCard;
		}catch(DataAccessException e) {
			return null;
		}
	}

}
