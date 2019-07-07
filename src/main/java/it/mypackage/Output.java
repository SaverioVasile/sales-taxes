package it.mypackage;

import java.util.ArrayList;

public class Output {

	private ArrayList<OutputEntry> outputEntryList = null;
	private Double salesTaxes = null;
	private Double totalCost = null;
	
	public Output() {
	}
	
	public Output(ArrayList<OutputEntry> outputEntryList, Double[] salesTaxes, Double[] totalCost) {
		this.outputEntryList = outputEntryList;
		this.salesTaxes = salesTaxes[0];
		this.totalCost = totalCost[0];
	}
	
	public ArrayList<OutputEntry> getOutputEntryList() {
		return outputEntryList;
	}
	public void setOutputEntryList(ArrayList<OutputEntry> outputEntryList) {
		this.outputEntryList = outputEntryList;
	}
	public Double getSalesTaxes() {
		return salesTaxes;
	}
	public void setSalesTaxes(Double salesTaxes) {
		this.salesTaxes = salesTaxes;
	}
	public Double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}	
	
}
