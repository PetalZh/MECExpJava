package algorithms;

import java.util.ArrayList;
import java.util.Collections;

import objs.BaseStation;
import utilities.BSUtils;
import utilities.Constants;

public class Greedy {
	
	public Greedy() {
		super();
	}
	
	public ArrayList<BaseStation> getResult(ArrayList<BaseStation> bsList) 
	{
		ArrayList<BaseStation> result = new ArrayList<>();
		while(bsList.size() != 0) {
			Collections.sort(bsList);
			BaseStation candidate = bsList.get(0);
			result.add(candidate);
			
			// remove connected bs from bsList
			ArrayList<BaseStation> conn = candidate.getAssignedBS();
			for(BaseStation bs : conn) {
				bsList.remove(bs);
			}
			
			bsList.remove(candidate);
			BSUtils.getBSConnection(bsList);
			
			// recompute the connection
		}
		
		printResult(result);
		
		return result;
	}
	
	private void printResult(ArrayList<BaseStation> result) 
	{
		System.out.println("Greedy result: " + result.size() + " items");
		int totalCost = 0; 
		for(BaseStation bs : result) 
		{
			
			int totalTaskNum = bs.getCTMax();
			for(BaseStation i : bs.getAssignedBS()) 
			{
				totalTaskNum += i.getCTMax();
			}
			
			totalCost += Constants.COST_EN;
			int serverNum = (int)Math.ceil((totalTaskNum * Constants.SINGLE_TASK_SIZE) / Constants.SINGLE_SERVER_CAPACITY);
			totalCost += serverNum * Constants.SINGLE_SERVER_CAPACITY;
			System.out.println(bs.getLocation() + " " + totalTaskNum);
			
		}
		
		System.out.println("Total Cost: " + totalCost);
	}




}
