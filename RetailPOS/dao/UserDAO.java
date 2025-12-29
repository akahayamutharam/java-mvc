package dao;

import model.User;
import java.sql.*;

public class UserDAO {
    public User login(String username, String password) throws SQLException {
        String sql = "SELECT id, username, role FROM users WHERE username=? AND password=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("role"));
                }
            }
        }
        return null;
    }
}
