package com.cts.bms.util;

import java.util.Date;

import com.cts.bms.model.FixedDeposit;

public class FixedDepositUtility {
	
	public void pay(FixedDeposit fd)
	{
		if(fd.getIsMatured()==false)
		{
			//Get the number of months left
		   int monthsLeft = fd.getTenure();
		  
		   /*
		    Compounded monthly. 
		    Amount = P*(1 + r/(the number of times that interest is compounded per unit t *100))^(the number of times that interest is compounded per unit t * tenureInYears) 
		    For monthly calculations compounding frequency = 12
		    */
		  double rateOfInterest = fd.getInterest();
		   double amount = fd.getAmount() * Math.pow((1+rateOfInterest/(100*12)),1);
		 
		   fd.setAmount(amount);
		   monthsLeft = monthsLeft-1;
		   fd.setTenure(monthsLeft);
		  
		   if(monthsLeft == 0)
		   {
			   fd.setIsMatured(true);
			   fd.setMatured_amount(amount);
		   }
		   
		}
	}
	/*Estimation of the matured amount*/
	public double getMaturedAmount(double principalAmount,double rateOfInterest,int tenure) {
		int tenureInYears = tenure/12;
		double totalMaturdAmount = principalAmount*Math.pow( (1 + rateOfInterest / (100*12) ),(tenureInYears*12));
		return  totalMaturdAmount;
	}
	/*Get amount after maturity*/
	public double getMaturedAmount(FixedDeposit fd)
	{
		if(fd.getIsMatured())
			return fd.getMatured_amount();
		else
			return 0.0;
		
	}
}
