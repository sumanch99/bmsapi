package com.cts.bms.util;

import java.util.Random;

import com.cts.bms.model.Account;
import com.cts.bms.model.DebitCard;

public class DebitRule {
	
	private static final double MIN_BALANCE = 1000 ;
	
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
