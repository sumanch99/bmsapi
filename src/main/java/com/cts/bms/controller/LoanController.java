package com.cts.bms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.model.Loan;
import com.cts.bms.response.CustomJsonResponse;
import com.cts.bms.service.AdminService;
import com.cts.bms.service.LoanService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/customer")
public class LoanController {
	
	@Autowired
	LoanService service;
	
	@Autowired
	AdminService adminService;
	
	@PostMapping("/apply-loan")
	public ResponseEntity<Object> applyForLoan(@RequestBody Loan loan) {
		if(service.applyForLoan(loan)) {
			return CustomJsonResponse.generateResponse("Applying for loan successful", HttpStatus.OK, loan);
		}
		return CustomJsonResponse.generateResponse("Applying for loan not possible", HttpStatus.EXPECTATION_FAILED, loan);
	}
	
	@GetMapping("/view-my-loans/{userId}") 
	public ResponseEntity<Object> getAllLoans(@PathVariable String userId) {
		List<Loan> loans = adminService.getAllApprovedLoans(userId);
		return CustomJsonResponse.generateResponse("All loans", HttpStatus.OK,loans);
	}
}
