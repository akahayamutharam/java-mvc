package service;

import dao.AccountDAO;
import java.util.List;
import model.Account;

public class AccountService {
	private final AccountDAO dao;
	
	public AccountService(AccountDAO dao) {
		this.dao = dao;
	}

	public List<Account> search(String name, String type, Boolean active, String sortCol, String sortDir) throws Exception {
		return dao.listAccounts(name,type, active, sortCol, sortDir);
	}

	public int save(Account acc) throws Exception {
		return dao.upsert(acc);
	}
	
	public boolean delete(int id) throws Exception {
		return dao.delete(id);
	}
}	