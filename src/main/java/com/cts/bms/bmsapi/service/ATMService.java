package com.cts.bms.bmsapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.bms.bmsapi.dao.AccountDao;
import com.cts.bms.bmsapi.exception.BmsException;
import com.cts.bms.bmsapi.model.Account;
import com.cts.bms.bmsapi.model.DebitCard;


@Service
public class ATMService {

	@Autowired
	CardService service;

	@Autowired
	AccountDao dao;

	public double getDebitCardBalance(DebitCard card) throws BmsException {
		card = service.getDebitCard(card.getCardNo(), card.getCvvNo(), card.getPin());
		if(card==null) {
			throw new BmsException("ATM card is not valid");
		}
		if(service.getDebitCard(card.getCardNo(), card.getCvvNo(), card.getPin())!=null) {
			Account account = dao.getAccountWithAccountNumber(card.getAccountNo());
			if(account!=null) {
				return account.getBalance();
			}
		}
		throw new BmsException("ATM card is not valid");
	}
	
	public boolean validateDebitCardWithDraw(DebitCard card,double amount) {
		card = service.getDebitCard(card.getCardNo(), card.getCvvNo(), card.getPin());
		if(card==null) {
			return false;
		}
		if(service.getDebitCard(card.getCardNo(), card.getCvvNo(), card.getPin())!=null) {
			Account account = dao.getAccountWithAccountNumber(card.getAccountNo());
			if(account!=null) {
				if(account.getBalance()>amount) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean withdrawWithDebitCard(DebitCard card,double amount) {
		card = service.getDebitCard(card.getCardNo(), card.getCvvNo(), card.getPin());
		Account account = dao.getAccountWithAccountNumber(card.getAccountNo());
		if(account!=null) {
			try {
				return dao.withDrawThroughDebitCard(account, amount);
			} catch (BmsException e) {
				return false;
			}
		}
		return false;
	}
	
}
