package objs;

import java.util.ArrayList;

public class Result {
	private String method;
	private ArrayList<Integer> enNum;
	private ArrayList<Integer> cost;
	private ArrayList<Double> time;
	public Result(String method) {
		super();
		this.method = method;
		this.enNum = new ArrayList<>();
		this.cost = new ArrayList<>();
		this.time = new ArrayList<>();
	}
	
	public void addEnNum(int num) 
	{
		this.enNum.add(num);
	}
	
	public void addCost(int cost) 
	{
		this.cost.add(cost);
	}
	
	
	public void addTime(double time) 
	{
		this.time.add(time);
	}
	
	public void printSta() 
	{
		System.out.println("Statistical result");
		int totalEn = 0;
		for(int n : this.enNum)
		{
			totalEn += n;
		}
		System.out.println("Avg en: " + totalEn/this.enNum.size());
		
		int totalCost = 0;
		for(int c : this.cost)
		{
			totalCost += c;
		}
		System.out.println("Avg cost: " + totalEn/this.cost.size());
		
		double totalTime = 0;
		for(double t : this.time)
		{
			totalCost += t;
		}
		System.out.println("Avg time: " + totalEn/this.cost.size());
		
	}
	

}
