package com.cts.bms.bmsapi.controller;

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

import com.cts.bms.bmsapi.model.FixedDeposit;
import com.cts.bms.bmsapi.response.CustomJsonResponse;
import com.cts.bms.bmsapi.service.PlanService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/customer")
public class DepositController {
	
	@Autowired
	PlanService service;
	
	@PostMapping("/apply-fd")
	public ResponseEntity<Object> applyForFd(@RequestBody FixedDeposit fd) {
		if(service.generateFixedDeposit(fd)) {
			return CustomJsonResponse.generateResponse("Fixed deposit generated successfully", HttpStatus.OK, fd);
		}
		return CustomJsonResponse.generateResponse("Fixed deposit cannot be generated.", HttpStatus.BAD_REQUEST, null);
	}
	
	@GetMapping("/view-my-fd/{accountNo}")
	public ResponseEntity<Object> viewMyFd(@PathVariable long accountNo) {
		List<FixedDeposit> fds = service.getAllFixedDeposits(accountNo);
		if(fds!=null) {
			return CustomJsonResponse.generateResponse("Fixed deposits", HttpStatus.OK, fds);
		}
		return CustomJsonResponse.generateResponse("Fixed deposits not found", HttpStatus.NOT_FOUND, null);
	}
	
}
