package controller;

import dao.AthleteDAO;
import model.Athlete;
import view.*;

public class AthleteController {
	private AthleteDAO dao;
	private AthleteFormView formView;
	private AthleteTableView tableView;
	private AthleteStatsView statsView;
	
	public AthleteController(AthleteDAO dao, AthleteFormView form, AthleteTableView table, AthleteStatsView stats) {
		this.dao = dao;
		this.formView = form;
		this.tableView = table;
		this.statsView = stats;
		
		formView.setController(this);
		tableView.setController(this);
		statsView.setController(this);
		
		refreshViews();
	}

	public void addAthlete(Athlete a) {
		try {
			dao.addAthlete(a);
			refreshViews();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void refreshViews() {
		try {
			var athelets = dao.getAllAthletes();
			tableView.updateTable(athelets);
			statsView.updateStats(athelets);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}	
	

				