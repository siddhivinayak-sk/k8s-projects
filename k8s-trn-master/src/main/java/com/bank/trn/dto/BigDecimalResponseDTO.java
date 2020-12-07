package com.bank.trn.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.http.HttpStatus;

public class BigDecimalResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal data;
	
	private String message;
	
	private HttpStatus status;
	
	private Date responseTime;
	
	public BigDecimalResponseDTO() {
		//Empty constructor
	}

	public BigDecimalResponseDTO(BigDecimal data, String message, HttpStatus status) {
		super();
		this.data = data;
		this.message = message;
		this.status = status;
		responseTime = new Date();
	}

	public BigDecimal getData() {
		return data;
	}

	public void setData(BigDecimal data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}
	
	
}
