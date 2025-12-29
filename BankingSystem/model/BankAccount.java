package model;

public class BankAccount {
	public int id;
	public String  name;
	public String accountType;
	public double balance;
	public boolean active;
	
	public BankAccount(int id, String name, String accountType, double balance, boolean active) {
		this.id = id;
		this.name = name;
		this.accountType = accountType;
		this.balance = balance;
		this.active = active;
	}
}	