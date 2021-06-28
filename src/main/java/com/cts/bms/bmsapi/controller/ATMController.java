package com.cts.bms.bmsapi.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.model.DebitCard;
import com.cts.bms.bmsapi.response.CustomJsonResponse;
import com.cts.bms.bmsapi.service.ATMService;





@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
public class ATMController {
	
	private static final Logger LOGGER=LogManager.getLogger( ATMController.class);
	@Autowired
	ATMService atmService;
	
	@PostMapping("/atm-corner/withdraw/{amount}")
	public ResponseEntity<Object> withdrawFromAccount(@PathVariable double amount,@RequestBody DebitCard card) {
		LOGGER.info("START");
		if(atmService.validateDebitCardWithDraw(card, amount)) {
			if(atmService.withdrawWithDebitCard(card, amount)) {
				LOGGER.info("END");
				return CustomJsonResponse.generateResponse("Amount successfully withdrawn", HttpStatus.OK, amount);
			}
			LOGGER.error("Process failed");
			return CustomJsonResponse.generateResponse("Process failed", HttpStatus.BAD_GATEWAY,card);
		}
		LOGGER.warn("BAD REQUEST");
		return CustomJsonResponse.generateResponse("Process cannot be completed. Invalid input.", HttpStatus.BAD_REQUEST, null);
	}
	
	@PostMapping("/atm-corner/check-balance")
	public ResponseEntity<Object> checkBalance(@RequestBody DebitCard card) {
		LOGGER.info("START");
		try {
			double balance  = atmService.getDebitCardBalance(card);
			LOGGER.info("END");
			return CustomJsonResponse.generateResponse("Your account balance is", HttpStatus.OK, balance);
		}catch(BmsException e) {
			LOGGER.warn("BAD REQUEST");
			return CustomJsonResponse.generateResponse("Incorrect Card Details", HttpStatus.BAD_REQUEST, null);
		}
		
	}
	
	
}
