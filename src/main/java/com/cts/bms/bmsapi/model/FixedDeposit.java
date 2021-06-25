package com.cts.bms.bmsapi.model;

import java.util.Date;

import javax.validation.constraints.Min;

public class FixedDeposit {
	
	private int fixed_deposit_id;
	@Min(value = 2000, message = "An amount of aleast Rs.2000 is required")
	private double amount;
	@Min(value = 3,message="Tenure must be atleast 3 months")
	private int tenure;
	private long account_number;
	private double interest;
	private Date start_date;
	private boolean isMatured;
	private double matured_amount;

	public int getFixed_deposit_id() {
		return fixed_deposit_id;
	}

	public void setFixed_deposit_id(int fixed_deposit_id) {
		this.fixed_deposit_id = fixed_deposit_id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getTenure() {
		return tenure;
	}

	public void setTenure(int tenure) {
		this.tenure = tenure;
	}

	public long getAccount_number() {
		return account_number;
	}

	public void setAccount_number(long account_number) {
		this.account_number = account_number;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public boolean getIsMatured() {
		return isMatured;
	}

	public void setIsMatured(boolean isMatured) {
		this.isMatured = isMatured;
	}

	public double getMatured_amount() {
		return matured_amount;
	}

	public void setMatured_amount(double matured_amount) {
		this.matured_amount = matured_amount;
	}

	@Override
	public String toString() {
		return "Fixed_Deposit [fixed_deposit_id=" + fixed_deposit_id + ", amount=" + amount + ", tenure=" + tenure
				+ ", account_number=" + account_number + ", interest=" + interest + ", start_date=" + start_date
				+ ", isMatured=" + isMatured + ", matured_amount=" + matured_amount + "]";
	}
}
