package controller;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.Account;
import service.AccountService;

public class FinanceController{
	private final AccountService service;
	private final DefaultTableModel model;
	
	public FinanceController(AccountService service,DefaultTableModel model) {
		this.service =service;
		this.model = model;
		initHeaders();
	}

	private void initHeaders() {
		model.setColumnCount(0);
		model.addColumn("ID");
		model.addColumn("Name");
		model.addColumn("Type");
		model.addColumn("Balance");
		model.addColumn("Active");
	}

	public void load(String name, String type, Boolean active, String sortCol, String sortDir) {
		try {
			List<Account> accounts = service.search(name, type, active, sortCol, sortDir);
			model.setRowCount(0);
			for(Account acc : accounts) {
				model.addRow(new Object[] {
					acc.getId(), acc.getName(), acc.getType(), acc.getBalance(), acc.isActive()
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save(Account acc) {
		try {
			service.save(acc);
			load(null, null, null, "id", "ASC");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void delete(int id) {
		try {
			service.delete(id);
			load(null, null, null, "id", "ASC");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}	

		
				