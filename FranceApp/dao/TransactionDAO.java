package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public  class TransactionDAO {
	private  final DBHelper db;
	
	public TransactionDAO(DBHelper db) {
		this.db = db;
	}

	//Example simple fetch od last N Transactions for an account (direct SQL; extends as method)
	public ResultSet lastTransactions(Connection con ,int accountId, int limit) throws Exception {
		PreparedStatement ps = con.prepareStatement(
			"SELECT id, txn_type, amount, created_at FROM transaction WHERE account_id=? ORDER BY created_at DESC LIMIT ?");
		ps.setInt(1, accountId);
		ps.setInt(2, limit);
		return ps.executeQuery();
	}
}	
	
	