package com.cts.bms.bmsapi.util;

import java.util.Random;

import com.cts.bms.bmsapi.model.Account;
import com.cts.bms.bmsapi.model.DebitCard;

public class DebitRule {
	
	private static final double MIN_BALANCE = 999 ;
	
	public static boolean isDebitCardPossible(DebitCard card, Account account) {
		if(card.getAccountNo()==account.getAccNo()) {
			if(account.getBalance()>MIN_BALANCE) {
				return true;
			}
		}
		return false;
	} 
	
	public static int generateCvv() {
		Random random = new Random();
		return random.nextInt(900) + 100;
	}
	
}
