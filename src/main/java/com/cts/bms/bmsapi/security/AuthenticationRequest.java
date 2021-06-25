package com.cts.bms.bmsapi.security;

import org.springframework.stereotype.Component;

public class AuthenticationRequest {
	private String empId;
	private String password;
	public AuthenticationRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AuthenticationRequest(String empId, String password) {
		super();
		this.empId = empId;
		this.password = password;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
