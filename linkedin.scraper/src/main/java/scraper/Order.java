package scraper;

import java.util.ArrayList;


public class Order {

	public String orderNumber;
	public String orderDate;
	public int productsCount;
	
	ArrayList<Product> productList;
	
	public Order(String onumber, String odate, int pcount){
		this.orderNumber = onumber;
		this.orderDate = odate;
		this.productsCount = pcount;
		productList = new ArrayList<Product>();
	}
	
	public void addProduct(Product p) {
		productList.add(p);
	}
	
	public ArrayList<Product> getAllProducts() {
		return productList;
	}
	
	public int getOrderNumber() {
		return Integer.parseInt(orderNumber);
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public int getProductsCount() {
		return productsCount;
	}

	public void setProductsCount(int productsCount) {
		this.productsCount = productsCount;
	}
	
	
	
	
}
