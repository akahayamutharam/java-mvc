package view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;

import dao.AccountDAO;

public class SummaryDialog extends JDialog {
    public SummaryDialog(Frame owner, Connection con, int accountId) {
        super(owner, "Transaction Summary", true);
        setSize(500, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);

        try {
            AccountDAO dao = new AccountDAO(new dao.DBHelper());
            try (ResultSet rs = dao.summary(con, accountId)) {
                if (rs.next()) {
                    String text = String.format(
                        "Account ID: %d\nName: %s\nType: %s\nBalance: %.2f\n\nTotal Deposit: %.2f\nTotal Withdraw: %.2f\nTotal Transfer: %.2f",
                        rs.getInt("id"), rs.getString("name"), rs.getString("account_type"),
                        rs.getDouble("balance"), rs.getDouble("total_deposit"),
                        rs.getDouble("total_withdraw"), rs.getDouble("total_transfer"));
                    area.setText(text);
                } else {
                    area.setText("No summary found.");
                }
            }
        } catch (Exception e) {
            area.setText("Error: " + e.getMessage());
        }
    }
}
