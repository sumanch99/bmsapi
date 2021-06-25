package com.cts.bms.bmsapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.bms.bmsapi.dao.BranchDao;
import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.model.Branch;



@Service
public class BranchService {
	@Autowired
	BranchDao branchDao;
	
	public List<Branch> getAllBranches(){
		return branchDao.getAllBranches();
	}
	
	public boolean depositIntoBranch(String ifscCode,double amount){
		
		try {
			return branchDao.depositIntoBranch(ifscCode, amount);
		} catch (BmsException e) {
			return false;
		}
		
	}
	
	public boolean deductFromBranch(String ifscCode,double amount){
		try {
			return branchDao.deductFromBranch(ifscCode, amount);
		} catch (BmsException e) {
			return false;
		}
	}
	
}
