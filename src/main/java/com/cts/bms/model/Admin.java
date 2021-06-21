package com.cts.bms.model;
import javax.validation.constraints.NotNull;
public class Admin {
	@NotNull(message="user id is required")
   private String userId;
	@NotNull(message="password is required")
   private String password;
	@NotNull(message="ifscCode is required")
   private String ifscCode;
   
public Admin(String userId, String password, String ifscCode) {
	super();
	this.userId = userId;
	this.password = password;
	this.ifscCode = ifscCode;
}

public String getUserId() {
	return userId;
}

public void setUserId(String userId) {
	this.userId = userId;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public String getIfscCode() {
	return ifscCode;
}

public void setIfscCode(String ifscCode) {
	this.ifscCode = ifscCode;
}

   
}
