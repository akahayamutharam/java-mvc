package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.sale;

public class SaleDAO {

    public int createSale(sale sale) throws SQLException {
        String sqlSale = "INSERT INTO sales (subtotal, discount_pot, discount_amt, tax_pot, tax_amt, total) VALUES (?,?,?,?,?,?)";
        String sqlItem = "INSERT INTO sale_items (sale_id, product_id, qty, price) VALUES (?,?,?,?)";

        try (Connection con = DB.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psSale = con.prepareStatement(sqlSale, Statement.RETURN_GENERATED_KEYS)) {
                psSale.setDouble(1, sale.getSubtotal());
                psSale.setDouble(2, sale.getDiscountPot());
                psSale.setDouble(3, sale.getDiscountAmt());
                psSale.setDouble(4, sale.getTaxPot());
                psSale.setDouble(5, sale.getTaxAmt());
                psSale.setDouble(6, sale.getTotal());
                psSale.executeUpdate();

                int saleId;
                try (ResultSet keys = psSale.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("No sale ID generated");
                    saleId = keys.getInt(1);
                }

                try (PreparedStatement psItem = con.prepareStatement(sqlItem)) {
                    for (sale.Item it : sale.getItems()) {
                        psItem.setInt(1, saleId);
                        psItem.setInt(2, it.getProductId());
                        psItem.setInt(3, it.getQty());
                        psItem.setDouble(4, it.getPrice());
                        psItem.addBatch();
                    }
                    psItem.executeBatch();
                }

                con.commit();
                return saleId;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public List<double[]> revenueByDay(int days) throws SQLException {
        String sql = "SELECT DATE(ts) AS d, SUM(total) AS revenue FROM sales " +
                     "WHERE ts >= DATE_SUB(CURDATE(), INTERVAL ? DAY) GROUP BY d ORDER BY d";
        List<double[]> rows = new ArrayList<>();
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, days);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date d = rs.getDate("d");
                    double revenue = rs.getDouble("revenue");
                    rows.add(new double[]{ d.getTime(), revenue });
                }
            }
        }
        return rows;
    }

    public double totalRevenue() throws SQLException {
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COALESCE(SUM(total),0) AS t FROM sales");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble("t") : 0.0;
        }
    }

    public int totalOrders() throws SQLException {
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS c FROM sales");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt("c") : 0;
        }
    }
}
