package utilities;

import java.util.ArrayList;
import java.util.Hashtable;

import objs.BSDistancePair;
import objs.BaseStation;
import objs.UserRequest;

public class BSUtils {
	
	public static ArrayList<BaseStation> getBSList(Hashtable<String, ArrayList<UserRequest>> BStable){
		ArrayList<BaseStation> bsList = new ArrayList<>();
		int max_ct_max = 0;
		for(String key: BStable.keySet()) {
			BaseStation bs = new BaseStation(key);
			bs.setRequestList(BStable.get(key));
			
			ArrayList<UserRequest> requestList= BStable.get(key);
			int ct_max = Utils.getCTMax(requestList);
			
			bs.setCTMax(ct_max);
			bsList.add(bs);
			
			if(ct_max > max_ct_max) 
			{
				max_ct_max = ct_max;
			}
			
			//System.out.println(key + ": " + bs.getCTMax() + " " + bs.getWorkload()); 
		}
		
		System.out.println("BS #"+ ": " + bsList.size());
		Constants.CTMAX = max_ct_max;
		//System.out.println("CT max: " + max_ct_max); 
		
		
		return bsList;
	}
	
	public static void getBSConnection(ArrayList<BaseStation> bsList) 
	{
		for(BaseStation bs1 : bsList) {
			String[] latlng1 = bs1.getLocation().split("/");
			double lat1 = Double.parseDouble(latlng1[0]); 
			double lng1 = Double.parseDouble(latlng1[1]); 
			bs1.clearBS();
			bs1.initWorkload();
			
			for(BaseStation bs2 : bsList) 
			{
				String[] latlng2 = bs2.getLocation().split("/");
				double lat2 = Double.parseDouble(latlng2[0]); 
				double lng2 = Double.parseDouble(latlng2[1]); 
				
				double distance = Utils.getDistance(lng1, lat1, lng2, lat2);
				double trans_delay = Utils.getTransDelay(distance, bs2.getCTMax() * Constants.SINGLE_TASK_SIZE);
				//trans_delay < Constants.DELAY_THRESH; distance <= Constants.DISTANCE_THRESH
				if( distance != 0 && trans_delay < Constants.DELAY_THRESH * 0.998) {
					//System.out.println("Before add: " + bs1.getLocation() + " workload: " + bs1.getWorkload());
					bs1.addBS(bs2, distance, Constants.isPeak);
					//System.out.println("After add: " + bs1.getLocation() +" "+ bs1.getWorkload());
				}
			}
		}
	}
	


}
