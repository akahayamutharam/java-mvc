package view;

import dao.SaleDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class DashboardView extends JFrame {
    private final SaleDAO saleDAO = new SaleDAO();
    private final JLabel kpiOrders = new JLabel("0");
    private final JLabel kpiRevenue = new JLabel("₹0.00");
    private final JLabel kpiAOV = new JLabel("₹0.00");

    public DashboardView() {
        super("RetailPOS — Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 200);
        setLocationRelativeTo(null);

        JPanel kpiPanel = new JPanel(new GridLayout(1, 3, 8, 8));
        kpiPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        kpiPanel.add(makeKpi("Total Orders", kpiOrders));
        kpiPanel.add(makeKpi("Total Revenue", kpiRevenue));
        kpiPanel.add(makeKpi("Avg Order Value", kpiAOV));

        setLayout(new BorderLayout());
        add(kpiPanel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::refresh);
    }

    private JPanel makeKpi(String title, JLabel value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(new Font("SansSerif", Font.BOLD, 14));
        value.setFont(new Font("SansSerif", Font.BOLD, 18));
        value.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(t, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    private void refresh() {
        try {
            int orders = saleDAO.totalOrders();
            double revenue = saleDAO.totalRevenue();
            double aov = orders > 0 ? revenue / orders : 0.0;

            kpiOrders.setText(String.valueOf(orders));
            kpiRevenue.setText(String.format("₹%.2f", revenue));
            kpiAOV.setText(String.format("₹%.2f", aov));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Dashboard error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
