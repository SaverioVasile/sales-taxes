package it.mypackage;

public class Product {
	
	private String name;
	private String type;
	private Boolean isTaxFree;
	
	public Product() {
	}
	
	public Product(String name, String type, Boolean isTaxFree) {
		this.name = name;
		this.type = type;
		this.isTaxFree = isTaxFree;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getIsTaxFree() {
		return isTaxFree;
	}
	public void setIsTaxFree(Boolean isTaxFree) {
		this.isTaxFree = isTaxFree;
	}

}
