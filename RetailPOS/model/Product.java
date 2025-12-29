package model;

public class Product {
	private int id;
	private String name;
	private String sku;
	private double price;
	private int stock;
	private String category;
	private String imagepath;
	
	public Product() {}
	
	public Product(int id, String name,String sku,double price,int stock,String category, String imagepath) {
		this.id = id;
		this.name = name;
		this.sku = sku;
		this.price = price;
		this.stock = stock;
		this.category = category;
		this.imagepath = imagepath;
	}

	public int getId() {return id;}
	public void setId(int id) {this.id = id;}
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	public String getSku() {return sku;}
	public void setSku(String sku) {this.sku = sku;}
	
	public double getPrice() { return price; }
	public void setPrice(double price) { this.price = price; }
	
	public int getStock() { return stock; }
	public void setStock(int stock) { this.stock = stock; }
	
	public String getCategory() { return category; }
	public void setCategory(String category) { this.category = category; }
	
	public String getImagePath() { return imagepath; }
	public void setImagePath(String imagePath) { this.imagepath = imagePath; }
}