package com.cts.bms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.model.Account;
import com.cts.bms.response.CustomJsonResponse;
import com.cts.bms.service.AccountService;

@RestController
@RequestMapping("/customer")
public class AccountController {

	@Autowired
	AccountService service;

	@GetMapping("/get-balance/{accountNo}")
	public ResponseEntity<Object> getBalance(@PathVariable long accountNo) {

		Account account = service.checkBalance(accountNo);
		if (account == null) {
			return CustomJsonResponse.generateResponse("Account not found", HttpStatus.NOT_FOUND, null);
		}
		return CustomJsonResponse.generateResponse("Account balance", HttpStatus.OK, account.getBalance());
	}

	@PostMapping("/deposit/{accountNo}")
	public ResponseEntity<Object> depositToAccount(@PathVariable long accountNo,@RequestBody double amount) {

		Account account = service.checkBalance(accountNo);
		if (account == null) {
			return CustomJsonResponse.generateResponse("Account not found", HttpStatus.NOT_FOUND, null);
		}
		if(service.depositIntoAccount(account, amount)) {
			return CustomJsonResponse.generateResponse("Amount deposited successfully", HttpStatus.OK, amount);
		}
		return CustomJsonResponse.generateResponse("Amount cannot be deposited", HttpStatus.OK, amount);
	}
	
	@PostMapping("/withdraw/{accountNo}")
	public ResponseEntity<Object> withdrawFromAccount(@PathVariable long accountNo,@RequestBody double amount) {

		Account account = service.checkBalance(accountNo);
		if (account == null) {
			return CustomJsonResponse.generateResponse("Account not found", HttpStatus.NOT_FOUND, null);
		}
		if(service.withdrawFromAccount(account, amount)) {
			return CustomJsonResponse.generateResponse("Amount withdrawn successfully", HttpStatus.OK, amount);
		}
		return CustomJsonResponse.generateResponse("Amount cannot be withdrawn", HttpStatus.OK, amount);
	}

}
