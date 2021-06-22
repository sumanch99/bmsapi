package com.cts.bms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.bms.dao.AccountDao;
import com.cts.bms.dao.AdminDao;
import com.cts.bms.dao.CustomerDao;
import com.cts.bms.exception.AccountNotFoundException;
import com.cts.bms.exception.BmsException;
import com.cts.bms.model.Account;
import com.cts.bms.model.DebitCard;
import com.cts.bms.util.DebitRule;

@Service
public class CardService {
	
	@Autowired 
	CustomerDao dao;
	@Autowired
	AccountDao accountDao;
	@Autowired
	AdminDao adminDao;
	
	public boolean applyForDebitCard(DebitCard card) throws AccountNotFoundException {
		try {
			Account account = accountDao.getAccountWithAccountNumber(card.getAccountNo());
			if(account==null) {
				throw new AccountNotFoundException("account not found");
			}
			if(DebitRule.isDebitCardPossible(card, account)) {
				card.setCvvNo(DebitRule.generateCvv());
				return dao.applyForDebitCard(card);
			}
		}catch(BmsException e) {
			return false;
		}
		return false;
	}
	
	public boolean approveDebitCard(DebitCard card) {
		try {
			if(adminDao.approveDebitCard(card)) {
				return true;
			}
		}catch(BmsException e) {
			return false;
		}
		return false;
	}
	
	public boolean rejectDebitCard(long cardNo) {
		try {
			if(adminDao.deleteRejectedDebitCard(cardNo)) {
				return true;
			}
		}catch(BmsException e) {
			return false;
		}
		return false;
	}
	
	public List<DebitCard> viewAllPendingDebitCards(){
		try {
			List<DebitCard> cards = adminDao.viewAllPendingDebitCards();
			return cards;
		}catch(BmsException e) {
			return null;
		}
	}
	
	public List<DebitCard> getDebitCard(long accountNo) throws AccountNotFoundException{
		try {
			Account account = accountDao.getAccountWithAccountNumber(accountNo);
			if(account==null) {
				throw new AccountNotFoundException("account not found");
			}
			List<DebitCard> cards = dao.viewAllApprovedDebitCards(account);
			return cards;
		}catch(BmsException e) {
			return null;
		}
	}
}
