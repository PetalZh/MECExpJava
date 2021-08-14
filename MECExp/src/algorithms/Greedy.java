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
	
	private void test_print_list(ArrayList<BaseStation> list) 
	{
		for(BaseStation bs : list) 
		{
			System.out.println("id: " + bs.getLocation() + " " + bs.getWorkload()); // + " " + bs.getCTMax()
			for(BSDistancePair p: bs.getAssignedBS()) 
			{
				//System.out.println(" " + p.getBS().getLocation() + " " + p.getBS().getWorkload()); //+ " " + p.getBS().getCTMax()
			}
			
		}
	}
	
	public ArrayList<BaseStation> getResult(ArrayList<BaseStation> bsList) 
	{
		ArrayList<BaseStation> result = new ArrayList<>();
		//test_print_list(bsList); 
		
		while(bsList.size() != 0) {
//			System.out.println("**********************");
//			test_print_list(result);
			
			Collections.sort(bsList, BaseStation.getCapacityComparator());
			//Collections.sort(bsList, BaseStation.getConnectionComparator());

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
