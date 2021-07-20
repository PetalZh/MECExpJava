package algorithms;

import java.util.ArrayList;
import java.util.Collections;

import objs.BSDistancePair;
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
			ArrayList<BSDistancePair> conn = candidate.getAssignedBS();
			for(BSDistancePair bs : conn) {
				bsList.remove(bs.getBS());
			}
			
			bsList.remove(candidate);
			
			// recompute the connection
			BSUtils.getBSConnection(bsList);
			
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
			
//			int totalTaskNum = bs.getCTMax();
//			for(BSDistancePair i : bs.getAssignedBS()) 
//			{
//				totalTaskNum += i.getBS().getCTMax();
//			}
			
			totalCost += Constants.COST_EN;
			int serverNum = (int)Math.ceil((bs.getWorkload()) / Constants.SINGLE_SERVER_CAPACITY);
			
			totalCost += serverNum * Constants.SINGLE_SERVER_CAPACITY;
			
			System.out.println(bs.getLocation() + " " + bs.getWorkload());
			//System.out.println(bs.getLocation() + " " + totalTaskNum);
		}
		
		System.out.println("Total Cost: " + totalCost);
	}




}
