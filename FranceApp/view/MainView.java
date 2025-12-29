package view;

import controller.FinanceController;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Account;

public class  MainView extends JFrame {
	private final JTextField nameField = new JTextField(12);
	private final JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Savings", "Current", "Business"});
	private final JCheckBox activeCheck = new JCheckBox("Active", true);
	private final JTextField balanceField = new JTextField(10);
	private final JButton addBtn = new JButton("Add/Update");
	private final JButton deleteBtn = new JButton("Delete");
	private final JButton searchBtn = new JButton("Search");
	private final JButton resetBtn = new JButton("Reset");
	private final JButton sortNameBtn = new JButton("Sort:Name");
	private final JButton sortBalanceBtn = new JButton("Sort:Balance");
	private final JButton sortIdBtn = new JButton("Sort:ID");
	private final JComboBox<String> orderCombo = new JComboBox<>(new String[]{"Ascending","Descending"});
	private final JLabel status = new JLabel ("Ready");
	
	private final JTable table;
	private final DefaultTableModel model;
	private final FinanceController controller;
	
	public MainView (FinanceController controller, DefaultTableModel model) {
		this.controller = controller;
		this.model = model;
		this.table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		setTitle("Finance Management (swings MVC + MySQL SP)");
        setSize(1000, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
        setLayout(new BorderLayout());
		
		add(buildTop(), BorderLayout.NORTH);
		add(new JScrollPane(table), BorderLayout.CENTER);
		add(status, BorderLayout.SOUTH);
		
		wireEvents();
		controller.load(null, null, null,"id", "ASC");
		
	}

	private JPanel buildTop() {
		JPanel p = new JPanel( new FlowLayout(FlowLayout.LEFT));
		p.add(new JLabel("Name:"));
		p.add(nameField);
		p.add(new JLabel("Type:"));
		p.add(typeCombo);
		p.add(new JLabel("Balance:"));
		p.add(balanceField);
		p.add(activeCheck);
		p.add(addBtn);
		p.add(deleteBtn);
		p.add(searchBtn);
		p.add(resetBtn);
		p.add(sortNameBtn);
		p.add(sortBalanceBtn);
		p.add(sortIdBtn);
		p.add(new JLabel("Order:"));
		p.add(orderCombo);
		return p;
	}
	
	private void wireEvents() {
		addBtn.addActionListener(e -> {
			String name = nameField.getText().trim();
			String type = (String) typeCombo.getSelectedItem();
			boolean active = activeCheck.isSelected();
			double balance = parseDouble(balanceField.getText().trim(),0.0);
			
			Integer id= getSelectedId(); // update if row selected , else insert
			controller.save(new Account(id == null ? 0 : id, name, type, balance, active));
			status.setText(id == null ? "Inserted" : "Updated");
		});

		deleteBtn.addActionListener(e -> {
			Integer id = getSelectedId();
			if(id == null) {JOptionPane.showMessageDialog(this, "Select a row to delete"); return;}
			controller.delete(id);
			status.setText("Deleted ID " +id);
		});
		
		searchBtn.addActionListener(e -> {
			String name = nameField.getText().trim();
			String type = (String) typeCombo.getSelectedItem();
			boolean active = activeCheck.isSelected();
			controller.load(name.isEmpty()?null:name, type, active, "name", getOrder());
			status.setText("Search applied");
		});

		resetBtn.addActionListener(e -> {
			nameField.setText("");
			typeCombo.setSelectedIndex(0);
			balanceField.setText("");
			activeCheck.setSelected(true);
			orderCombo.setSelectedIndex(0);
			controller.load(null, null, null,"id", "ASC");
			status.setText("Filters reset");
		});

		sortNameBtn.addActionListener(e -> {
			controller.load(null, null, null, "name", getOrder());
			status.setText("Sorted by name");
		});
		
		sortBalanceBtn.addActionListener(e -> {
			controller.load(null, null, null, "balance", getOrder());
			status.setText("Sorted by balance");
		});
		
		sortIdBtn.addActionListener(e -> {
			controller.load(null, null, null, "id", getOrder());
			status.setText("Sorted by ID");
		});
		
		table.getSelectionModel().addListSelectionListener(e-> {
			int row = table.getSelectedRow();
			if(row >= 0) {
				nameField.setText(String.valueOf(model.getValueAt(row, 1)));
				typeCombo.setSelectedItem(String.valueOf(model.getValueAt(row, 2)));
				balanceField.setText(String.valueOf(model.getValueAt(row, 3)));
				activeCheck.setSelected(Boolean.parseBoolean(String.valueOf(model.getValueAt(row, 4))));
			}
		});
	}

	private Integer getSelectedId() {
		int row = table.getSelectedRow();
		if (row < 0) return null;
		Object val = model.getValueAt(row, 0);
		return val == null ? null : Integer.parseInt(val.toString());
	}

	private String getOrder() {
		return"Descending".equalsIgnoreCase((String) orderCombo.getSelectedItem()) ? "DESC" : "ASC";
	}	
	private double parseDouble(String s, double def) {
		if (s == null || s.isEmpty()) return def;
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException ex) {
			return def;
		}
	}
}		

		
			