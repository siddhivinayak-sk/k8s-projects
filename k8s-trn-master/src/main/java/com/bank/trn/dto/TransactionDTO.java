package com.bank.trn.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * Transaction DTO class.
 *
 * @author kumar-sand
 */
public class TransactionDTO implements Serializable {
	
	/** The Constant serialVersionUID. */
	final private static long serialVersionUID = 1l;
	
	private Long id;
	
	@NotNull
	private String transactionId;

	@NotNull
	private Boolean isCustomerAccount;
	
	@NotNull
	private String account;
	
	@NotNull
	private BigDecimal amount;
	
	@NotNull
	private String type;
	
	private Date time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Boolean getIsCustomerAccount() {
		return isCustomerAccount;
	}

	public void setIsCustomerAccount(Boolean isCustomerAccount) {
		this.isCustomerAccount = isCustomerAccount;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
