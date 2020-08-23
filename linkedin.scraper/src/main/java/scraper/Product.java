package scraper;

public class Product {
	
	private String productName;
	private String qty;
	private String subOrderNumber;
	private String size;
	private String productSKU;
	private	String meeshoProductId;
	private String SLAStatus;
	private String expactedDispatchDate;
	private String imgURL;
	
	

	public Product() {
		
	}
	
	public Product(String pname, String qty, String ordernum, String size, String sku, String pid, String sla, String dispatchDate, String imgURL) {
		this.productName = pname;
		this.qty = qty;
		this.subOrderNumber = ordernum;
		this.size = size;
		this.productSKU = sku;
		this.meeshoProductId = pid;
		this.SLAStatus = sla;
		this.expactedDispatchDate = dispatchDate;
		this.imgURL = imgURL;
	}
	
	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getQty() {
		return qty;
	}


	public void setQty(String qty) {
		this.qty = qty;
	}


	public String getSubOrderNumber() {
		return subOrderNumber;
	}


	public void setSubOrderNumber(String subOrderNumber) {
		this.subOrderNumber = subOrderNumber;
	}


	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public String getProductSKU() {
		return productSKU;
	}


	public void setProductSKU(String productSKU) {
		this.productSKU = productSKU;
	}


	public String getMeeshoProductId() {
		return meeshoProductId;
	}


	public void setMeeshoProductId(String meeshoProductId) {
		this.meeshoProductId = meeshoProductId;
	}


	public String getSLAStatus() {
		return SLAStatus;
	}


	public void setSLAStatus(String sLAStatus) {
		SLAStatus = sLAStatus;
	}


	public String getExpactedDispatchDate() {
		return expactedDispatchDate;
	}


	public void setExpactedDispatchDate(String expactedDispatchDate) {
		this.expactedDispatchDate = expactedDispatchDate;
	}
	
	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}


}

