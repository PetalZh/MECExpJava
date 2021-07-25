package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;

import objs.BSDistancePair;
import objs.BaseStation;
import objs.Connection;
import utilities.BSUtils;
import utilities.Constants;
import utilities.Utils;

public class GreedyNew {
	private Hashtable<String, Connection> bs_en_table; 
	private int threshold;
	//private ArrayList<BaseStation> result;

	
	public GreedyNew() 
	{
		super();
		bs_en_table = new Hashtable<>();
	}
	
	private void test_print_list(ArrayList<BaseStation> list) 
	{
		for(BaseStation bs : list) 
		{
			System.out.println("id: " + bs.getLocation() + " " + bs.getWorkload() + " " + bs.getCTMax());
			for(BSDistancePair p: bs.getAssignedBS()) 
			{
				System.out.println(" " + p.getBS().getLocation() + " " + p.getBS().getWorkload() + " " + p.getBS().getCTMax());
//				System.out.println("    overlap: ");
//				for(BaseStation i : p.getBS().getOverlapped()) {
//					System.out.println("    " + i.getLocation() + " ");
//				}
			}
			
		}
	}
	
	public ArrayList<BaseStation> getResult(ArrayList<BaseStation> bsList, int thres) 
	{
			this.threshold = thres;
			ArrayList<BaseStation> candidates = new ArrayList<>();
			ArrayList<BaseStation> enList = new ArrayList<>();
			
			while(bsList.size() != 0) 
			{
				System.out.println("----------------------------");
//				System.out.println("BS list: ");
//				test_print_list(bsList);
//				
				System.out.println("");
				System.out.println("Can list: ");
				test_print_list(candidates);
//				
//				System.out.println("");
//				System.out.println("EN list: ");
//				test_print_list(enList);
				
				BaseStation en = null;
				
				Collections.sort(bsList);
				Collections.sort(candidates);
				//System.out.println("test "+candidates.size());
				
				if(candidates.size() != 0 && candidates.get(0).getWorkload() > bsList.get(0).getWorkload()) 
				{
					en = candidates.get(0);
					en.includeSelfWorkload();
					candidates.remove(en);
				}else {
					en = bsList.get(0);
					bsList.remove(en);
				}
				
				enList.add(en);
				removeEnFromOverlap(en, candidates);
				
				addEN(en, bsList, candidates, enList);
				
				
				//this.result = enList;
			}
			
			
			printResult(enList);
			return enList;
	}
	
	private void removeEnFromOverlap(BaseStation en, ArrayList<BaseStation> candidates) 
	{
		for(BaseStation bs : candidates) 
		{
			bs.removeOverlappedBS(en);
		}
	}
	
	private void printResult(ArrayList<BaseStation> result) 
	{
		System.out.println("-----------------------------");
		System.out.println("Greedy with candidate list result: " + result.size() + " items");
		int totalCost = 0; 
		for(BaseStation bs : result) 
		{
			totalCost += Constants.COST_EN;
			int serverNum = (int)Math.ceil((bs.getWorkload()) / Constants.SINGLE_SERVER_CAPACITY);
			
			totalCost += serverNum * Constants.SINGLE_SERVER_CAPACITY;
			
			//System.out.println(bs.getLocation() + " " + bs.getWorkload());
		}
		
		System.out.println("Total Cost: " + totalCost);
	}
	
	private void addEN(BaseStation en, ArrayList<BaseStation> bsList, ArrayList<BaseStation> candidates, ArrayList<BaseStation> enList) 
	{
		String key = en.getLocation();
		if(this.bs_en_table.containsKey(key)) 
		{
			// this is a bs from candidate list
			
			// Step 1: disconnect en from previous en
			BaseStation prev_en = this.bs_en_table.get(key).getEn();
			
			prev_en.removeBS(en);
			this.bs_en_table.remove(key);
			
			// Step 2: address overlapped base station
			ArrayList<BaseStation> overlapped = en.getOverlapped();
			
			for(BaseStation bs : overlapped) 
			{	
				BaseStation prev = this.bs_en_table.get(bs.getLocation()).getEn();
				updateSignleBSAssignment(en, prev , bs);
			}
			
		}
		
		// Address new added BSs come with en
		
		// Step 1: Register new bs-en connections to table
		// and remove assigned bs from bsList
		
		ArrayList<BSDistancePair> assignedBS = en.getAssignedBS();
		for(BSDistancePair bs : assignedBS) 
		{
			
			registerNewAssignment(bs.getBS(), en);
			
			bsList.remove(bs.getBS());
		}
		
		// Step 2: add bs to candidate list (number < threshold)
		Collections.sort(assignedBS);
		
		Iterator<BSDistancePair> iter = assignedBS.iterator();
		int counter = 0;
		while(iter.hasNext()) 
		{
			BaseStation item = iter.next().getBS();
			counter++;
			if(!candidates.contains(item)) {
				item.excludeSelfWorkload();
				candidates.add(item);
			}
			
			if(counter == this.threshold) 
			{
				break;
			}
		}
		
		// Step 3: Update assignment in candidate list
		for(BaseStation can : candidates) 
		{
			ArrayList<BSDistancePair> assignedList = can.getAssignedBS();
			ArrayList<BaseStation> removeList = new ArrayList<>();
			for(BSDistancePair assigned : assignedList) 
			{
				if(this.bs_en_table.containsKey(assigned.getBS().getLocation())) 
				{
					can.addOverlappedBS(assigned.getBS());
					removeList.add(assigned.getBS());
				}
				
				if(enList.contains(assigned.getBS())) 
				{
					removeList.add(assigned.getBS());
				}
			}
			
			for(BaseStation r : removeList) 
			{
				can.removeBS(r);
			}
		}
		
		// Step 4: Recompute connections for BS List
		
		BSUtils.getBSConnection(bsList);
		
		
	}
	
	private void registerNewAssignment(BaseStation bs, BaseStation en) 
	{
		String key = bs.getLocation();
		this.bs_en_table.put(key, new Connection(bs, en));
	}
	
	
	private void updateSignleBSAssignment(BaseStation new_en, BaseStation prev_en, BaseStation bs) 
	{
		
		// compare bs's distance to old en and candidate
		double distance_to_prev_en = Utils.getDistance(bs.getLocation(), prev_en.getLocation());
		double distance_to_new_en = Utils.getDistance(bs.getLocation(), new_en.getLocation());
		
		// assign bs to en which has the shortest distance
		if(distance_to_prev_en > distance_to_new_en)
		{
			// connect bs with new en
			new_en.addBS(bs, distance_to_new_en);
			// update table
			this.bs_en_table.get(bs.getLocation()).setEn(new_en);
			// disconnect bs from old en
			prev_en.removeBS(bs);
		}
	}

}
