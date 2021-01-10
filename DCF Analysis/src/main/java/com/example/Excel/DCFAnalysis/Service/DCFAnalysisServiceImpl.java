package com.example.Excel.DCFAnalysis.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Excel.DCFAnalysis.Models.DCFAnalysisData;
import com.example.Excel.DCFAnalysis.Models.FutureFCF;

@Service
public class DCFAnalysisServiceImpl {
	
	public double DCFAnalysis(DCFAnalysisData getAllData)
	{
		List<FutureFCF> result = EstimateFutureFCF(getAllData);
		
		double npvFcf=result.stream().mapToDouble(list->list.getPvfcf()).sum();
		System.out.println("npv value : "+npvFcf);
		
		double noOfOutstandingShares = 29400;
		
		result.stream().forEach(mylist->{
			System.out.println("year : "+mylist.getYear() +" fcf : "+mylist.getFcf() + " present value : "+mylist.getPvfcf());});

		double tenthYearFcf=result.get(9).getFcf();
		System.out.println("10th year Future FCF is : "+tenthYearFcf);
		
		double terminalValue = CalculateTerminalValue(getAllData.getTerminalGrowthRate(), tenthYearFcf, getAllData.getDiscountRate());
		System.out.println("Terminal value is : "+terminalValue);
		
		double pv = CalculatePresentValueForFCF(9, result, terminalValue, 2016, npvFcf, 7482);
		System.out.println(pv);
		
		double sharePrice=pv/noOfOutstandingShares;
		System.out.println("Intrinsic value : "+sharePrice);
		
		return sharePrice;
	}
	
	private double CalculatePresentValueForFCF(double discountRate, List<FutureFCF> listResult, double terminalValue, int futureYear, double netPvFcf,
			double netDebt) {
		// TODO Auto-generated method stub
		double presentValueOfFcf = 0 ;
		double presentValueOfTerminalValue = 0;
		
		double pVFcfFinal = 0;
		
		int year = 2014;//Calendar.getInstance().get(Calendar.YEAR);
		
		int noOfYears = futureYear-year;
		
		double futureFcf = listResult.stream().filter(list->list.getYear()==futureYear).findFirst().get().getFcf();

		double presentValueOfFcfDenominator = Math.pow((1+(discountRate/100)), noOfYears);
		
		double presentValueOfTerminalValueDenominator = Math.pow((1+(discountRate/100)), 10);
		
		presentValueOfFcf = netPvFcf/presentValueOfFcfDenominator;
		presentValueOfTerminalValue = terminalValue/presentValueOfTerminalValueDenominator;
		System.out.println("present value using discount rate : "+presentValueOfFcf);
		System.out.println("present value of terminal value : "+ presentValueOfTerminalValue);
		System.out.println("future fcf of 2019 : " + futureFcf);
		
		pVFcfFinal = presentValueOfFcf+presentValueOfTerminalValue;
		
		double totalPVofFCF= pVFcfFinal-netDebt;
		
		return totalPVofFCF;
	}

	private double CalculateTerminalValue(double terminalGrowthRate, double tenthYearFcf, double discountRate) {
		// TODO Auto-generated method stub
		double terminalValue = 0 ;
		
		terminalValue = tenthYearFcf *(1+(terminalGrowthRate/100))/((discountRate-terminalGrowthRate)/100);
		return terminalValue;
	}

	public List<FutureFCF> EstimateFutureFCF(DCFAnalysisData data)
	{
		double netfcf = data.getAvgThreeYrFcf();//data.getCashFromOperatingActivities()-data.getCapitalExpenditure();
		int year = Calendar.getInstance().get(Calendar.YEAR);
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

}
