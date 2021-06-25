package com.cts.bms.bmsapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.bmsapi.exception.IntroducerNotFoundException;
import com.cts.bms.bmsapi.model.Account;
import com.cts.bms.bmsapi.model.Admin;
import com.cts.bms.bmsapi.model.Branch;
import com.cts.bms.bmsapi.model.Customer;
import com.cts.bms.bmsapi.model.Loan;
import com.cts.bms.bmsapi.model.Transaction;
import com.cts.bms.bmsapi.response.CustomJsonResponse;
import com.cts.bms.bmsapi.security.AuthenticationRequest;
import com.cts.bms.bmsapi.security.AuthenticationResponse;
import com.cts.bms.bmsapi.security.BmsUserDetailsService;
import com.cts.bms.bmsapi.security.JwtUtil;
import com.cts.bms.bmsapi.service.AccountService;
import com.cts.bms.bmsapi.service.AdminService;
import com.cts.bms.bmsapi.service.LoanService;
import com.cts.bms.bmsapi.service.TransactionService;



@CrossOrigin(origins="*")
@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private BmsUserDetailsService userDetailsService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	AdminService service;
	
	@Autowired
	LoanService loanService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	AccountService accountService;
	
	
	@PostMapping("/authenticate")
	public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		try {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getEmpId(), authenticationRequest.getPassword())
		);
		}catch(BadCredentialsException e) {
			throw new Exception("Incorrect username or password",e);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmpId());
		final String jwt = jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	@PostMapping("/create-new-admin")
	public ResponseEntity<Object> createAdmin(@RequestBody Admin admin) {
		if(service.createNewAdmin(admin)) {
			return CustomJsonResponse.generateResponse("Admin successfully signed up", HttpStatus.OK,admin);
		}
		return CustomJsonResponse.generateResponse("No Employee found", HttpStatus.NOT_FOUND,admin);
	}
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
	 
	@PatchMapping("/grant-loan") 
	public ResponseEntity<Object> grantLoan(@RequestBody Loan loan) {
		if(loanService.grantLoan(loan)) {
			return CustomJsonResponse.generateResponse("Loan successfully granted", HttpStatus.OK, loan);
		}
		return CustomJsonResponse.generateResponse("Loan cannot be granted", HttpStatus.EXPECTATION_FAILED, loan);
	}
	
	@DeleteMapping("/reject-loan/{loanId}") 
	public ResponseEntity<Object> rejectLoan(@PathVariable long loanId) {
		if(service.deleteLoanRequest(loanId)) {
			return CustomJsonResponse.generateResponse("Loan successfully rejected", HttpStatus.OK, loanId);
		}
		return CustomJsonResponse.generateResponse("Loan cannot be rejected", HttpStatus.EXPECTATION_FAILED, loanId);
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
	
	@GetMapping("/all-transactions") 
	public ResponseEntity<Object> getAllTransactions() {
		List<Transaction> transactions = transactionService.viewAllTransactions();
		if(transactions!=null) {
			return CustomJsonResponse.generateResponse("All transactions", HttpStatus.OK,transactions);
		}
		return CustomJsonResponse.generateResponse("Transactions cannot be fetched", HttpStatus.CONFLICT,transactions);
	}
	
	@GetMapping("/view-account/{accountNo}") 
	public ResponseEntity<Object> viewAccountWithAccountNo(@PathVariable long accountNo) {
		Account account = accountService.checkBalance(accountNo);
		if(account!=null) {
			return CustomJsonResponse.generateResponse("Account details fetched", HttpStatus.OK,account);
		}
		return CustomJsonResponse.generateResponse("Account doesnot exists", HttpStatus.OK,null);
	}
}
