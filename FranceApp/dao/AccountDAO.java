package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Account;

public class AccountDAO {
	private final DBHelper db;
	
	public AccountDAO(DBHelper db) {
		this.db = db;
	}

	public List<Account> listAccounts(String name, String type, Boolean active, String sortCol, String sortDir) throws Exception {
		List<Account> list = new ArrayList<>();
		try (Connection con = db.getConnection();
			CallableStatement cs = con.prepareCall("{CALL sp_list_accounts(?, ?, ?, ?, ?)}")) {
				
				//use setObject to allow nulls
				cs.setObject(1, (name ==null || name.isEmpty()) ? null : name, Types.VARCHAR);
				cs.setObject(2, (type ==null || type.isEmpty()) ? null : type, Types.VARCHAR);	
				cs.setObject(3, active ==null ? null : (active ? 1 : 0), Types.TINYINT);	
				cs.setObject(4, (sortCol ==null || sortCol.isEmpty()) ? "id": sortCol);	
				cs.setObject(5, (sortDir ==null || sortDir.isEmpty()) ? "ASC" : sortDir);

				try (ResultSet rs = cs.executeQuery()) {
					while (rs.next()) {
						list.add(new Account (
							rs.getInt("id"),
							rs.getString("name"),
							rs.getString("type"),
							rs.getDouble("balance"),
							rs.getInt("active") == 1
						));
					}
				}
			}
		
			//apply direction in java if method
			list.sort((a, b) -> {
				int cmp;
				if ("name".equalsIgnoreCase(sortCol)) cmp = a.getName().compareToIgnoreCase(b.getName());
				else if ("type".equalsIgnoreCase(sortCol)) cmp = a.getType().compareToIgnoreCase(b.getType());
				else if ("balance".equalsIgnoreCase(sortCol)) cmp = Double.compare(a.getBalance(),b.getBalance());
				else cmp = Integer.compare(a.getId(), b.getId());
				return "DESC".equalsIgnoreCase(sortDir) ? -cmp : cmp;
			});
			return list;
	}

		public int upsert(Account acc) throws Exception {
			try (Connection con = db.getConnection();
				CallableStatement cs = con.prepareCall("{CALL sp_upsert_account(?, ?, ?, ?, ?)}")) {
						
				cs.setObject(1, acc.getId() == 0 ? null : acc.getId(), Types.INTEGER);
				cs.setObject(2, acc.getName());
				cs.setObject(3, acc.getType());
				cs.setObject(4, acc.getBalance());
				cs.setObject(5, acc.isActive() ? 1 :0 );
				
				try (ResultSet rs = cs.executeQuery()) {
					return rs.next() ? rs.getInt(1) : -1;
				}
			}
		}

		public boolean delete(int id ) throws Exception {
			try (Connection con = db.getConnection();
				CallableStatement cs = con.prepareCall("{CALL sp_delete_account(?)}")) {
				cs.setObject(1, id);
				
				try (ResultSet rs = cs.executeQuery()) {
					return rs.next() && rs.getInt("affected") > 0;
				}
			}
		}
		
		public ResultSet summary(Connection con, int accountId) throws Exception {
			CallableStatement cs = con.prepareCall("{CALL sp_account_txn_summary(?)}");
			cs.setInt(1, accountId);
			return cs.executeQuery();
		}	
}		
				