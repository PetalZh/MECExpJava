package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import objs.BSDistancePair;
import objs.BaseStation;
import utilities.BSUtils;

public class GreedyCan {
	
	public GreedyCan() 
	{
		super();
	}
	
	public ArrayList<BaseStation> getResult(ArrayList<BaseStation> bsList, int thres) 
	{
		
			thres = 10;
			ArrayList<BaseStation> candidates = new ArrayList<>();
			ArrayList<BaseStation> enList = new ArrayList<>();
			//ArrayList<BaseStation> current = new ArrayList<>();
			
			while(bsList.size() != 0) 
			{
				BaseStation en = null;
				
				Collections.sort(bsList);
				if(candidates.size() != 0) 
				{
					Collections.sort(candidates);
					if(candidates.get(0).getWorkload() > bsList.get(0).getWorkload()) 
					{
						en = candidates.get(0);
						// remove from previous EN assign
						// update EN workload req
					}else {
						en = bsList.get(0);
					}	
				}else {
					en = bsList.get(0);
				}
				
				
				enList.add(en);
				
				ArrayList<BSDistancePair> conn = en.getAssignedBS();
				
				for(BSDistancePair bs : conn) {
					bsList.remove(bs.getBS());
				}
				
				// get candidates according to threshold
				Collections.sort(conn);
				
				Iterator<BSDistancePair> iter = conn.iterator();
				int counter = 0;
				while(iter.hasNext()) 
				{
					candidates.add(iter.next().getBS());
					if(counter == thres) 
					{
						break;
					}
				}
				
				updateCandidateAssignment(candidates, conn);
				
				
				
				bsList.remove(en);
				BSUtils.getBSConnection(bsList);
			}
			
			return enList;
	}
	
	private void updateCandidateAssignment(ArrayList<BaseStation> candidates, ArrayList<BSDistancePair> assignedBSList) 
	{ 
		for(BaseStation bs : candidates) 
		{
			for(BSDistancePair cbs : assignedBSList) 
			{
				bs.removeBS(cbs.getBS());
			}
		}
		
	}
	


}
