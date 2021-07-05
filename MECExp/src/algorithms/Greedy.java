package algorithms;

import java.util.ArrayList;
import java.util.Collections;

import objs.BaseStation;

public class Greedy {
	
	public Greedy() {
		super();
	}
	
	public ArrayList<BaseStation> getSolution(ArrayList<BaseStation> bsList) 
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
			
			// recompute the connection
		}
		
		return result;
	}




}
