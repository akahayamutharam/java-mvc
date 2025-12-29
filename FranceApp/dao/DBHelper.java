package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public  class DBHelper {
	private static final String URL = "jdbc:mysql://localhost:3306/finance_db";
	private static final String USER = "root";
	private static final String PASSWORD = "Akshaya@1408";
	
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("MySql JDBC Driver loaded successfully.");
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to load MySql JDBC Driver");
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
	
	