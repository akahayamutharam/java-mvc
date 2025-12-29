package model;

import java.util.ArrayList;
import java.util.List;

public class sale {
	public static class Item{
		private int productId;
		private String name;
		private String sku;
		private int qty;
		private double price;
		
		public Item(int productId, String name, String sku,int qty, double price) {
			this.productId = productId;
			this.name = name;
			this.sku = sku;
			this.qty = qty;
			this.price = price;
		}
		
		public int getProductId() {return productId;}
		public String getName() {return name;}
		public String getSku() {return sku;}
		public int getQty() {return qty;}
		public double getPrice() {return price;}
		
	}

	private int id;
	private long tsMillis;
	private final List<Item> items = new ArrayList<>();
	private double subtotal;
	private double discountPot;
	private double discountAmt;
	private double taxPot;
	private double taxAmt;
	private double total;
	
	public int getId() {return id;}
	public void setId(int id) {this.id = id;}
	
	public long getTsMillis() {return tsMillis;}
	public void setTsMillis(long tsMillis) {this.tsMillis = tsMillis;}
	
	public List<Item> getItems() {return items;}
	
	
	public double getSubtotal() { return subtotal; }
	public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
	
	public double getDiscountPot() { return discountPot; }
	public void setDiscountPot(double discountPot) { this.discountPot = discountPot; }
	
	public double getDiscountAmt() { return discountAmt; }
	public void setDiscountAmt(double discountAmt) { this.discountAmt = discountAmt; }
	
	public double getTaxPot() { return taxPot; }
	public void setTaxPot(double taxPot) { this.taxPot = taxPot; }
	
	public double getTaxAmt() { return taxAmt; }
	public void setTaxAmt(double taxAmt) { this.taxAmt = taxAmt; }
	
	public double getTotal() { return total; }
	public void setTotal(double total) { this.total = total; }
	
}	
	
	
	
	
	
	
		
		
		