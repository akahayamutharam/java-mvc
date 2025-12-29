package controller;

import dao.UserDAO;
import java.sql.SQLException;
import javax.swing.*;
import model.User;
import view.AdminView;
import view.LoginView;
import view.POSView;

public class LoginController {
	private final LoginView view;
	private final UserDAO userDAO = new UserDAO();
	
	public LoginController(LoginView view) {
		this.view = view;
		init();
	}

	private void init() {
		view.getLoginButton().addActionListener(e -> {
			String username = view.getUsernameField().getText().trim();
			String password = new String(view.getPasswordField().getPassword());
			try {
				User user = userDAO.login(username, password);
				if (user == null) {
					JOptionPane.showMessageDialog(view, "Invalid credentials", "Login", JOptionPane.ERROR_MESSAGE);
					return;
				}
				view.dispose();
				if ("admin".equalsIgnoreCase(user.getRole())) {
					SwingUtilities.invokeLater(() -> new AdminView(user).setVisible(true));
				} else {
					SwingUtilities.invokeLater(() -> new POSView(user).setVisible(true));
				}
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(view, "Database error:" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}	
				
					

				
			