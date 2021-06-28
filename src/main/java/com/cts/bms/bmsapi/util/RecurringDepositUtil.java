package com.cts.bms.bmsapi.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RecurringDepositUtil {
	private static final Logger logger = LogManager.getLogger(RecurringDepositUtil.class);

	/* Estimation of the matured amount */
	public static double getMaturedAmount(double monthlyInvestment, double rateOfInterest, int tenure) {
		logger.info("START");
		int tenureInYears = tenure / 12;
		int quaters = tenureInYears * 4;

		// M=R[(1+i) (n-1)]/1-(1+i)(-1/3))
		/*
		 * Let's take an example of Arun, who is planning to invest INR 5,000 every
		 * month at 8% interest p.a. for 24 months or eight quarters. When we insert
		 * these values in the formula we get, 0.15/
		 * 
		 * M=5,000[(1+8/400) (8-1)]/1-(1+8/400)(-1/3))
		 * 
		 * M=INR 1,26,369
		 */
		System.out.println(monthlyInvestment + " " + rateOfInterest + " " + tenure);
		
		double a=(rateOfInterest/100)*(1/12);
		double b = (tenure*(tenure+1)/2)*a;
		double A = (monthlyInvestment*tenure)+(monthlyInvestment*b);
		double totalMaturdAmount = monthlyInvestment * ((1 + rateOfInterest / 400) * (quaters - 1))
				/ (1 - (1 + rateOfInterest / 400) * (-1 / 3));

		/*
		 * double totalMaturdAmount = monthlyInvestment Math.pow((1 + (rateOfInterest *
		 * 1.0 / 100) / quaters), (tenureInYears * 12));
		 */
		System.out.println(totalMaturdAmount);
		logger.info("END");
		return A;
	}

}
