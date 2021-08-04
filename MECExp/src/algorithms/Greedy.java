package algorithms;

import java.util.ArrayList;
import java.util.Collections;

import objs.BSDistancePair;
import objs.BaseStation;
import utilities.BSUtils;
import utilities.Constants;
import utilities.Utils;

public class Greedy {
	
	public Greedy() {
		super();
	}
	
	public ArrayList<BaseStation> getResult(ArrayList<BaseStation> bsList) 
	{
		ArrayList<BaseStation> result = new ArrayList<>();
		
		while(bsList.size() != 0) {
			getCost(bsList); 
			
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
		
		Utils.printResult(result, "Greedy result: ");
		
		return result;
	}
	
	private void getCost(ArrayList<BaseStation> bsList) 
	{
		for(BaseStation bs : bsList) 
		{
			bs.getTotalCost();
		}
	}
	
}
