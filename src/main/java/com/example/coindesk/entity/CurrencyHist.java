package com.example.coindesk.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "currency_hist")
public class CurrencyHist {

	@Id
	@Column(length = 64, nullable = false, unique = true)
	private String id;

	@Column(length = 20)
	private String name;

	@Column(nullable = false, length = 10)
	private String code; // ex: USD

	@Column(length = 20)
	private String symbol; // ex: &#36;

	@Column(length = 20)
	private String rate; // ex: 57,756.298 (字串，保留格式)

	@Column(length = 100)
	private String description; // ex: United States Dollar

	@Column(name = "rate_float")
	private Double rateFloat; // ex: 57756.2984

	@Column(name = "modify_date")
	private Date modifyDate;

	@Column(length = 10)
	private String action; // ex: ADD SAVE DELETE;

	public CurrencyHist() {
	}

	public CurrencyHist(String code, String name, String symbol, String rate, String description, Double rateFloat) {
		this.code = code;
		this.name = name;
		this.symbol = symbol;
		this.rate = rate;
		this.description = description;
		this.rateFloat = rateFloat;
		this.modifyDate = new Date();
	}

	@PrePersist
	public void generateId() {
		if (id == null) {
			id = generateRandomId(64);
		}
	}

	private String generateRandomId(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int index = (int) (Math.random() * chars.length());
			sb.append(chars.charAt(index));
		}
		return sb.toString();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getRateFloat() {
		return rateFloat;
	}

	public void setRateFloat(Double rateFloat) {
		this.rateFloat = rateFloat;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
