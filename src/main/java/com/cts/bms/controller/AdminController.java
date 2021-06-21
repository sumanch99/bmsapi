package com.cts.bms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.exception.IntroducerNotFoundException;
import com.cts.bms.model.Account;
import com.cts.bms.model.Branch;
import com.cts.bms.model.Customer;
import com.cts.bms.model.InterestRate;
import com.cts.bms.model.Loan;
import com.cts.bms.response.CustomJsonResponse;
import com.cts.bms.service.AdminService;
import com.cts.bms.service.LoanService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	AdminService service;
	
	@Autowired
	LoanService loanService;
	
	@GetMapping("/customer-list") 
	public ResponseEntity<Object> getAllCustomers() {
		List<Customer> customerList = service.getAllCustomers();
		return CustomJsonResponse.generateResponse("All approved customers", HttpStatus.OK,customerList);
	}
	
	@PostMapping("/add-new-branch")
	public ResponseEntity<Object> addNewBranch(@RequestBody Branch branch ) {
		
		if(service.addBankBranch(branch)) {
			return CustomJsonResponse.generateResponse("Branch creation Successfull", HttpStatus.CREATED, branch);
		}
			return CustomJsonResponse.generateResponse("Branch creation Failed", HttpStatus.EXPECTATION_FAILED, branch);
	}
	
	@PostMapping("/create-account")
	public ResponseEntity<Object> createNewAccount(@RequestBody Account account ) {
		boolean flag;
		try {
			flag = service.createNewAccount(account);
			if(flag) {
				return CustomJsonResponse.generateResponse("Account created", HttpStatus.OK, account);
			}
			return CustomJsonResponse.generateResponse("Account creation unsuccessfull", HttpStatus.EXPECTATION_FAILED, account);
		} catch (IntroducerNotFoundException e) {
			return CustomJsonResponse.generateResponse("Introducer not found", HttpStatus.NOT_FOUND, account);
		}
		
		
	}
	
	@PatchMapping("/disable-account")
	public ResponseEntity<Object> disableAccount(@RequestBody Account account) {
		if(service.disableAccount(account)) {
			return CustomJsonResponse.generateResponse("Account disabled successfully", HttpStatus.OK, account);
		}
		return CustomJsonResponse.generateResponse("Account disable unsuccessfull", HttpStatus.EXPECTATION_FAILED, account);
	}
	
	@PatchMapping("/enable-account")
	public ResponseEntity<Object> enableAccount(@RequestBody Account account) {
		if(service.enableAccount(account)) {
			return CustomJsonResponse.generateResponse("Account enabled successfully", HttpStatus.OK, account);
		}
		return CustomJsonResponse.generateResponse("Account enable unsuccessfull", HttpStatus.EXPECTATION_FAILED, account);
	}
	 
	@PatchMapping("/grant-loan/{ifscCode}") 
	public ResponseEntity<Object> grantLoan(@RequestBody Loan loan,@PathVariable String ifscCode) {
		if(loanService.grantLoan(loan, ifscCode)) {
			return CustomJsonResponse.generateResponse("Loan successfully granted", HttpStatus.OK, loan);
		}
		return CustomJsonResponse.generateResponse("Loan cannot be granted", HttpStatus.EXPECTATION_FAILED, loan);
	}
	
	@PatchMapping("/reject-loan/{ifscCode}") 
	public ResponseEntity<Object> rejectLoan(@RequestBody Loan loan,@PathVariable(required = false) String ifscCode) {
		if(service.deleteLoanRequest(loan.getLoan_id())) {
			return CustomJsonResponse.generateResponse("Loan successfully rejected", HttpStatus.OK, loan);
		}
		return CustomJsonResponse.generateResponse("Loan cannot be rejected", HttpStatus.EXPECTATION_FAILED, loan);
	}
	
	@GetMapping("/pending-loans") 
	public ResponseEntity<Object> getAllPendingLoanRequest() {
		List<Loan> pendingLoans = service.getAllPendingLoanRequests();
		return CustomJsonResponse.generateResponse("All pending loan requests", HttpStatus.OK,pendingLoans);
	}
	
	@GetMapping("/all-approved-loans") 
	public ResponseEntity<Object> getAllApprovedLoans() {
		List<Loan> loans = loanService.getAllApprovedLoans();
		return CustomJsonResponse.generateResponse("All approved loans", HttpStatus.OK,loans);
	}
}
