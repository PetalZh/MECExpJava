package optimizers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import objs.BaseStation;
import objs.UserRequest;
import utilities.Utils;

public class DynamicGreedy {

	public DynamicGreedy() {
		super();
	}
	
	public void dynamicAssign(ArrayList<BaseStation> bsList, ArrayList<BaseStation> enList, ArrayList<UserRequest> userRequests) 
	{
		System.out.println("number of user requests: " + userRequests.size());
		HashMap<String, BaseStation> bsIndex = createBSIndex(bsList);
		
		Date start1 = new Date();
		assignURs(enList, userRequests, bsIndex);
		Date end1 = new Date();
		
		String time = String.valueOf((double)(end1.getTime() - start1.getTime())/(double)1000);
		System.out.println("prepare time: " + time + " s");
		
		Date start2 = new Date();
		for(UserRequest ur : userRequests) 
		{
			
			ArrayList<UserRequest> bs_overlap_list = getOverlapList(ur, ur.getFrom().getRequestList());
			ArrayList<UserRequest> en_overlap_list = getOverlapList(ur, ur.getTo().getAssignedURs());
			
			int ct_max_trans = Utils.getCTMax(bs_overlap_list);
			int ct_max_comp = Utils.getCTMax(en_overlap_list);
			
			double distance = Utils.getDistance(ur.getLocation(), ur.getTo().getLocation());
//			double trans_delay = Utils.getTransDelay(distance, ct_max_trans);
			double capacity = Utils.getCapacityRequired(distance, ct_max_comp);
			ur.getTo().updateCapacityRequired(capacity);
		}
		Date end2 = new Date();
		String time2 = String.valueOf((double)(end2.getTime() - start2.getTime())/(double)1000);
		System.out.println("cal time: " + time2 + " s");
		
		
		Utils.printResult(enList, "Greedy with candidate list in dynamic result: ");
		
	}
	
	private ArrayList<UserRequest> getOverlapList(UserRequest ur, ArrayList<UserRequest> ur_list) 
	{
		ArrayList<UserRequest> overlap_list = new ArrayList<>();
		
		for(UserRequest ur2: ur_list) 
		{
			if(ur.getStartTime().getTime() <= ur2.getEndTime().getTime() || ur.getEndTime().getTime() >= ur2.getStartTime().getTime()) 
			{
				overlap_list.add(ur2);
			}
		}
		return overlap_list;
	}

	private void assignURs(ArrayList<BaseStation> enList, ArrayList<UserRequest> userRequests, HashMap<String, BaseStation> bsIndex) 
	{
		for(UserRequest ur : userRequests) 
		{
			double distance_min = 999999999;
			BaseStation assignedEN = null;
			for(BaseStation en : enList) 
			{
				if(ur.getLocation().equals(en.getLocation())) 
				{
					assignedEN = en;
					break;
				}
				
				double distance = Utils.getDistance(ur.getLocation(), en.getLocation());
				if(distance < distance_min) 
				{
					distance_min = distance;
					assignedEN = en;
					
				}
			}
			
			
			assignedEN.addUR(ur);
			ur.setTo(assignedEN);
			ur.setFrom(bsIndex.get(ur.getLocation()));
		}
	}
	
	private HashMap<String, BaseStation> createBSIndex(ArrayList<BaseStation> bsList) 
	{
		HashMap<String, BaseStation> index = new HashMap<>();

		for(BaseStation bs : bsList) 
		{
			index.put(bs.getLocation(), bs);
		}
		
		return index;
	}
	
	

}
