package model;

public class User {
	private int id;
	private String username;
	private String role;
	private String preference;
	
	public User(int id, String username, String role, String preference) {
		this.id = id;
		this.username = username;
		this.role= role;
		this.preference =preference;
	}

	public int getId() {return id;}
	public  String getUsername() {return username;}
	public String getRole() {return role;}
	public String getPreference() {return preference;}
}	