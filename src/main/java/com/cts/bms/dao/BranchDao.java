package com.cts.bms.dao;

import java.util.List;

import com.cts.bms.exception.BmsException;
import com.cts.bms.model.Branch;

public interface BranchDao {
	public boolean depositIntoBranch(String ifscCode, double amount) throws BmsException;

	public boolean deductFromBranch(String ifscCode, double amount) throws BmsException;
	
	public List<Branch> getAllBranches();
	
	public Branch getBranch(String ifscCode);  
}
