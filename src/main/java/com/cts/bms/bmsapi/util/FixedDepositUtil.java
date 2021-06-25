package com.cts.bms.bmsapi.util;

public class FixedDepositUtil {
	
	
	/*Estimation of the matured amount*/
	public static double getMaturedAmount(double principalAmount,double rateOfInterest,int tenure) {
		int tenureInYears = tenure/12;
		double totalMaturdAmount = principalAmount*Math.pow( (1 + rateOfInterest / (100*12) ),(tenureInYears*12));
		return  totalMaturdAmount;
	}
	
}
