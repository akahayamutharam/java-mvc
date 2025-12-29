package view;

import dao.ProductDAO;
import model.Product;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AdminView extends JFrame {
    private final ProductDAO productDAO = new ProductDAO();

    private final JTextField nameField = new JTextField();
    private final JTextField skuField = new JTextField();
    private final JTextField priceField = new JTextField();
    private final JTextField stockField = new JTextField();
    private final JTextField categoryField = new JTextField();
    private final JTextField imagePathField = new JTextField();
    private final JButton saveButton = new JButton("Save");
    private final JButton resetButton = new JButton("Reset");
    private final JButton deleteButton = new JButton("Delete");
    private final JTextField searchField = new JTextField();
    private final JButton searchBtn = new JButton("Search");
    private final JButton clearBtn = new JButton("Clear");

    private final JTable table = new JTable(new DefaultTableModel(
        new Object[]{"ID","Name","SKU","Price","Stock","Category","Image"}, 0
    ) {
        public Class<?> getColumnClass(int col) {
            switch (col) { case 0: case 4: return Integer.class; case 3: return Double.class; default: return String.class; }
        }
        public boolean isCellEditable(int r, int c) { return false; }
    });

    public AdminView(User user) {
        super("RetailPOS — Admin (" + user.getUsername() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(0,2,6,6));
        form.add(new JLabel("Name")); form.add(nameField);
        form.add(new JLabel("SKU")); form.add(skuField);
        form.add(new JLabel("Price (₹)")); form.add(priceField);
        form.add(new JLabel("Stock")); form.add(stockField);
        form.add(new JLabel("Category")); form.add(categoryField);
        form.add(new JLabel("Image Path")); form.add(imagePathField);

        JPanel formBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        formBtns.add(saveButton); formBtns.add(resetButton); formBtns.add(deleteButton);

        JPanel searchPanel = new JPanel(new BorderLayout(6,6));
        searchPanel.add(searchField, BorderLayout.CENTER);
        JPanel sb = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sb.add(searchBtn); sb.add(clearBtn);
        searchPanel.add(sb, BorderLayout.EAST);

        setLayout(new BorderLayout(8,8));
        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.CENTER);
        south.add(formBtns, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);

        searchBtn.addActionListener(e -> load(searchField.getText()));
        clearBtn.addActionListener(e -> { searchField.setText(""); load(""); });

        resetButton.addActionListener(e -> {
            nameField.setText(""); skuField.setText(""); priceField.setText("");
            stockField.setText(""); categoryField.setText(""); imagePathField.setText("");
            table.clearSelection();
        });

        saveButton.addActionListener(e -> save());
        deleteButton.addActionListener(e -> deleteSelected());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = table.getSelectedRow();
            if (row < 0) return;
            DefaultTableModel tm = (DefaultTableModel) table.getModel();
            nameField.setText((String) tm.getValueAt(row, 1));
            skuField.setText((String) tm.getValueAt(row, 2));
            priceField.setText(String.valueOf(tm.getValueAt(row, 3)));
            stockField.setText(String.valueOf(tm.getValueAt(row, 4)));
            categoryField.setText((String) tm.getValueAt(row, 5));
            imagePathField.setText((String) tm.getValueAt(row, 6));
        });

        load("");
    }

    private void load(String q) {
        try {
            List<Product> products = productDAO.findAll(q);
            DefaultTableModel tm = (DefaultTableModel) table.getModel();
            tm.setRowCount(0);
            for (Product p : products) {
                tm.addRow(new Object[]{ p.getId(), p.getName(), p.getSku(), p.getPrice(), p.getStock(), p.getCategory(), p.getImagePath() });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Load failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void save() {
        try {
            Product p = new Product();
            int row = table.getSelectedRow();
            if (row >= 0) p.setId((int) table.getValueAt(row, 0));
            p.setName(nameField.getText().trim());
            p.setSku(skuField.getText().trim());
            p.setPrice(Double.parseDouble(priceField.getText().trim()));
            p.setStock(Integer.parseInt(stockField.getText().trim()));
            p.setCategory(categoryField.getText().trim());
            p.setImagePath(imagePathField.getText().trim());
            productDAO.upsert(p);
            JOptionPane.showMessageDialog(this, "Saved", "Info", JOptionPane.INFORMATION_MESSAGE);
            load(searchField.getText());
            resetButton.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Save failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = (int) table.getValueAt(row, 0);
        try {
            productDAO.delete(id);
            JOptionPane.showMessageDialog(this, "Deleted", "Info", JOptionPane.INFORMATION_MESSAGE);
            load(searchField.getText());
            resetButton.doClick();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Delete failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
