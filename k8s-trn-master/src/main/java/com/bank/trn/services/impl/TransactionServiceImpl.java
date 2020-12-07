package com.bank.trn.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.bank.trn.constants.Exceptions;
import com.bank.trn.constants.SpecificationConstants;
import com.bank.trn.dto.AccountDTO;
import com.bank.trn.dto.BigDecimalResponseDTO;
import com.bank.trn.dto.CustomerDTO;
import com.bank.trn.dto.CustomerResponseDTO;
import com.bank.trn.dto.TransactionDTO;
import com.bank.trn.entities.TransactionEntity;
import com.bank.trn.exceptions.CommonAPIException;
import com.bank.trn.repositories.TransactionRepository;
import com.bank.trn.services.TransactionService;
import com.bank.trn.utils.SpecificationUtils;
import com.bank.trn.utils.SpecificationUtils.Expression;

/**
 * Transaction Service implementation.
 *
 * @author kumar-sand
 */
@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${csm.bankbranch.doadjustment}")
	private String bankBranchDoAdjustmentURI;

	@Value("${csm.customer.doadjustment}")
	private String customerDoAdjustmentURI;
	
	@Value("${csm.customer.byacno}")
	private String customerbyAccountNoURI;
	
	@Value("${csm.customer.bycif}")
	private String customerbyCifURI;
	
	private Calendar calendar = Calendar.getInstance();
	
	public static final String DEPOSIT_BY_AMT_PO = "DEPOSIT_BY_AMT_PO";
	public static final String WITHDRAWAL_BY_AMT_NE = "WITHDRAWAL_BY_AMT_NE";
	public static final String TRANSFER_BY_VAL_PO = "TRANSFER_BY_VAL_PO";
	public static final String TRANSFER_BY_VAL_NE = "TRANSFER_BY_VAL_NE";
	
	@Override
	public String deposit(String accountNo, BigDecimal amount) throws CommonAPIException {
		AccountDTO account = validateDeposit(accountNo, amount);
		String transactionId = getUniqueTransactionID();
		Date date = calendar.getTime();
		BigDecimal accountAdj = doAccountAdjustment(account.getAccountNo(), amount, true);
		List<TransactionEntity> trList = new ArrayList<>();
		if(null != accountAdj) {
			TransactionEntity trAccount = new TransactionEntity();
			trAccount.setAccount(account.getAccountNo());
			trAccount.setAmount(amount);
			trAccount.setIsCustomerAccount(true);
			trAccount.setTransactionId(transactionId);
			trAccount.setType(DEPOSIT_BY_AMT_PO);
			trAccount.setCurrentBalance(accountAdj);
			trAccount.setTime(date);
			trList.add(trAccount);
		}
		else {
			throw new CommonAPIException(Exceptions.E0038);
			
		}
		BigDecimal branchAdj = doBranchAdjustment(account.getBranchCode(), amount, true);
		if(null != branchAdj) {
			TransactionEntity trBranch = new TransactionEntity();
			trBranch.setAccount(account.getBranchCode());
			trBranch.setAmount(amount);
			trBranch.setIsCustomerAccount(false);
			trBranch.setTransactionId(transactionId);
			trBranch.setType(DEPOSIT_BY_AMT_PO);
			trBranch.setCurrentBalance(branchAdj);
			trBranch.setTime(date);
			trList.add(trBranch);
		}
		else {
			throw new CommonAPIException(Exceptions.E0038);
			
		}
		transactionRepository.saveAll(trList);
		return transactionId;
	}

	@Override
	public String withdrawal(String accountNo, BigDecimal amount) throws CommonAPIException {
		AccountDTO account = validateWithdrawal(accountNo, amount);
		Date date = calendar.getTime();
		String transactionId = getUniqueTransactionID();
		BigDecimal accountAdj = doAccountAdjustment(account.getAccountNo(), amount, false);
		List<TransactionEntity> trList = new ArrayList<>();
		if(null != accountAdj) {
			TransactionEntity trAccount = new TransactionEntity();
			trAccount.setAccount(account.getAccountNo());
			trAccount.setAmount(amount);
			trAccount.setIsCustomerAccount(true);
			trAccount.setTransactionId(transactionId);
			trAccount.setType(WITHDRAWAL_BY_AMT_NE);
			trAccount.setCurrentBalance(accountAdj);
			trAccount.setTime(date);
			trList.add(trAccount);
		}
		else {
			throw new CommonAPIException(Exceptions.E0038);
			
		}
		BigDecimal branchAdj = doBranchAdjustment(account.getBranchCode(), amount, false);
		if(null != branchAdj) {
			TransactionEntity trBranch = new TransactionEntity();
			trBranch.setAccount(account.getBranchCode());
			trBranch.setAmount(amount);
			trBranch.setIsCustomerAccount(false);
			trBranch.setTransactionId(transactionId);
			trBranch.setType(WITHDRAWAL_BY_AMT_NE);
			trBranch.setCurrentBalance(branchAdj);
			trBranch.setTime(date);
			trList.add(trBranch);
		}
		else {
			throw new CommonAPIException(Exceptions.E0038);
			
		}
		transactionRepository.saveAll(trList);
		return transactionId;
	}

	@Override
	public String transfer(String fromAccountNo, String toAccountNo, BigDecimal amount) throws CommonAPIException {
		AccountDTO toAccount = validateTo(toAccountNo);
		AccountDTO fromAccount = validateFrom(fromAccountNo, amount);
		Date date = calendar.getTime();
		String transactionId = getUniqueTransactionID();
		BigDecimal fromAccountAdj = doAccountAdjustment(fromAccount.getAccountNo(), amount, false);
		List<TransactionEntity> trList = new ArrayList<>();
		if(null != fromAccountAdj) {
			TransactionEntity trAccount = new TransactionEntity();
			trAccount.setAccount(fromAccount.getAccountNo());
			trAccount.setAmount(amount);
			trAccount.setIsCustomerAccount(true);
			trAccount.setTransactionId(transactionId);
			trAccount.setType(TRANSFER_BY_VAL_NE);
			trAccount.setCurrentBalance(fromAccountAdj);
			trAccount.setTime(date);
			trList.add(trAccount);
		}
		else {
			throw new CommonAPIException(Exceptions.E0038);
			
		}
		BigDecimal toAccountAdj = doAccountAdjustment(toAccount.getAccountNo(), amount, true);
		if(null != toAccountAdj) {
			TransactionEntity trAccount = new TransactionEntity();
			trAccount.setAccount(toAccount.getAccountNo());
			trAccount.setAmount(amount);
			trAccount.setIsCustomerAccount(true);
			trAccount.setTransactionId(transactionId);
			trAccount.setType(TRANSFER_BY_VAL_PO);
			trAccount.setCurrentBalance(toAccountAdj);
			trAccount.setTime(date);
			trList.add(trAccount);
		}
		else {
			throw new CommonAPIException(Exceptions.E0038);
			
		}
		transactionRepository.saveAll(trList);
		return transactionId;
	}

	@Override
	public Page<TransactionDTO> enquiry(String query, Pageable pageable) {
		Specification<TransactionEntity> specification = SpecificationUtils.buildSpecification(query, new ArrayList<>());
		Page<TransactionEntity> transactionPage = transactionRepository.findAll(specification, pageable);
		List<TransactionDTO> transactionDTOList = transactionPage.getContent().stream().map(transaction -> {
			TransactionDTO temp = new TransactionDTO();
			BeanUtils.copyProperties(transaction, temp);
			return temp;
			}).collect(Collectors.toList());
		return new PageImpl<TransactionDTO>(transactionDTOList, pageable, transactionPage.getTotalElements());
	}

	@Override
	public Page<TransactionDTO> enquiryByAccount(String account, String query, Pageable pageable) throws CommonAPIException {
		validateAccountNo(account);
		List<Expression<? extends Comparable<?>>> expressionList = new ArrayList<>();
		expressionList.add(new Expression<String>("account", SpecificationConstants.OPERATOR_EQUAL, SpecificationConstants.JOIN_AND, account, null));
		Specification<TransactionEntity> specification = SpecificationUtils.buildSpecification(query, expressionList);
		Page<TransactionEntity> transactionPage = transactionRepository.findAll(specification, pageable);
		List<TransactionDTO> transactionDTOList = transactionPage.getContent().stream().map(transaction -> {
			TransactionDTO temp = new TransactionDTO();
			BeanUtils.copyProperties(transaction, temp);
			return temp;
			}).collect(Collectors.toList());
		return new PageImpl<TransactionDTO>(transactionDTOList, pageable, transactionPage.getTotalElements());
	}

	@Override
	public Page<TransactionDTO> enquiryByCustomer(String cif, String query, Pageable pageable) throws CommonAPIException {
		List<String> accounts = validateCustomer(cif);
		List<Expression<? extends Comparable<?>>> expressionList = new ArrayList<>();
		expressionList.add(new Expression<String>("account", SpecificationConstants.OPERATOR_IN, SpecificationConstants.JOIN_AND, null, accounts));
		Specification<TransactionEntity> specification = SpecificationUtils.buildSpecification(query, expressionList);
		Page<TransactionEntity> transactionPage = transactionRepository.findAll(specification, pageable);
		List<TransactionDTO> transactionDTOList = transactionPage.getContent().stream().map(transaction -> {
			TransactionDTO temp = new TransactionDTO();
			BeanUtils.copyProperties(transaction, temp);
			return temp;
			}).collect(Collectors.toList());
		return new PageImpl<TransactionDTO>(transactionDTOList, pageable, transactionPage.getTotalElements());
	}

	@Override
	public Page<TransactionDTO> enquiryByTransactionId(String transactionId, String query, Pageable pageable) {
		List<Expression<? extends Comparable<?>>> expressionList = new ArrayList<>();
		expressionList.add(new Expression<String>("transactionId", SpecificationConstants.OPERATOR_EQUAL, SpecificationConstants.JOIN_AND, transactionId, null));
		Specification<TransactionEntity> specification = SpecificationUtils.buildSpecification(query, expressionList);
		Page<TransactionEntity> transactionPage = transactionRepository.findAll(specification, pageable);
		List<TransactionDTO> transactionDTOList = transactionPage.getContent().stream().map(transaction -> {
			TransactionDTO temp = new TransactionDTO();
			BeanUtils.copyProperties(transaction, temp);
			return temp;
			}).collect(Collectors.toList());
		return new PageImpl<TransactionDTO>(transactionDTOList, pageable, transactionPage.getTotalElements());
	}

	private AccountDTO validateAccountNo(String accountNo) throws CommonAPIException {
		CustomerDTO customer = getCustomerDTO(accountNo);
		AccountDTO account = null;
		if(null != customer && null != customer.getAccounts()) {
			for(AccountDTO ac:customer.getAccounts()) {
				if(null != accountNo && accountNo.equals(ac.getAccountNo())) {
					account = ac;
					break;
				}
			}
		}
		if(null == account) {
			throw new CommonAPIException(Exceptions.E0026);
		}
		return account;
	}

	private List<String> validateCustomer(String cif) throws CommonAPIException {
		CustomerDTO customer = getCustomerDTOByCif(cif);
		List<String> accounts = new ArrayList<>();
		if(null != customer && null != customer.getAccounts()) {
			for(AccountDTO ac:customer.getAccounts()) {
				accounts.add(ac.getAccountNo());
			}
		}
		if(accounts.isEmpty()) {
			throw new CommonAPIException(Exceptions.E0037);
		}
		return accounts;
	}
	
	private AccountDTO validateWithdrawal(String accountNo, BigDecimal amount) throws CommonAPIException {
		CustomerDTO customer = getCustomerDTO(accountNo);
		AccountDTO account = null;
		if(null != customer && null != customer.getAccounts()) {
			for(AccountDTO ac:customer.getAccounts()) {
				if(null != accountNo && accountNo.equals(ac.getAccountNo())) {
					account = ac;
					break;
				}
			}
		}
		if(null == accountNo || null == customer || null == customer.getAccounts() || null == account) {
			throw new CommonAPIException(Exceptions.E0026);
		}
		if(null == amount || amount.doubleValue() < 0) {
			throw new CommonAPIException(Exceptions.E0027);
		}
		if(null == customer.getActive() || customer.getActive().equals(Boolean.FALSE)) {
			throw new CommonAPIException(Exceptions.E0032);
		}
		if(null == account.getActive() || account.getActive().equals(Boolean.FALSE)) {
			throw new CommonAPIException(Exceptions.E0030);
		}
		if(null == account.getBalance() || account.getBalance().doubleValue() < amount.doubleValue()) {
			throw new CommonAPIException(Exceptions.E0031);
		}
		return account;
	}

	private AccountDTO validateDeposit(String accountNo, BigDecimal amount) throws CommonAPIException {
		CustomerDTO customer = getCustomerDTO(accountNo);
		AccountDTO account = null;
		if(null != customer && null != customer.getAccounts()) {
			for(AccountDTO ac:customer.getAccounts()) {
				if(null != accountNo && accountNo.equals(ac.getAccountNo())) {
					account = ac;
					break;
				}
			}
		}
		if(null == accountNo || null == customer || null == customer.getAccounts() || null == account) {
			throw new CommonAPIException(Exceptions.E0026);
		}
		if(null == amount || amount.doubleValue() < 0) {
			throw new CommonAPIException(Exceptions.E0027);
		}
		if(null == customer.getActive() || customer.getActive().equals(Boolean.FALSE)) {
			throw new CommonAPIException(Exceptions.E0032);
		}
		if(null == account.getActive() || account.getActive().equals(Boolean.FALSE)) {
			throw new CommonAPIException(Exceptions.E0030);
		}
		return account;
	}
	
	private AccountDTO validateTo(String toAccountNo) throws CommonAPIException {
		CustomerDTO customer = getCustomerDTO(toAccountNo);
		AccountDTO account = null;
		if(null != customer && null != customer.getAccounts()) {
			for(AccountDTO ac:customer.getAccounts()) {
				if(null != toAccountNo && toAccountNo.equals(ac.getAccountNo())) {
					account = ac;
					break;
				}
			}
		}
		if(null == toAccountNo || null == customer || null == customer.getAccounts() || null == account) {
			throw new CommonAPIException(Exceptions.E0028);
		}
		if(null == customer.getActive() || customer.getActive().equals(Boolean.FALSE)) {
			throw new CommonAPIException(Exceptions.E0035);
		}
		if(null == account.getActive() || account.getActive().equals(Boolean.FALSE)) {
			throw new CommonAPIException(Exceptions.E0033);
		}
		return account;
	}

	private AccountDTO validateFrom(String fromAccountNo, BigDecimal amount) throws CommonAPIException {
		CustomerDTO customer = getCustomerDTO(fromAccountNo);
		AccountDTO account = null;
		if(null != customer && null != customer.getAccounts()) {
			for(AccountDTO ac:customer.getAccounts()) {
				if(null != fromAccountNo && fromAccountNo.equals(ac.getAccountNo())) {
					account = ac;
					break;
				}
			}
		}
		if(null == fromAccountNo || null == customer || null == customer.getAccounts() || null == account) {
			throw new CommonAPIException(Exceptions.E0029);
		}
		if(null == amount || amount.doubleValue() < 0) {
			throw new CommonAPIException(Exceptions.E0027);
		}
		if(null == customer.getActive() || customer.getActive().equals(Boolean.FALSE)) {
			throw new CommonAPIException(Exceptions.E0036);
		}
		if(null == account.getActive() || account.getActive().equals(Boolean.FALSE)) {
			throw new CommonAPIException(Exceptions.E0034);
		}
		if(null == account.getBalance() || account.getBalance().doubleValue() < amount.doubleValue()) {
			throw new CommonAPIException(Exceptions.E0031);
		}
		return account;
	}
	
	private String getUniqueTransactionID() {
		return UUID.randomUUID().toString();
	}
	
	private BigDecimal doBranchAdjustment(String branchCode, BigDecimal amount, boolean isPositive) throws CommonAPIException {
		String resource = bankBranchDoAdjustmentURI + "?branchCode=" + branchCode + "&amount=" + amount + "&isPositive=" + isPositive;
		try {
			ResponseEntity<BigDecimalResponseDTO> responseEntity = restTemplate.getForEntity(resource, BigDecimalResponseDTO.class);
			if(responseEntity.getStatusCode() == HttpStatus.OK) {
				return responseEntity.getBody().getData();
			}
		}
		catch(HttpClientErrorException ex) {
			throw new CommonAPIException(Exceptions.E0039, ex);
		}
		return null;
	}

	private BigDecimal doAccountAdjustment(String accountNo, BigDecimal amount, boolean isPositive) throws CommonAPIException {
		String resource = customerDoAdjustmentURI + "?accountNo=" + accountNo + "&amount=" + amount + "&isPositive=" + isPositive;
		try {
			ResponseEntity<BigDecimalResponseDTO> responseEntity = restTemplate.getForEntity(resource, BigDecimalResponseDTO.class);
			if(responseEntity.getStatusCode() == HttpStatus.OK) {
				return responseEntity.getBody().getData();
			}
		}
		catch(HttpClientErrorException ex) {
			throw new CommonAPIException(Exceptions.E0039, ex);
		}
		return null;
	}

	private CustomerDTO getCustomerDTO(String accountNo) throws CommonAPIException {
		String resource = customerbyAccountNoURI + "?accountNo=" + accountNo;
		try {
			ResponseEntity<CustomerResponseDTO> responseEntity = restTemplate.getForEntity(resource, CustomerResponseDTO.class);
			if(responseEntity.getStatusCode() == HttpStatus.OK) {
				return responseEntity.getBody().getData();
			}
		}
		catch(HttpClientErrorException ex) {
			throw new CommonAPIException(Exceptions.E0039, ex);
		}
		return null;
	}

	private CustomerDTO getCustomerDTOByCif(String cif) throws CommonAPIException {
		String resource = customerbyCifURI + "?cif=" + cif;
		try {
			ResponseEntity<CustomerResponseDTO> responseEntity = restTemplate.getForEntity(resource, CustomerResponseDTO.class);
			if(responseEntity.getStatusCode() == HttpStatus.OK) {
				return responseEntity.getBody().getData();
			}
		}
		catch(HttpClientErrorException ex) {
			throw new CommonAPIException(Exceptions.E0039, ex);
		}
		return null;
	}

}
