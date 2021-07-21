package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import objs.BSDistancePair;
import objs.BaseStation;
import objs.Connection;
import utilities.BSUtils;
import utilities.Constants;
import utilities.Utils;

public class GreedyCan {
	private Hashtable<String, Connection> bs_en_table; 
	
	public GreedyCan() 
	{
		super();
		bs_en_table = new Hashtable<>();
	}
	
	public ArrayList<BaseStation> getResult(ArrayList<BaseStation> bsList, int thres) 
	{
		
			ArrayList<BaseStation> candidates = new ArrayList<>();
			ArrayList<BaseStation> enList = new ArrayList<>();
			
			while(bsList.size() != 0) 
			{
				BaseStation en = null;
				
				Collections.sort(bsList);
				//System.out.println("test "+candidates.size());
				
				if(candidates.size() != 0) 
				{
					Collections.sort(candidates);
					if(candidates.get(0).getWorkload() > bsList.get(0).getWorkload()) 
					{
						en = candidates.get(0);
						updateCandidateBSAsEN(en);
						candidates.remove(en);
					}else {
						en = bsList.get(0);
						bsList.remove(en);
					}	
				}else {
					en = bsList.get(0);
					bsList.remove(en);
				}
				
				
				enList.add(en);
				updateTable(en);
				
				// remove bs from bsList
				ArrayList<BSDistancePair> conn = en.getAssignedBS();
				
				for(BSDistancePair bs : conn) {
					bsList.remove(bs.getBS());
					candidates.remove(bs.getBS());
				}
				
				System.out.println("BS list size: " + bsList.size());
				
				// get candidates according to threshold
				Collections.sort(conn);
				
				Iterator<BSDistancePair> iter = conn.iterator();
				int counter = 0;
				while(iter.hasNext()) 
				{
					counter++;
					candidates.add(iter.next().getBS());
					if(counter == thres) 
					{
						break;
					}
				}
				
				updateCandidateAssignment(candidates);
				
				BSUtils.getBSConnection(bsList);
			}
			printResult(enList);
			return enList;
	}
	
	private void printResult(ArrayList<BaseStation> result) 
	{
		System.out.println("Greedy with candidate list result: " + result.size() + " items");
		int totalCost = 0; 
		for(BaseStation bs : result) 
		{
			totalCost += Constants.COST_EN;
			int serverNum = (int)Math.ceil((bs.getWorkload()) / Constants.SINGLE_SERVER_CAPACITY);
			
			totalCost += serverNum * Constants.SINGLE_SERVER_CAPACITY;
			
			System.out.println(bs.getLocation() + " " + bs.getWorkload());
			//System.out.println(bs.getLocation() + " " + totalTaskNum);
		}
		
		System.out.println("Total Cost: " + totalCost);
	}
	
	private void updateCandidateAssignment(ArrayList<BaseStation> candidates) 
	{ 
		for(BaseStation bs : candidates) 
		{
			ArrayList<BaseStation> removeList = new ArrayList<>();
			for(BSDistancePair cbs : bs.getAssignedBS()) 
			{
				BaseStation assignedBS = cbs.getBS();
				if(this.bs_en_table.containsKey(assignedBS.getLocation())) 
				{
					bs.addOverlappedBS(assignedBS);
					removeList.add(assignedBS);
				}
			}
			
			// remove
			
			for(BaseStation b : removeList) 
			{
				bs.removeBS(b);
			}
		}
		
	}
	
	
	private void updateCandidateBSAsEN(BaseStation candidate) 
	{
		
		String canId = candidate.getLocation();
		if(this.bs_en_table.containsKey(canId))
		{
			// remove from previous EN assigned BS list
			BaseStation prev_en = this.bs_en_table.get(canId).getEn();
			prev_en.removeBS(candidate);
			
			//update table
			this.bs_en_table.remove(canId);
			for(BaseStation bs : candidate.getOverlapped()) 
			{
				BaseStation old_en = this.bs_en_table.get(bs.getLocation()).getEn();
				
				// compare bs's distance to old en and candidate
				double distance_to_old_en = Utils.getDistance(bs.getLocation(), old_en.getLocation());
				double distance_to_candidate = Utils.getDistance(bs.getLocation(), canId);
				
				// assign bs to en or candidate which has the shortest distance
				if(distance_to_old_en < distance_to_candidate)
				{
					// connect bs with candidate en
					candidate.addBS(bs, distance_to_candidate);
					// update table
					this.bs_en_table.get(bs.getLocation()).setEn(candidate);
					// disconnect bs from old en
					old_en.removeBS(bs);
				}
			}
		}
		
	}
	
	private void updateTable(BaseStation en) 
	{
		for(BSDistancePair pair: en.getAssignedBS()) 
		{
			String key = pair.getBS().getLocation();
			this.bs_en_table.put(key, new Connection(pair.getBS(), en));
		}
	}

}
