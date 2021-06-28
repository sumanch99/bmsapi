package com.cts.bms.bmsapi.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.cts.bms.bmsapi.service.AccountService;
import com.cts.bms.bmsapi.service.AdminService;
import com.cts.bms.bmsapi.service.LoanService;
import com.cts.bms.bmsapi.service.TransactionService;




@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/admin")
public class AdminController {
	private static final Logger LOGGER=LogManager.getLogger( AdminController.class);
	
	@Autowired
	AdminService service;
	
	@Autowired
	LoanService loanService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	AccountService accountService;
	
	
	@PostMapping("/create-new-admin")
	public ResponseEntity<Object> createAdmin(@RequestBody Admin admin) {
		LOGGER.info("START");
		String encodedPassword = new BCryptPasswordEncoder().encode(admin.getPassword());
		admin.setPassword(encodedPassword);	
		if(service.createNewAdmin(admin)) {
			LOGGER.info("END");
			return CustomJsonResponse.generateResponse("Admin successfully signed up", HttpStatus.OK,admin);
		}
		LOGGER.error("No Employee found");
		return CustomJsonResponse.generateResponse("No Employee found", HttpStatus.NOT_FOUND,admin);
	}
	@GetMapping("/customer-list") 
	public ResponseEntity<Object> getAllCustomers() {
		LOGGER.info("START");
		List<Customer> customerList = service.getAllCustomers();
		LOGGER.info("END");
		return CustomJsonResponse.generateResponse("All approved customers", HttpStatus.OK,customerList);
	}
	
	@PostMapping("/add-new-branch")
	public ResponseEntity<Object> addNewBranch(@RequestBody Branch branch ) {
		LOGGER.info("START");
		if(service.addBankBranch(branch)) {
			LOGGER.info("END");
			return CustomJsonResponse.generateResponse("Branch creation Successfull", HttpStatus.CREATED, branch);
		}   LOGGER.error("Branch creation Failed");
			return CustomJsonResponse.generateResponse("Branch creation Failed", HttpStatus.EXPECTATION_FAILED, branch);
	}
	
	@PostMapping("/create-account")
	public ResponseEntity<Object> createNewAccount(@RequestBody Account account ) {
		boolean flag;
		LOGGER.info("START");
		try {
			flag = service.createNewAccount(account);
			if(flag) {
				LOGGER.info("END");
				return CustomJsonResponse.generateResponse("Account created", HttpStatus.OK, account);
			}
			LOGGER.error("Account creation unsuccessfull");
			return CustomJsonResponse.generateResponse("Account creation unsuccessfull", HttpStatus.EXPECTATION_FAILED, account);
		} catch (IntroducerNotFoundException e) {
			LOGGER.error("Account can not be created");
			return CustomJsonResponse.generateResponse("Account can not be created", HttpStatus.NOT_FOUND, account);
		}
		
		
	}
	
	@GetMapping("/get-all-accounts")
	public ResponseEntity<Object> getAllAccounts() {
		LOGGER.info("START");
		List<Account> accounts = service.getAllAccounts();
		if(accounts!=null) {
			LOGGER.info("END");
			return CustomJsonResponse.generateResponse("All accounts", HttpStatus.OK, accounts);
		}
		LOGGER.error("Accounts can not be fetched");
		return CustomJsonResponse.generateResponse("Accounts can not be fetched", HttpStatus.NOT_FOUND, null);
	}
	
	@PutMapping("/disable-account/{accountNo}")
	public ResponseEntity<Object> disableAccount(@PathVariable long accountNo) {
		LOGGER.info("START");
		if(service.disableAccount(accountNo)) {
			LOGGER.info("END");
			return CustomJsonResponse.generateResponse("Account disabled successfully", HttpStatus.OK, accountNo);
		}
		LOGGER.error("ACCOUNT DISABLE UNSUCCESSFULL");
		return CustomJsonResponse.generateResponse("Account disable unsuccessfull", HttpStatus.EXPECTATION_FAILED, accountNo);
	}
	
	@PutMapping("/enable-account/{accountNo}")
	public ResponseEntity<Object> enableAccount(@PathVariable long accountNo) {
		LOGGER.info("START");
		if(service.enableAccount(accountNo)) {
			LOGGER.info("END");
			return CustomJsonResponse.generateResponse("Account enabled successfully", HttpStatus.OK, accountNo);
		}
		LOGGER.error("Account enable unsuccessfull");
		return CustomJsonResponse.generateResponse("Account enable unsuccessfull", HttpStatus.EXPECTATION_FAILED, accountNo);
	}
	   
	@PutMapping("/grant-loan/{loanId}") 
	public ResponseEntity<Object> grantLoan(@PathVariable long loanId) {
		LOGGER.info("START");
		if(loanService.grantLoan(loanId)) {
			LOGGER.info("END");
			return CustomJsonResponse.generateResponse("Loan successfully granted", HttpStatus.OK, loanId);
		} LOGGER.error("Loan cannot be granted");
		return CustomJsonResponse.generateResponse("Loan cannot be granted", HttpStatus.EXPECTATION_FAILED, loanId);
	} 
	
	@DeleteMapping("/reject-loan/{loanId}") 
	public ResponseEntity<Object> rejectLoan(@PathVariable long loanId) {
		LOGGER.info("START");
		if(service.deleteLoanRequest(loanId)) {
			LOGGER.info("END");
			return CustomJsonResponse.generateResponse("Loan successfully rejected", HttpStatus.OK, loanId);
		}
		LOGGER.error("Loan cannot be rejected");
		return CustomJsonResponse.generateResponse("Loan cannot be rejected", HttpStatus.EXPECTATION_FAILED, loanId);
	}
	
	@GetMapping("/pending-loans") 
	 
	public ResponseEntity<Object> getAllPendingLoanRequest() {
		LOGGER.info("START");
		List<Loan> pendingLoans = service.getAllPendingLoanRequests();
		LOGGER.info("END");
		return CustomJsonResponse.generateResponse("All pending loan requests", HttpStatus.OK,pendingLoans);
	}
	
	@GetMapping("/all-approved-loans") 
	public ResponseEntity<Object> getAllApprovedLoans() {
		LOGGER.info("START");
		List<Loan> loans = loanService.getAllApprovedLoans();
		LOGGER.info("END");
		return CustomJsonResponse.generateResponse("All approved loans", HttpStatus.OK,loans);
	}
	
	@GetMapping("/all-transactions") 
	public ResponseEntity<Object> getAllTransactions() {
		LOGGER.info("START");
		List<Transaction> transactions = transactionService.viewAllTransactions();
		if(transactions!=null) {
			LOGGER.info("END");
			return CustomJsonResponse.generateResponse("All transactions", HttpStatus.OK,transactions);
		}
		LOGGER.error("Transactions cannot be fetched");
		return CustomJsonResponse.generateResponse("Transactions cannot be fetched", HttpStatus.CONFLICT,transactions);
	}
	
	@GetMapping("/view-account/{accountNo}") 
	public ResponseEntity<Object> viewAccountWithAccountNo(@PathVariable long accountNo) {
		LOGGER.info("START");
		Account account = accountService.checkBalance(accountNo);
		if(account!=null) {
			LOGGER.info("END");
			return CustomJsonResponse.generateResponse("Account details fetched", HttpStatus.OK,account);
		}
		LOGGER.warn("BAD REQUEST");
		return CustomJsonResponse.generateResponse("Account doesnot exists", HttpStatus.OK,null);
	}
}
