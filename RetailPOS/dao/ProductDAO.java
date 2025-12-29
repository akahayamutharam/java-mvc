package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Product;

public class ProductDAO {

    public List<Product> findAll(String query) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id,name,sku,price,stock,category,image_path FROM products " +
                     "WHERE (? IS NULL OR name LIKE ? OR sku LIKE ? OR category LIKE ?) ORDER BY name";
        try (Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            String q = (query == null || query.trim().isEmpty()) ? null : "%" + query.trim() + "%";
            ps.setString(1, q); ps.setString(2, q); ps.setString(3, q); ps.setString(4, q);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("sku"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("category"),
                        rs.getString("image_path")
                    ));
                }
            }
        }
        return list;
    }

    public Product findById(int id) throws SQLException {
        String sql = "SELECT id,name,sku,price,stock,category,image_path FROM products WHERE id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("sku"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("category"),
                        rs.getString("image_path")
                    );
                }
            }
        }
        return null;
    }

    public void upsert(Product p) throws SQLException {
        if (p.getId() > 0) {
            String sql = "UPDATE products SET name=?, sku=?, price=?, stock=?, category=?, image_path=? WHERE id=?";
            try (Connection con = DB.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, p.getName());
                ps.setString(2, p.getSku());
                ps.setDouble(3, p.getPrice());
                ps.setInt(4, p.getStock());
                ps.setString(5, p.getCategory());
                ps.setString(6, p.getImagePath());
                ps.setInt(7, p.getId());
                ps.executeUpdate();
            }
        } else {
            String sql = "INSERT INTO products (name, sku, price, stock, category, image_path) VALUES (?,?,?,?,?,?)";
            try (Connection con = DB.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, p.getName());
                ps.setString(2, p.getSku());
                ps.setDouble(3, p.getPrice());
                ps.setInt(4, p.getStock());
                ps.setString(5, p.getCategory());
                ps.setString(6, p.getImagePath());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) p.setId(keys.getInt(1));
                }
            }
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM products WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void decrementStock(int productId, int qty) throws SQLException {
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE products SET stock = GREATEST(stock - ?, 0) WHERE id=?")) {
            ps.setInt(1, qty);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }
}