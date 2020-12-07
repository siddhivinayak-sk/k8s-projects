package com.bank.trn.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * Transaction entity.
 *
 * @author kumar-sand
 */
@Entity
@Table(name = "TRANSACTION")
public class TransactionEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(name = "TRANSACTION_ID")
	private String transactionId;
	
	@NotNull
	@Column(name = "IS_CUSTOMER_ACCOUNT")
	private Boolean isCustomerAccount;
	
	@NotNull
	@Column(name = "ACCOUNT_NO")
	private String account;
	
	@NotNull
	@Column(name = "AMOUNT")
	private BigDecimal amount;

	@NotNull
	@Column(name = "CURRENT_BALANCE")
	private BigDecimal currentBalance;
	
	@NotNull
	@Column(name = "TYPE")
	private String type;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TRN_TIME")
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

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
