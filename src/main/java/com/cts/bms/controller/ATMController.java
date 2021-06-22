package com.cts.bms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.exception.BmsException;
import com.cts.bms.model.Account;
import com.cts.bms.model.DebitCard;
import com.cts.bms.response.CustomJsonResponse;
import com.cts.bms.service.ATMService;


@CrossOrigin(origins="*")
@RestController
public class ATMController {
	
	@Autowired
	ATMService atmService;
	
	@PostMapping("/atm-corner/withdraw/{amount}")
	public ResponseEntity<Object> withdrawFromAccount(@PathVariable double amount,@RequestBody DebitCard card) {
		if(atmService.validateDebitCardWithDraw(card, amount)) {
			if(atmService.withdrawWithDebitCard(card, amount)) {
				return CustomJsonResponse.generateResponse("Amount successfully withdrawn", HttpStatus.OK, amount);
			}
			return CustomJsonResponse.generateResponse("Process failed", HttpStatus.BAD_GATEWAY,card);
		}
		return CustomJsonResponse.generateResponse("Process cannot be completed. Invalid input.", HttpStatus.BAD_REQUEST, null);
	}
	
	@PostMapping("/atm-corner/check-balance")
	public ResponseEntity<Object> checkBalance(@RequestBody DebitCard card) {
		try {
			double balance  = atmService.getDebitCardBalance(card);
			return CustomJsonResponse.generateResponse("Your account balance is", HttpStatus.OK, balance);
		}catch(BmsException e) {
			return CustomJsonResponse.generateResponse("Incorrect Card Details", HttpStatus.BAD_REQUEST, null);
		}
		
	}
	
}
