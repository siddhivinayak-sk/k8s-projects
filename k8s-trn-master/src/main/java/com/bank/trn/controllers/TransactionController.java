package com.bank.trn.controllers;

import java.math.BigDecimal;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.trn.constants.CommonMessageConstants;
import com.bank.trn.constants.URLConstants;
import com.bank.trn.exceptions.CommonAPIException;
import com.bank.trn.services.TransactionService;
import com.bank.trn.utils.AppWebUtils;

@RestController
@RequestMapping(URLConstants.RESOURCE_TRN + URLConstants.VERSION)
public class TransactionController {
	
	public static final Logger LOGGER = Logger.getLogger(TransactionController.class.getName());
	
	@Autowired
	private TransactionService transactionService;

	@GetMapping(URLConstants.RESOURCE_TRN_DEPOSIT)
	public ResponseEntity<?> deposit(@RequestParam(required = true) String account, @RequestParam(required = true) BigDecimal amount) throws CommonAPIException {
		LOGGER.info("Deposit - Calling");
		return AppWebUtils.buildResponse(CommonMessageConstants.SUCCESS_DEPOSITED, HttpStatus.OK, transactionService.deposit(account, amount));
	}
	
	@GetMapping(URLConstants.RESOURCE_TRN_WITHDRAWAL)
	public ResponseEntity<?> withdrawal(@RequestParam(required = true) String account, @RequestParam(required = true) BigDecimal amount) throws CommonAPIException {
		LOGGER.info("Withdrawal - Calling");
		return AppWebUtils.buildResponse(CommonMessageConstants.SUCCESS_WITHDRAWAL, HttpStatus.OK, transactionService.withdrawal(account, amount));
	}

	@GetMapping(URLConstants.RESOURCE_TRN_TRANSFER)
	public ResponseEntity<?> transfer(@RequestParam(required = true) String fromAccount, @RequestParam(required = true) String toAccount, @RequestParam(required = true) BigDecimal amount) throws CommonAPIException {
		LOGGER.info("Transfer - Calling");
		return AppWebUtils.buildResponse(CommonMessageConstants.SUCCESS_TRANSFER, HttpStatus.OK, transactionService.transfer(fromAccount, toAccount, amount));
	}
	
	@GetMapping(URLConstants.RESOURCE_TRN_BY)
	public ResponseEntity<?> enquiry(@RequestParam(required = false) String query, Pageable pageable) {
		LOGGER.info("Enquiry - Calling");
		return AppWebUtils.buildResponse(CommonMessageConstants.SUCCESS_FETCHED, HttpStatus.OK, transactionService.enquiry(query, pageable));
	}

	@GetMapping(URLConstants.RESOURCE_TRN_BY_ACCOUNT_NO)
	public ResponseEntity<?> enquiryByAccountNo(@RequestParam(required = false) String query, @RequestParam(required = true) String accountNo, Pageable pageable) throws CommonAPIException {
		LOGGER.info("Enquiry by Account - Calling");
		return AppWebUtils.buildResponse(CommonMessageConstants.SUCCESS_FETCHED, HttpStatus.OK, transactionService.enquiryByAccount(accountNo, query, pageable));
	}

	@GetMapping(URLConstants.RESOURCE_TRN_BY_CIF_NO)
	public ResponseEntity<?> enquiryByCif(@RequestParam(required = false) String query, @RequestParam(required = true) String cif, Pageable pageable) throws CommonAPIException {
		LOGGER.info("Enquiry by Account - Calling");
		return AppWebUtils.buildResponse(CommonMessageConstants.SUCCESS_FETCHED, HttpStatus.OK, transactionService.enquiryByCustomer(cif, query, pageable));
	}

	@GetMapping(URLConstants.RESOURCE_TRN_BY_TRANSACTION_ID)
	public ResponseEntity<?> enquiryByTransactionId(@RequestParam(required = false) String query, @RequestParam(required = true) String transactionId, Pageable pageable) {
		LOGGER.info("Enquiry by Account - Calling");
		return AppWebUtils.buildResponse(CommonMessageConstants.SUCCESS_FETCHED, HttpStatus.OK, transactionService.enquiryByTransactionId(transactionId, query, pageable));
	}
}
