package view;

import controller.AthleteController;
import model.Athlete;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AthleteTableView extends JPanel {
	private DefaultTableModel model;
	private JTable table;
	private AthleteController controller;
	
	public AthleteTableView() {
		setLayout(new BorderLayout());
		
		JLabel title = new JLabel("Athlete List", JLabel.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 18));
		add(title, BorderLayout.NORTH);
		
		model = new DefaultTableModel(new String[] {"ID", "Name", "Sport", "Age", "Score"},0);
		table = new JTable(model);
		table.setRowHeight(25);
		table.setFont(new Font("SansSerif", Font.PLAIN, 16));
		table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
		
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
	}	
	
	public void updateTable(List<Athlete> athletes) {
		model.setRowCount(0);
		for(Athlete a : athletes) {
			model.addRow(new Object[] {
				a.getId(), a.getName(), a.getSport(), a.getAge(), a.getScore()
			});
		}
	}	
		

	public void setController(AthleteController c) {
		this.controller = c;
	}
}
		