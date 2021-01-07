package com.example.Excel.DCFAnalysis.Models;

public class DCFAnalysisData {
	
	private double netDebt;
	private double growthRate;
	private double terminalGrowthRate;
	private double discountRate;
	//private double cashFromOperatingActivities;
	//private double capitalExpenditure;
	//private double outstandingShares;
	private double avgThreeYrFcf;
	
	public double getAvgThreeYrFcf() {
		return avgThreeYrFcf;
	}
	public double getGrowthRate() {
		return growthRate;
	}
	public void setGrowthRate(double growthRate) {
		this.growthRate = growthRate;
	}
	public void setAvgThreeYrFcf(double avgThreeYrFcf) {
		this.avgThreeYrFcf = avgThreeYrFcf;
	}
	public double getTerminalGrowthRate() {
		return terminalGrowthRate;
	}
	public void setTerminalGrowthRate(double terminalGrowthRate) {
		this.terminalGrowthRate = terminalGrowthRate;
	}
	public double getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}
	public double getNetDebt() {
		return netDebt;
	}
	public void setNetDebt(double netDebt) {
		this.netDebt = netDebt;
	}
	
	
	
	

}
