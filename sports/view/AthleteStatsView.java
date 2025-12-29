package view;

import controller.AthleteController;
import model.Athlete;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AthleteStatsView extends JPanel {
	private JLabel avgAgeLabel = new JLabel();
	private JLabel topScoreLabel = new JLabel();
	private AthleteController controller;
	
	public AthleteStatsView() {
		setLayout(new GridLayout(3, 1, 10, 10));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JLabel title = new JLabel("Athlete Statistics", JLabel.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 18));
		add(title);
		
		avgAgeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
		topScoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
		
		add(avgAgeLabel);
		add(topScoreLabel);
		
	}	
	
	public void updateStats(List<Athlete> athletes) {
		double avgAge = athletes.stream().mapToInt(Athlete::getAge).average().orElse(0);
		double topScore = athletes.stream().mapToDouble(Athlete::getScore).max().orElse(0);
		
		avgAgeLabel.setText("Average Age: " + String.format("%.2f", avgAge));
		topScoreLabel.setText("Top Score: " + String.format("%.2f", topScore));
	}

	public void setController(AthleteController c) {
		this.controller = c;
	}
}
		