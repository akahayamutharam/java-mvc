package view;

import controller.RetailPOSController;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class POSView extends JFrame {
    private final JTable productTable = new JTable(new DefaultTableModel(
        new Object[]{"ID","Name","SKU","Category","Price","Stock","Image"}, 0
    ) {
        public Class<?> getColumnClass(int col) {
            return switch (col) {
                case 0, 5 -> Integer.class;
                case 4 -> Double.class;
                default -> String.class;
            };
        }
        public boolean isCellEditable(int r, int c) { return false; }
    });

    private final JTable cartTable = new JTable(new DefaultTableModel(
        new Object[]{"ID","Name","SKU","Price","Qty","Stock"}, 0
    ) {
        public Class<?> getColumnClass(int col) {
            return switch (col) {
                case 0, 4, 5 -> Integer.class;
                case 3 -> Double.class;
                default -> String.class;
            };
        }
        public boolean isCellEditable(int r, int c) { return false; }
    });

    private final JTextField searchField = new JTextField();
    private final JButton searchButton = new JButton("Search");
    private final JButton clearSearchButton = new JButton("Clear");

    private final JButton addToCartButton = new JButton("Add to Cart");
    private final JButton removeFromCartButton = new JButton("Remove");
    private final JButton increaseQtyButton = new JButton("+");
    private final JButton decreaseQtyButton = new JButton("-");

    private final JTextField discountField = new JTextField("0");
    private final JTextField taxField = new JTextField("18");
    private final JButton recalculateButton = new JButton("Recalculate Totals");

    private final JLabel subtotalLabel = new JLabel("₹0.00");
    private final JLabel discountLabel = new JLabel("₹0.00");
    private final JLabel taxLabel = new JLabel("₹0.00");
    private final JLabel totalLabel = new JLabel("₹0.00");
    private final JButton checkoutButton = new JButton("Checkout");

    private RetailPOSController controller;

    public POSView(User user) {
        super("RetailPOS — POS (" + user.getUsername() + " : " + user.getRole() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.6);

        JPanel left = new JPanel(new BorderLayout(8,8));
        JPanel searchPanel = new JPanel(new BorderLayout(6,6));
        searchPanel.add(searchField, BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(searchButton); buttons.add(clearSearchButton);
        searchPanel.add(buttons, BorderLayout.EAST);

        productTable.setRowHeight(24);
        left.add(searchPanel, BorderLayout.NORTH);
        left.add(new JScrollPane(productTable), BorderLayout.CENTER);
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addPanel.add(addToCartButton);
        left.add(addPanel, BorderLayout.SOUTH);

        JPanel right = new JPanel(new BorderLayout(8,8));
        cartTable.setRowHeight(24);
        right.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        qtyPanel.add(decreaseQtyButton);
        qtyPanel.add(increaseQtyButton);
        qtyPanel.add(removeFromCartButton);

        JPanel totalsPanel = new JPanel(new GridLayout(0,2,8,8));
        totalsPanel.add(new JLabel("Discount (%)")); totalsPanel.add(discountField);
        totalsPanel.add(new JLabel("Tax (%)")); totalsPanel.add(taxField);
        totalsPanel.add(recalculateButton); totalsPanel.add(new JLabel()); // empty cell
        totalsPanel.add(new JLabel("Subtotal")); totalsPanel.add(subtotalLabel);
        totalsPanel.add(new JLabel("Discount")); totalsPanel.add(discountLabel);
        totalsPanel.add(new JLabel("Tax")); totalsPanel.add(taxLabel);
        totalsPanel.add(new JLabel("Total")); totalsPanel.add(totalLabel);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(qtyPanel, BorderLayout.WEST);
        bottom.add(totalsPanel, BorderLayout.CENTER);
        JPanel checkoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        checkoutPanel.add(checkoutButton);
        bottom.add(checkoutPanel, BorderLayout.SOUTH);

        right.add(bottom, BorderLayout.SOUTH);

        split.setLeftComponent(left);
        split.setRightComponent(right);

        setLayout(new BorderLayout());
        add(split, BorderLayout.CENTER);

        controller = new RetailPOSController(this);
    }

    public JTable getProductTable() { return productTable; }
    public JTable getCartTable() { return cartTable; }
    public JTextField getSearchField() { return searchField; }
    public JButton getSearchButton() { return searchButton; }
    public JButton getClearSearchButton() { return clearSearchButton; }
    public JButton getAddToCartButton() { return addToCartButton; }
    public JButton getRemoveFromCartButton() { return removeFromCartButton; }
    public JButton getIncreaseQtyButton() { return increaseQtyButton; }
    public JButton getDecreaseQtyButton() { return decreaseQtyButton; }
    public JTextField getDiscountField() { return discountField; }
    public JTextField getTaxField() { return taxField; }
    public JButton getRecalculateButton() { return recalculateButton; }
    public JLabel getSubtotalLabel() { return subtotalLabel; }
    public JLabel getDiscountLabel() { return discountLabel; }
    public JLabel getTaxLabel() { return taxLabel; }
    public JLabel getTotalLabel() { return totalLabel; }
    public JButton getCheckoutButton() { return checkoutButton; }
}
