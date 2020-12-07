package com.bank.trn.services;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bank.trn.dto.TransactionDTO;
import com.bank.trn.exceptions.CommonAPIException;

/**
 * Services for region.
 *
 * @author kumar-sand
 */
public interface TransactionService {

	/**
	 * Deposit.
	 *
	 * @param account the account
	 * @param amount the amount
	 * @return the string
	 */
	public String deposit(String account, BigDecimal amount) throws CommonAPIException;
	
	/**
	 * Withdrawl.
	 *
	 * @param account the account
	 * @param amount the amount
	 * @return the string
	 */
	public String withdrawal(String account, BigDecimal amount) throws CommonAPIException;
	
	/**
	 * Transfer.
	 *
	 * @param fromAccount the from account
	 * @param toAccount the to account
	 * @param amount the amount
	 * @return the string
	 */
	public String transfer(String fromAccount, String toAccount, BigDecimal amount) throws CommonAPIException;
	
	/**
	 * Enquiry.
	 *
	 * @param query the query
	 * @param pageable the pageable
	 * @return the page
	 */
	public Page<TransactionDTO> enquiry(String query, Pageable pageable);
	
	/**
	 * Enquiry by account.
	 *
	 * @param query the query
	 * @param pageable the pageable
	 * @return the page
	 */
	public Page<TransactionDTO> enquiryByAccount(String account, String query, Pageable pageable) throws CommonAPIException;
	
	/**
	 * Enquiry by customer.
	 *
	 * @param query the query
	 * @param pageable the pageable
	 * @return the page
	 */
	public Page<TransactionDTO> enquiryByCustomer(String cif, String query, Pageable pageable) throws CommonAPIException;
	
	/**
	 * Enquiry by transaction id.
	 *
	 * @param query the query
	 * @param pageable the pageable
	 * @return the page
	 */
	public Page<TransactionDTO> enquiryByTransactionId(String transactionId, String query, Pageable pageable);
}
