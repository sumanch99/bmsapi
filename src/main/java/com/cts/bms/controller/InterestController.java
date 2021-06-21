package com.cts.bms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.bms.model.InterestRate;
import com.cts.bms.response.CustomJsonResponse;
import com.cts.bms.service.AdminService;
import com.cts.bms.service.PlanService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/admin")
public class InterestController {
	@Autowired
	AdminService service;
	
	@Autowired
	PlanService planService;
	
	@PostMapping("/change-interest-rate")
	public ResponseEntity<Object> changeInterestRate(@RequestBody InterestRate irate ) {
		if(service.changeInterestRate(irate)) {
			return CustomJsonResponse.generateResponse("Interest rate changed", HttpStatus.OK, irate);
		}
		return CustomJsonResponse.generateResponse("Interest rate change unsuccessfull", HttpStatus.EXPECTATION_FAILED, irate);
	}
	
	@GetMapping("/view-interest-plans")
	public ResponseEntity<Object> viewAllPlans() {
		List<InterestRate> plans = planService.viewAllPlans();
		if(plans.isEmpty()) {
			return CustomJsonResponse.generateResponse("No Plans available", HttpStatus.OK,null);
		}
		return CustomJsonResponse.generateResponse("Plans fetched", HttpStatus.OK, plans);
	}
}
