package dao;

import model.BankAccount;
import java.sql.*;
import java.util.*;

public class BankDAO {
	Connection conn;
	
	public BankDAO() {
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db", "root", "Akshaya@1408");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public List<BankAccount> getAllAccounts() {
		List<BankAccount> list = new ArrayList<>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM accounts");
			while(rs.next()) {
				list.add(new BankAccount (
					rs.getInt("id"),
					rs.getString("name"),
					rs.getString("account_type"),
					rs.getDouble("balance"),
					rs.getBoolean("active")
				));
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void insertAccount(BankAccount acc) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO accounts (name, account_type, balance, active) VALUES (?, ?, ?, ?)");
			ps.setString(1, acc.name);
			ps.setString(2, acc.accountType);
			ps.setDouble(3, acc.balance);
			ps.setBoolean(4, acc.active);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateAccount(BankAccount acc) {
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE accounts SET name=?, account_type=?, balance=?, active=? WHERE id=?");
			ps.setString(1, acc.name);
			ps.setString(2, acc.accountType);
			ps.setDouble(3, acc.balance);
			ps.setBoolean(4, acc.active);
			ps.setInt(5, acc.id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteAccount(int id) {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM accounts WHERE id=?");
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}