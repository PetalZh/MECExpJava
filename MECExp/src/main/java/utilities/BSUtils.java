package utilities;

import java.util.ArrayList;
import java.util.Hashtable;

import objs.BaseStation;
import objs.UserRequest;

public class BSUtils {
	
	public static ArrayList<BaseStation> getBSList(Hashtable<String, ArrayList<UserRequest>> BStable){
		ArrayList<BaseStation> bsList = new ArrayList<>();
		int max_ct_max = 0;
		for(String key: BStable.keySet()) {
			BaseStation bs = new BaseStation(key);
			bs.setRequestList(BStable.get(key));
			
			ArrayList<UserRequest> requestList = BStable.get(key);
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
		System.out.println("CT max: " + max_ct_max);
		
		
		return bsList;
	}
	

	public static void getBSConnection(ArrayList<BaseStation> bsList) {
//		Loops are independent, changed to parallel
		bsList.parallelStream().forEach(bs1 -> {
			double[] latLng1 = parseLatLng(bs1.getLocation());
			bs1.clearBS();
			bs1.initWorkload();

//			 Exclude duplicate comparisons
			bsList.stream().filter(bs2 -> !bs1.equals(bs2)).forEach(bs2 -> {
				double[] latLng2 = parseLatLng(bs2.getLocation());
				double distance = Utils.getDistance(latLng1[1], latLng1[0], latLng2[1], latLng2[0]);
				double trans_delay = Utils.getTransDelay(distance, bs2.getCTMax() * Constants.SINGLE_TASK_SIZE);

				if (trans_delay < Constants.DELAY_THRESH * 0.998) {
					bs1.addBS(bs2, distance, Constants.isPeak);
				}
			});
		});
	}

	private static double[] parseLatLng(String location) {
		String[] latLngStr = location.split("/");
//		Resource consumption is too high
		return new double[]{Double.parseDouble(latLngStr[0]), Double.parseDouble(latLngStr[1])};
	}
}
