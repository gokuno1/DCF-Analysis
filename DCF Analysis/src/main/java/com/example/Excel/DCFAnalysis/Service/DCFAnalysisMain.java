package com.example.Excel.DCFAnalysis.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.formula.ptg.PowerPtg;

import com.example.Excel.DCFAnalysis.Models.DCFAnalysisData;
import com.example.Excel.DCFAnalysis.Models.FutureFCF;

public class DCFAnalysisMain {
	
	public static List<FutureFCF> EstimateFutureFCF(DCFAnalysisData data)
	{
		double netfcf = data.getAvgThreeYrFcf();//data.getCashFromOperatingActivities()-data.getCapitalExpenditure();
		int year = 2015;
		double fcf = 0.0;
		double pvFcf = 0.0;
	
		List<FutureFCF> futureFcf = new ArrayList<FutureFCF>();
		
		for(int i=1;i<=10;i++)
		{
			if(i>5)
			{
				FutureFCF future = new FutureFCF();
				fcf=netfcf*(1+(data.getGrowthRate1()/100));
				double denominator = Math.pow((1+(data.getDiscountRate()/100)), i);
				pvFcf = fcf/denominator;
				System.out.println("fcf calculated "+fcf);
				future.setFcf(fcf);
				future.setYear(year);
				future.setPvfcf(pvFcf);
				futureFcf.add(future);
				netfcf=fcf;
				year=year+1;
				
			}
			else
			{
				FutureFCF future = new FutureFCF();
				fcf=netfcf*(1+(data.getGrowthRate()/100));
				double denominator = Math.pow((1+(data.getDiscountRate()/100)), i);
				pvFcf = fcf/denominator;
				System.out.println("fcf calculated "+fcf);
				future.setFcf(fcf);
				future.setYear(year);
				future.setPvfcf(pvFcf);
				futureFcf.add(future);
				netfcf=fcf;
				year=year+1;
			}
		}
		//System.out.println(futureFcf);
		return futureFcf;		
	}

	public static double CalculateTerminalValue(double terminalGrowthRate, double tenthFCF, double discountRate)
	{
		double terminalValue = 0 ;
		
		terminalValue = tenthFCF *(1+(terminalGrowthRate/100))/((discountRate-terminalGrowthRate)/100);
		return terminalValue;
	}
	
	public static double CalculatePresentValueForFCF(double discountRate, List<FutureFCF> listResult, double terminalValue, int futureYear, double netPvFcf,
			double netDebt)
	{
		double presentValueOfFcf = 0 ;
		double presentValueOfTerminalValue = 0;
		
		double pVFcfFinal = 0;
		
		double totalPVofFCF = 0;
		
		int year = 2014;//Calendar.getInstance().get(Calendar.YEAR);
		
		int noOfYears = futureYear-year;
		
		double futureFcf = listResult.stream().filter(list->list.getYear()==futureYear).findFirst().get().getFcf();

		double presentValueOfFcfDenominator = Math.pow((1+(discountRate/100)), noOfYears);
		
		double presentValueOfTerminalValueDenominator = Math.pow((1+(discountRate/100)), 10);
		
		double npv = listResult.stream().mapToDouble(list -> list.getPvfcf()).sum();
		System.out.println("sum of all Net Present value : "+npv);
		
		presentValueOfFcf = netPvFcf/presentValueOfFcfDenominator;
		presentValueOfTerminalValue = terminalValue/presentValueOfTerminalValueDenominator;
	//	System.out.println("present value using discount rate : "+presentValueOfFcf);
		System.out.println("present value of terminal value : "+ presentValueOfTerminalValue);
	//	System.out.println("future fcf of 2019 : " + futureFcf);
		
		pVFcfFinal = presentValueOfTerminalValue+npv;
		System.out.println("sum of present value of cash flows: "+pVFcfFinal);
		totalPVofFCF= pVFcfFinal-netDebt;
		System.out.println("total present value of free cash flow : "+totalPVofFCF);
		return totalPVofFCF;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DCFAnalysisData getAllData = new DCFAnalysisData();
		getAllData.setAvgThreeYrFcf(140.36);
		getAllData.setGrowthRate(18);
		getAllData.setGrowthRate1(10);
		getAllData.setDiscountRate(9);
		getAllData.setTerminalGrowthRate(3.5);
		List<FutureFCF> result = EstimateFutureFCF(getAllData);
		
		double npvFcf=result.stream().mapToDouble(list->list.getPvfcf()).sum();
		
		double noOfOutstandingShares = 17.081;
		
		result.stream().forEach(mylist->{
			System.out.println("year : "+mylist.getYear() +" fcf : "+mylist.getFcf() + " present value : "+mylist.getPvfcf());});

		double tenthYearFcf=result.get(9).getFcf();
		System.out.println("10th year Future FCF is : "+tenthYearFcf);
		
		double terminalValue = CalculateTerminalValue(getAllData.getTerminalGrowthRate(), tenthYearFcf, getAllData.getDiscountRate());
		System.out.println("Terminal value is : "+terminalValue);
		
		double pv = CalculatePresentValueForFCF(9, result, terminalValue, 2016, npvFcf, -218.6);
		System.out.println(pv);
		
		double sharePrice=pv/noOfOutstandingShares;
		System.out.println("Intrinsic value : "+sharePrice);
	}

}
