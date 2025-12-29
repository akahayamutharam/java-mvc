package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import service.UserService;
import model.User;

public class UserManagementView extends JFrame {
	private UserService service = new UserService();
	private DefaultTableModel model;
	
	public UserManagementView() {
		setTitle("User Management System");
		setSize(700, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		//table
		String[] columns = {"ID", "Username", "Role", "Preference"};
		model= new DefaultTableModel(columns, 0);
		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane , BorderLayout.CENTER);
		
		
		//Form Panel
		JPanel formPanel = new JPanel(new FlowLayout());
		JTextField usernameField = new JTextField(10);
		JTextField passwordField = new JTextField(10);
		JComboBox<String> roleBox = new JComboBox<>(new String[] {"admin", "user"});
		JTextField prefField = new JTextField(10);
		JButton addButton = new JButton("Add User");
		
		formPanel.add(new JLabel("Username:"));
		formPanel.add(usernameField);
		formPanel.add(new JLabel("Password:"));
		formPanel.add(passwordField);
		formPanel.add(new JLabel("Role:"));
		formPanel.add(roleBox);
		formPanel.add(new JLabel("Preference:"));
		formPanel.add(prefField);
		formPanel.add(addButton);
		
		add(formPanel, BorderLayout.SOUTH);
		
		//load initial data
		refreshTable();
		
		//add button action
		addButton.addActionListener(e -> {
			String uname = usernameField.getText().trim();
			String pwd = passwordField.getText().trim();
			String role = roleBox.getSelectedItem().toString();
			String pref = prefField.getText().trim();
			
			
			if(uname.isEmpty() || pwd.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Username and password are required.");
				return;
			}
			
			service.addUser(uname, pwd, role, pref);
			refreshTable();
			
			usernameField.setText("");
			passwordField.setText("");
			prefField.setText("");
		});
	}
	
	private void refreshTable() {
		model.setRowCount(0);
		List<User> users = service.fetchUsers();
		for (User u : users) {
			model.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole(), u.getPreference()});
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new UserManagementView().setVisible(true);
		});
	}
}	
		
		

			