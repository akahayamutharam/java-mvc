package dao;

import model.Athlete;
import java.sql.*;
import java.util.*;

public class AthleteDAO {
    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/sports_db", "root", "Akshaya@1408");
    }

    public List<Athlete> getAllAthletes() throws SQLException {
        List<Athlete> list = new ArrayList<>();
        try (Connection conn = connect()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM athletes");
            while (rs.next()) {
                list.add(new Athlete(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("sport"),
                    rs.getInt("age"),
                    rs.getDouble("score")
                ));
            }
        }
        return list;
    }

    public void addAthlete(Athlete a) throws SQLException {
        try (Connection conn = connect()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO athletes(name, sport, age, score) VALUES (?, ?, ?, ?)");
            ps.setString(1, a.getName());
            ps.setString(2, a.getSport());
            ps.setInt(3, a.getAge());
            ps.setDouble(4, a.getScore());
            ps.executeUpdate();
        }
    }
}