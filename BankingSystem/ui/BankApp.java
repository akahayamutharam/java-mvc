package ui;

import model.BankAccount;
import dao.BankDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BankApp extends JFrame {
    BankDAO dao = new BankDAO();
    List<BankAccount> accounts = new ArrayList<>();
    DefaultTableModel model;
    TableRowSorter<DefaultTableModel> sorter;
    JTable table;
    JTextField nameField, balanceField, searchField;
    JComboBox<String> typeBox, sortColumnBox, sortOrderBox;
    JCheckBox activeBox;
    JButton addBtn, updateBtn, deleteBtn, searchBtn;
    int selectedId = -1;

    public BankApp() {
        setTitle("Banking Management System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel form = new JPanel(new GridLayout(0, 2));
        nameField = new JTextField();
        balanceField = new JTextField();
        typeBox = new JComboBox<>(new String[]{"Savings", "Current"});
        activeBox = new JCheckBox("Active");

        form.add(new JLabel("Name")); form.add(nameField);
        form.add(new JLabel("Account Type")); form.add(typeBox);
        form.add(new JLabel("Balance")); form.add(balanceField);
        form.add(new JLabel("Status")); form.add(activeBox);
        add(form, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Name", "Type", "Balance", "Active"}, 0);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Controls Panel
        JPanel controls = new JPanel();
        addBtn = new JButton("Add");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        searchField = new JTextField(15);
        searchBtn = new JButton("Search");
        sortColumnBox = new JComboBox<>(new String[]{"Name", "Account Type", "Balance"});
        sortOrderBox = new JComboBox<>(new String[]{"Ascending", "Descending"});

        controls.add(addBtn); controls.add(updateBtn); controls.add(deleteBtn);
        controls.add(new JLabel("Search by Name:")); controls.add(searchField); controls.add(searchBtn);
        controls.add(new JLabel("Sort By:")); controls.add(sortColumnBox);
        controls.add(new JLabel("Order:")); controls.add(sortOrderBox);
        add(controls, BorderLayout.SOUTH);

        // Load initial data
        loadTable();

        // Add Button
        addBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty() || balanceField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }
            BankAccount acc = new BankAccount(0, nameField.getText(), typeBox.getSelectedItem().toString(),
                    Double.parseDouble(balanceField.getText()), activeBox.isSelected());
            dao.insertAccount(acc);
            loadTable();
            clearForm();
        });

        // Update Button
        updateBtn.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Select a row to update.");
                return;
            }
            BankAccount acc = new BankAccount(selectedId, nameField.getText(), typeBox.getSelectedItem().toString(),
                    Double.parseDouble(balanceField.getText()), activeBox.isSelected());
            dao.updateAccount(acc);
            loadTable();
            clearForm();
        });

        // Delete Button
        deleteBtn.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Select a row to delete.");
                return;
            }
            dao.deleteAccount(selectedId);
            loadTable();
            clearForm();
        });

        // Search Button
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            List<BankAccount> filtered = accounts.stream()
                .filter(acc -> acc.name.toLowerCase().contains(keyword))
                .collect(Collectors.toList());
            updateTable(filtered);
        });

        // Sorting
        sortColumnBox.addActionListener(e -> applySort());
        sortOrderBox.addActionListener(e -> applySort());

        // Table Click
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                selectedId = Integer.parseInt(model.getValueAt(row, 0).toString());
                nameField.setText(model.getValueAt(row, 1).toString());
                typeBox.setSelectedItem(model.getValueAt(row, 2).toString());
                balanceField.setText(model.getValueAt(row, 3).toString());
                activeBox.setSelected((boolean) model.getValueAt(row, 4));
            }
        });

        setVisible(true);
    }

    void loadTable() {
        accounts = dao.getAllAccounts();
        updateTable(accounts);
    }

    void updateTable(List<BankAccount> list) {
        model.setRowCount(0);
        for (BankAccount acc : list) {
            model.addRow(new Object[]{acc.id, acc.name, acc.accountType, acc.balance, acc.active});
        }
    }

    void applySort() {
        int columnIndex = switch (sortColumnBox.getSelectedItem().toString()) {
            case "Name" -> 1;
            case "Account Type" -> 2;
            case "Balance" -> 3;
            default -> 1;
        };

        SortOrder order = sortOrderBox.getSelectedItem().equals("Ascending") ?
                SortOrder.ASCENDING : SortOrder.DESCENDING;

        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(columnIndex, order));
        sorter.setSortKeys(sortKeys);
    }

    void clearForm() {
        nameField.setText("");
        balanceField.setText("");
        typeBox.setSelectedIndex(0);
        activeBox.setSelected(false);
        selectedId = -1;
    }

    public static void main(String[] args) {
        new BankApp();
    }
}