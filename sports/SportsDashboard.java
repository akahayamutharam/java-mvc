import controller.AthleteController;
import dao.AthleteDAO;
import view.*;

import javax.swing.*;


public class SportsDashboard extends JFrame {
	public SportsDashboard() {
		setTitle("Sports Management system");
		setSize(500, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		AthleteDAO dao = new AthleteDAO();
		AthleteFormView formView = new AthleteFormView();
		AthleteTableView tableView = new AthleteTableView();
		AthleteStatsView statsView = new AthleteStatsView();
		
		AthleteController controller = new AthleteController(dao, formView, tableView, statsView);
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Add Athlete", formView);
		tabs.addTab("view Athlete", tableView);
		tabs.addTab("stats", statsView);
		
		add(tabs);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new SportsDashboard();
	}
}	
		
		