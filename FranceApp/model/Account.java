package model;

public class Account {
	private int id;
	private String name;
	private String type;
	private double balance;
	private boolean active;
	
	public Account(int id, String name, String type, double balance, boolean active) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.balance = balance;
		this.active = active;
	}
	
	public int getId() { return id; }
	public String getName() { return name; }
	public String getType() { return type; }
	public double getBalance() { return balance; }
	public boolean isActive() { return active; }
}	