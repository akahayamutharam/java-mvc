package model;

import java.time.LocalDateTime;

public class Transaction {
	private int id;
	private int accountId;
	private String txntype;
	private double amount;
	private LocalDateTime createdAt;
	
	public Transaction(int id,  int accountId, String txntype, double amount, LocalDateTime createdAt) {
		this.id = id;
		this.accountId = accountId;
		this.txntype = txntype;
		this.amount = amount;
		this.createdAt = createdAt;
	}
	
	public int getId() { return id; }
	public int getAccountId() { return accountId; }
	public String getTxnType() { return txntype; }
	public double getAmount() { return amount; }
	public LocalDateTime getCreatedAt() { return createdAt; }
}	