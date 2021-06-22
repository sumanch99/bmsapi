package com.cts.bms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.exception.AccountNotFoundException;
import com.cts.bms.model.DebitCard;
import com.cts.bms.response.CustomJsonResponse;
import com.cts.bms.service.CardService;

@CrossOrigin(origins="*")
@RestController
public class DebitCardController { 
	
	@Autowired
	CardService cardService;
	
	@PostMapping("/customer/apply-debit-card")
	public ResponseEntity<Object> changeInterestRate(@RequestBody DebitCard card ) {
		try {
			if(cardService.applyForDebitCard(card)) {
				return CustomJsonResponse.generateResponse("Applied for Debit Card successfully", HttpStatus.OK, card);
			}
		} catch (AccountNotFoundException e) {
			return CustomJsonResponse.generateResponse("Account not found", HttpStatus.NOT_FOUND, card);
		}
		return CustomJsonResponse.generateResponse("DebitCard application not possible", HttpStatus.EXPECTATION_FAILED, card);
	}
	
	@PatchMapping("/admin/approve-debit-card")
	public ResponseEntity<Object> approveDebitCard(@RequestBody DebitCard card) {
		if(cardService.approveDebitCard(card)) {
			return CustomJsonResponse.generateResponse("Approved Debit Card successfully", HttpStatus.OK, card);
		}
		return CustomJsonResponse.generateResponse("DebitCard approval failed", HttpStatus.EXPECTATION_FAILED, card);
	}
	 
	@DeleteMapping("/admin/reject-debit-card")
	public ResponseEntity<Object> rejectDebitCard(@RequestBody DebitCard card) {
		if(cardService.rejectDebitCard(card)) {
			return CustomJsonResponse.generateResponse("Rejected Debit Card successfully", HttpStatus.OK, card);
		}
		return CustomJsonResponse.generateResponse("DebitCard rejection failed", HttpStatus.EXPECTATION_FAILED, card);
	}    
	
	@GetMapping("/admin/view-all-pending-debit-cards")
	public ResponseEntity<Object> viewAllPendingDebitCards() {
		List<DebitCard> cards = cardService.viewAllPendingDebitCards();
		if(cards!=null) {
			return CustomJsonResponse.generateResponse("Pending Debit Cards", HttpStatus.OK, cards);
		}
		return CustomJsonResponse.generateResponse("Pending Debit Cards cannot be fetched", HttpStatus.CONFLICT, null );
	}
	
	@GetMapping("/customer/view-debit-card/{accountNo}")
	public ResponseEntity<Object> viewDebitCard(@PathVariable long accountNo) {
		List<DebitCard> cards;
		try {
			cards = cardService.getDebitCard(accountNo);
		} catch (AccountNotFoundException e) {
			return CustomJsonResponse.generateResponse("Account number not found", HttpStatus.NOT_FOUND, null );
		}
		if(cards!=null) {
			return CustomJsonResponse.generateResponse(" Debit Cards", HttpStatus.OK, cards);
		}
		return CustomJsonResponse.generateResponse("Debit Cards cannot be fetched", HttpStatus.CONFLICT, null );
	}
}
