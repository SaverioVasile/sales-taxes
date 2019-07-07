package it.mypackage;

public class Input {

	private Integer qty;
	private String item;
	private Double cost;
	private Boolean isImported;
	
    public Input() {
    }
	
    public Input(Integer qty, String item, Double cost, Boolean isImported) {
        this.qty = qty;
        this.item = item;
        this.cost = cost;
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
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Boolean getIsImported() {
		return isImported;
	}
	public void setIsImported(Boolean isImported) {
		this.isImported = isImported;
	}	
}
