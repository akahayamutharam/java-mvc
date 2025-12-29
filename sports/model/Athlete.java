package model;

public class Athlete {
	private int id;
	private String name;
	private String sport;
	private int age;
	private double  score;
	
	public Athlete(int id, String name, String sport, int age, double score){
		this.id = id;
		this.name = name;
		this.sport=sport;
		this.age= age;
		this.score=score;
	}

	public int getId() {return id; }
	public String getName() {return name; }
	public String getSport() {return sport; }
	public int getAge() {return age; }
	public double getScore() {return score; }
}	