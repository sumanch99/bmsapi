package com.cts.bms.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cts.bms.dto.AccountMapper;
import com.cts.bms.dto.BranchMapper;
import com.cts.bms.dto.CustomerMapper;
import com.cts.bms.exception.BmsException;
import com.cts.bms.model.Account;
import com.cts.bms.model.Branch;
import com.cts.bms.model.Customer;

@Repository
public class BranchDaoImpl implements BranchDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public boolean depositIntoBranch(String ifscCode, double amount) throws BmsException {
		String query = "update branch set branch_fund = branch_fund+? where ifsc_code = ? ";
		try {
			jdbcTemplate.update(query, new Object[] { amount, ifscCode });
			return true;
		} catch (DataAccessException e) {
			throw new BmsException("Deposit to bank branch not possible");
		}
	}

	@Override
	public boolean deductFromBranch(String ifscCode, double amount) throws BmsException {
		String query = "update branch set branch_fund = branch_fund-? where ifsc_code = ? ";
		try {
			jdbcTemplate.update(query, new Object[] { amount, ifscCode });
			return true;
		} catch (DataAccessException e) {
			throw new BmsException("Duduction from bank branch not possible");
		}
	}

	@Override
	public List<Branch> getAllBranches() {
		String query = "select * from branch";
		List<Branch> branches = jdbcTemplate.query(query, new BranchMapper());
		return branches;
	}

}
