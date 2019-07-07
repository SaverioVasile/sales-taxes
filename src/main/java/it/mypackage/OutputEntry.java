package it.mypackage;

public class OutputEntry {

	private Integer qty;
	private String item;
	private Double taxedCost;
	private Boolean isImported;
	
    public OutputEntry() {
    }
	
    public OutputEntry(Integer qty, String item, Double taxedCost, Boolean isImported) {
        this.qty = qty;
        this.item = item;
        this.taxedCost = taxedCost;
        this.isImported = isImported;
    }
	
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public Double getTaxedCost() {
		return taxedCost;
	}
	public void setTaxedCost(Double taxedCost) {
		this.taxedCost = taxedCost;
	}
	public Boolean getIsImported() {
		return isImported;
	}
	public void setIsImported(Boolean isImported) {
		this.isImported = isImported;
	}	
}
