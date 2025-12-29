package dao;

import java.sql.*;

public class UserDAO {
	private static final String URL  = "jdbc:mysql://localhost:3306/usermanagement"; //replace with your DB name
    private static final String USER = "root";      // TODO: set your MySQL user
    private static final String PASSWORD = "Akshaya@1408";  // TODO: set your MySQL password
	
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	// Fetch users as xml (stored producre)
	public String getUsersXML() {
		String xmlData = null;
		try (Connection conn = getConnection();
			CallableStatement stmt = conn.prepareCall("{CALL getUsersXML()}");
			ResultSet rs = stmt.executeQuery()) {
				
			if (rs.next()) {
				xmlData = rs.getString("result");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 return xmlData;
		
	}
	
	// add user (stored producre)
	public void addUser(String username, String password, String role, String preference) {
		try (Connection conn = getConnection();
			CallableStatement stmt = conn.prepareCall("{CALL addUser(?,?,?,?)}")) {
				
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, role);
			stmt.setString(4, preference);
			stmt.executeUpdate();
			
			System.out.println(" User added successfully: " + username);
		} catch (SQLException e)  {
			System.err.println(" Failed to add user: " + e.getMessage());
		}
	}
}