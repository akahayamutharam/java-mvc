package view;

import controller.AthleteController;
import model.Athlete;
import javax.swing.*;
import java.awt.*;

public class AthleteFormView extends JPanel {
	private JTextField nameField = new JTextField();
	private JTextField sportField = new JTextField();
	private JTextField ageField = new JTextField();
	private JTextField scoreField = new JTextField();
	private AthleteController controller;
	
	public AthleteFormView() {
		setLayout(new GridLayout(6, 2, 5, 5));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10 ));
		
		add(new JLabel("Name:"));
		add(nameField);
		add(new JLabel("Sport:"));
		add(sportField);
		add(new JLabel("Age:"));
		add(ageField);
		add(new JLabel("Score:"));
		add(scoreField);
		
		JButton addBtn = new JButton("Add Athlete");
		addBtn.addActionListener(e -> handleAdd());
		add(addBtn);
		
		JButton clearBtn = new JButton("Reset");
		clearBtn.addActionListener(e -> clearForm());
		add(clearBtn);
	}
	
	private void handleAdd() {
		String name = nameField.getText().trim();
		String sport = sportField.getText().trim();
		String ageText = ageField.getText().trim();
		String scoreText = scoreField.getText().trim();
		
		if (name.isEmpty() || sport.isEmpty() || ageText.isEmpty() || scoreText.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill in all filds.", "Validation Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		try{
			int age = Integer.parseInt(ageText);
			double score = Double.parseDouble(scoreText);
			
			Athlete a = new Athlete(0, name, sport, age, score);
			controller.addAthlete(a);
			
			JOptionPane.showMessageDialog(this, "Record inserted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
			clearForm();
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Age must be an integer and score must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void clearForm() {
		nameField.setText("");
		sportField.setText("");
		ageField.setText("");
		scoreField.setText("");
	}
	
	public void setController(AthleteController c) {
		this.controller = c;
	}
}