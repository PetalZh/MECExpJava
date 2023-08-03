package optimizers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import objs.BaseStation;
import objs.TimePoint;
import objs.UserRequest;
import utilities.Utils;
import java.util.concurrent.*;

public class DynamicGreedy {

	public DynamicGreedy() {
		super();
	}
	
//	Date start1 = new Date();
//	Date end1 = new Date();
//	
//	String time = String.valueOf((double)(end1.getTime() - start1.getTime())/(double)1000);
//	System.out.println("prepare time: " + time + " s");
	
	
//	public void dynamicAssign(ArrayList<BaseStation> bsList, ArrayList<BaseStation> enList, ArrayList<UserRequest> userRequests) 
//	{
//		System.out.println("number of user requests: " + userRequests.size());
////		HashMap<String, BaseStation> bsIndex = createBSIndex(bsList);
////		
////		assignURs(enList, userRequests, bsIndex);
//		
//		assignURByBS(enList, bsList);
//		
//		for(BaseStation en : enList) 
//		{
//			int en_ct_max = Utils.getCTMax(en.getAssignedURs());
//			 
//		}
//		
//		for(UserRequest ur : userRequests) 
//		{
//			
//			ArrayList<UserRequest> bs_overlap_list = getOverlapList(ur, ur.getFrom().getRequestList());
//			ArrayList<UserRequest> en_overlap_list = getOverlapList(ur, ur.getTo().getAssignedURs());
//			
//			int ct_max_trans = Utils.getCTMax(bs_overlap_list);
//			int ct_max_comp = Utils.getCTMax(en_overlap_list);
//			
//			double distance = Utils.getDistance(ur.getLocation(), ur.getTo().getLocation());
////			double trans_delay = Utils.getTransDelay(distance, ct_max_trans);
//			double capacity = Utils.getCapacityRequired(distance, ct_max_comp);
//			ur.getTo().updateCapacityRequired(capacity);
//		}
//		
//		
//		Utils.printResult(enList, "Greedy with candidate list in dynamic result: ");
//		
//	}
	
	public ArrayList<BaseStation> dynamicAssign(ArrayList<BaseStation> bsList, ArrayList<BaseStation> enList, ArrayList<UserRequest> userRequests, boolean withCandidate) 
	{
		assignURByBS(enList, bsList);
		
		for(BaseStation en : enList) 
		{
			int en_ct_max = Utils.getCTMax(en.getAssignedURs());
			
			double max_trans_delay = 0;
			for(BaseStation bs : en.getFromList())
			{
				double distance = Utils.getDistance(en.getLocation(), bs.getLocation());
				int ct_max = Utils.getCTMax(bs.getAssignedURs());
				double delay = Utils.getTransDelay(distance, ct_max);
				if(delay > max_trans_delay) 
				{
					max_trans_delay = delay;
				}
			}
			double capacityRequired = Utils.getCapacityRequiredByTransDelay(max_trans_delay, en_ct_max);
			en.updateCapacityRequired(capacityRequired); 
			 
		}
		return enList;
	}

	public ArrayList<BaseStation> dynamicAssignMultithreading(ArrayList<BaseStation> bsList, ArrayList<BaseStation> enList, ArrayList<UserRequest> userRequests, boolean withCandidate) {
		assignURByBS(enList, bsList);

		// Create thread pool
		int numOfThreads = Runtime.getRuntime().availableProcessors();
		ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

		// Create and submit a task for each en
		for (BaseStation en : enList) {
			executorService.submit(() -> {
				int en_ct_max = Utils.getCTMax(en.getAssignedURs());

				double max_trans_delay = 0;
				for(BaseStation bs : en.getFromList()) {
					double distance = Utils.getDistance(en.getLocation(), bs.getLocation());
					int ct_max = Utils.getCTMax(bs.getAssignedURs());
					double delay = Utils.getTransDelay(distance, ct_max);
					if(delay > max_trans_delay) {
						max_trans_delay = delay;
					}
				}
				double capacityRequired = Utils.getCapacityRequiredByTransDelay(max_trans_delay, en_ct_max);
				en.updateCapacityRequired(capacityRequired);
			});
		}

		// wait for all tasks to complete
		executorService.shutdown();
		try {
			if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
			}
		} catch (InterruptedException ex) {
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}
		return enList;
	}


//	private ArrayList<TimePoint> getTimeEntries(ArrayList<UserRequest> requestList)
//	{
//		ArrayList<TimePoint> pointList = new ArrayList<>();
//		for(int i = 0; i < requestList.size(); i++) {
//			try {
//				pointList.add(new TimePoint(requestList.get(i).getStartTime(), 0));
//				pointList.add(new TimePoint(requestList.get(i).getEndTime(), 1));
//			}catch(Exception e) {
//				System.out.println(e.getMessage());
//			}
//			
//		}
//		
//		Collections.sort(pointList);
//		return pointList;
//	}
	
//	private ArrayList<UserRequest> getOverlapList(UserRequest ur, ArrayList<UserRequest> ur_list) 
//	{
//		ArrayList<UserRequest> overlap_list = new ArrayList<>();
//		
//		for(UserRequest ur2: ur_list) 
//		{
//			if(ur.getStartTime().getTime() <= ur2.getEndTime().getTime() || ur.getEndTime().getTime() >= ur2.getStartTime().getTime()) 
//			{
//				overlap_list.add(ur2);
//			}
//		}
//		return overlap_list;
//	}
	
	
	private void assignURByBS(ArrayList<BaseStation> enList, ArrayList<BaseStation> bsList) 
	{
		for(BaseStation bs : bsList) 
		{
			double distance_min = 999999999;
			BaseStation assignedEN = null;
			
			// get the nearest en
			for(BaseStation en : enList) 
			{
				if(bs.getLocation().equals(en.getLocation())) 
				{
					assignedEN = en;
					break;
				}
				
				double distance = Utils.getDistance(bs.getLocation(), en.getLocation());
				if(distance < distance_min) 
				{
					distance_min = distance;
					assignedEN = en;
					
				}
			}
			
			bs.setAssignTo(assignedEN);
			assignedEN.AddFromBS(bs);
			
			for (UserRequest ur : bs.getRequestList()) 
			{
				assignedEN.addUR(ur);
				ur.setTo(assignedEN);
				ur.setFrom(bs);
			}
			
		}
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
