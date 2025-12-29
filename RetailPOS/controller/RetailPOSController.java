package controller;

import dao.ProductDAO;
import dao.SaleDAO;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Product;
import model.sale;
import view.POSView;

public class RetailPOSController {
    private final POSView view;
    private final ProductDAO productDAO = new ProductDAO();
    private final SaleDAO saleDAO = new SaleDAO();

    public RetailPOSController(POSView view) {
        this.view = view;
        wire();
        loadProducts("");
        renderTotals();
    }

private void wire() 
{
    //  Search functionality
    view.getSearchField().addActionListener(e -> loadProducts(view.getSearchField().getText()));
    view.getSearchButton().addActionListener(e -> loadProducts(view.getSearchField().getText()));
    view.getClearSearchButton().addActionListener(e -> {
        view.getSearchField().setText("");
        loadProducts("");
    });

    // Cart operations
    view.getAddToCartButton().addActionListener(e -> addSelectedProductToCart());
    view.getRemoveFromCartButton().addActionListener(e -> removeSelectedCartItem());

    // Quantity adjustment (+/- buttons)
    view.getIncreaseQtyButton().addActionListener(e -> changeQty(1));
    view.getDecreaseQtyButton().addActionListener(e -> changeQty(-1));

    // Manual recalculation of totals
    view.getRecalculateButton().addActionListener(e -> renderTotals());

    // Finalize sale
    view.getCheckoutButton().addActionListener(e -> checkout());
}

    private void loadProducts(String q) {
        try {
            List<Product> products = productDAO.findAll(q);
            DefaultTableModel tm = (DefaultTableModel) view.getProductTable().getModel();
            tm.setRowCount(0);
            for (Product p : products) {
                tm.addRow(new Object[]{ p.getId(), p.getName(), p.getSku(), p.getCategory(), p.getPrice(), p.getStock(), p.getImagePath() });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Failed to load products: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSelectedProductToCart() {
        int row = view.getProductTable().getSelectedRow();
        if (row < 0) return;
        DefaultTableModel ptm = (DefaultTableModel) view.getProductTable().getModel();
        int id = (int) ptm.getValueAt(row, 0);
        String name = (String) ptm.getValueAt(row, 1);
        String sku = (String) ptm.getValueAt(row, 2);
        double price = (double) ptm.getValueAt(row, 4);
        int stock = (int) ptm.getValueAt(row, 5);

        DefaultTableModel ctm = (DefaultTableModel) view.getCartTable().getModel();
        int existingRow = -1;
        for (int i = 0; i < ctm.getRowCount(); i++) {
            if ((int) ctm.getValueAt(i, 0) == id) { existingRow = i; break; }
        }
        if (existingRow >= 0) {
            int qty = (int) ctm.getValueAt(existingRow, 4);
            if (qty < stock) ctm.setValueAt(qty + 1, existingRow, 4);
        } else {
            ctm.addRow(new Object[]{ id, name, sku, price, 1, stock });
        }
        renderTotals();
    }

    private void removeSelectedCartItem() {
        int row = view.getCartTable().getSelectedRow();
        if (row < 0) return;
        DefaultTableModel ctm = (DefaultTableModel) view.getCartTable().getModel();
        ctm.removeRow(row);
        renderTotals();
    }

   private void changeQty(int delta) {
    int row = view.getCartTable().getSelectedRow();
    if (row < 0) return;

    DefaultTableModel ctm = (DefaultTableModel) view.getCartTable().getModel();
    int qty = (int) ctm.getValueAt(row, 4);
    int stock = (int) ctm.getValueAt(row, 5);

    int newQty = qty + delta;
    if (newQty >= 1 && newQty <= stock) {
        ctm.setValueAt(newQty, row, 4);
        renderTotals();
    } else {
        JOptionPane.showMessageDialog(view, "Quantity must be between 1 and " + stock, "Invalid Qty", JOptionPane.WARNING_MESSAGE);
    }
}

    private double computeSubtotal() {
        DefaultTableModel ctm = (DefaultTableModel) view.getCartTable().getModel();
        double subtotal = 0.0;
        for (int i = 0; i < ctm.getRowCount(); i++) {
            double price = (double) ctm.getValueAt(i, 3);
            int qty = (int) ctm.getValueAt(i, 4);
            subtotal += price * qty;
        }
        return subtotal;
    }

    private void renderTotals() {
        double subtotal = computeSubtotal();
        double discountPct = parseDouble(view.getDiscountField().getText(), 0.0);
        double taxPct = parseDouble(view.getTaxField().getText(), 18.0);
        double discountAmt = subtotal * discountPct / 100.0;
        double taxedBase = Math.max(0.0, subtotal - discountAmt);
        double taxAmt = taxedBase * taxPct / 100.0;
        double total = taxedBase + taxAmt;

        view.getSubtotalLabel().setText(String.format("₹%.2f", subtotal));
        view.getDiscountLabel().setText(String.format("₹%.2f", discountAmt));
        view.getTaxLabel().setText(String.format("₹%.2f", taxAmt));
        view.getTotalLabel().setText(String.format("₹%.2f", total));
    }

    private double parseDouble(String s, double def) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return def; }
    }

    private void checkout() {
        DefaultTableModel ctm = (DefaultTableModel) view.getCartTable().getModel();
        if (ctm.getRowCount() == 0) return;

        // Lock discount and tax fields
        view.getDiscountField().setEditable(false);
        view.getTaxField().setEditable(false);

        double subtotal = computeSubtotal();
        double discountPct = parseDouble(view.getDiscountField().getText(), 0.0);
        double taxPct = parseDouble(view.getTaxField().getText(), 18.0);
        double discountAmt = subtotal * discountPct / 100.0;
        double taxedBase = Math.max(0.0, subtotal - discountAmt);
        double taxAmt = taxedBase * taxPct / 100.0;
        double total = taxedBase + taxAmt;

        sale Sale = new sale();
        Sale.setSubtotal(subtotal);
        Sale.setDiscountPot(discountPct);
        Sale.setDiscountAmt(discountAmt);
        Sale.setTaxPot(taxPct);
        Sale.setTaxAmt(taxAmt);
        Sale.setTotal(total);

        for (int i = 0; i < ctm.getRowCount(); i++) {
            int productId = (int) ctm.getValueAt(i, 0);
            String name = (String) ctm.getValueAt(i, 1);
            String sku = (String) ctm.getValueAt(i, 2);
            double price = (double) ctm.getValueAt(i, 3);
            int qty = (int) ctm.getValueAt(i, 4);
            Sale.getItems().add(new sale.Item(productId, name, sku, qty, price));
        }

        try {
            int saleId = saleDAO.createSale(Sale);
            for (sale.Item it : Sale.getItems()) {
                productDAO.decrementStock(it.getProductId(), it.getQty());
            }
            JOptionPane.showMessageDialog(view, "Sale completed. ID: " + saleId, "Checkout", JOptionPane.INFORMATION_MESSAGE);
            ctm.setRowCount(0);

            // Reset discount/tax fields
            view.getDiscountField().setEditable(true);
            view.getTaxField().setEditable(true);
            view.getDiscountField().setText("0");
            view.getTaxField().setText("18");

            renderTotals();
            loadProducts(view.getSearchField().getText());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Checkout failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
