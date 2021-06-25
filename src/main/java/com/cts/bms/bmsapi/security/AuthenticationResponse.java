package com.cts.bms.bmsapi.security;

public class AuthenticationResponse {
	private String jwt;
	
	

	public AuthenticationResponse(String jwt) {
		super();
		this.jwt = jwt;
	}



	public String getJwt() {
		return jwt;
	}
	
}
