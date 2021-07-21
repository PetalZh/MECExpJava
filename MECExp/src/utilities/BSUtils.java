package utilities;

import java.util.ArrayList;
import java.util.Hashtable;

import objs.BSDistancePair;
import objs.BaseStation;
import objs.UserRequest;

public class BSUtils {
	
	public static ArrayList<BaseStation> getBSList(Hashtable<String, ArrayList<UserRequest>> BStable){
		ArrayList<BaseStation> bsList = new ArrayList<>();
	
		for(String key: BStable.keySet()) {
			BaseStation bs = new BaseStation(key);

			ArrayList<UserRequest> requestList= BStable.get(key);
			bs.setCTMax(Utils.getCTMax(requestList));
			bsList.add(bs);
			
			//System.out.println(key + ": " + bs.getCTMax() + " " + bs.getWorkload()); //
		}
		
		System.out.println("BS #"+ ": " + bsList.size());
		
		return bsList;
	}
	
	public static void getBSConnection(ArrayList<BaseStation> bsList, ArrayList<BaseStation> candidates) 
	{
		ArrayList<BaseStation> availableBSList = (ArrayList<BaseStation>) bsList.clone();
		availableBSList.addAll(candidates);
		
		for(BaseStation bs1 : bsList) {
			String[] latlng1 = bs1.getLocation().split("/");
			double lat1 = Double.parseDouble(latlng1[0]); 
			double lng1 = Double.parseDouble(latlng1[1]); 
			bs1.clearBS();
			bs1.initWorkload();
			
			
			for(BaseStation bs2 : availableBSList) 
			{
				String[] latlng2 = bs2.getLocation().split("/");
				double lat2 = Double.parseDouble(latlng2[0]); 
				double lng2 = Double.parseDouble(latlng2[1]); 
				
				double distance = Utils.getDistance(lng1, lat1, lng2, lat2);
				if( distance != 0 && distance <= Constants.DISTANCE_THRESH) {
					bs1.addBS(bs2, distance);
				}
			}
		}
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
				if( distance != 0 && distance <= Constants.DISTANCE_THRESH) {
					bs1.addBS(bs2, distance);
				}
			}
		}
	}
	


}
