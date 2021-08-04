package utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;

import objs.BaseStation;
import objs.TimePoint;
import objs.UserRequest;

public class Utils {
	public static Date timeFormater(String time_string) 
	{
		try {
			String timeFormat = "dd-MM-yyyy H:mm";
			String[] time = time_string.split(" ");
			
			String[] date_item = time[0].split("/");
			
			if(date_item[0].length() == 1) {
				date_item[0] = 0 + date_item[0];
			}

			if(date_item[1].length() == 1) {
				date_item[1] = 0 + date_item[1];
			}
			
			date_item[2] = 20 + date_item[2];
			
			String date_string = date_item[0] + "-" + date_item[1]+ "-" +date_item[2];
			Date date = new SimpleDateFormat(timeFormat).parse(date_string + " " + time[1]);
			//System.out.println(date_string);
			return date;
		} catch (ParseException e) {
			//e.printStackTrace();
			return null;
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
		
	}
	
	public static int getCTMax(ArrayList<UserRequest> requestList) 
	{
		ArrayList<TimePoint> pointList = new ArrayList<>();
		for(int i = 0; i < requestList.size(); i++) {
			pointList.add(new TimePoint(requestList.get(i).getStartTime(), 0));
			pointList.add(new TimePoint(requestList.get(i).getEndTime(), 1));
		}
		
		Collections.sort(pointList);
		int count = 0;
		int max = 0;
		for(int i = 0; i < pointList.size(); i++) {
			if(pointList.get(i).getType() == 0) 
			{
				count++;
			}else {
				count--;
			}
			
			if(count > max) {
				max = count;
			}
		}
		return max;
	}
	
	// calculate distance between two points with lat and lng
	// cite from https://www.jianshu.com/p/18efaabab98e
	private static final  double EARTH_RADIUS = 6378137;
	private static double rad(double d){
	    return d * Math.PI / 180.0;
	}
	
	public static double getDistance(double lon1,double lat1,double lon2, double lat2) {
	    double radLat1 = rad(lat1);
	    double radLat2 = rad(lat2);
	    double a = radLat1 - radLat2;
	    double b = rad(lon1) - rad(lon2);
	    double s = 2 *Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2))); 
	    s = s * EARTH_RADIUS;    
	   return s; // in meter
	}
	
	public static double getDistance(String loc1, String loc2) {
		String[] latlng1 = loc1.split("/");
		String[] latlng2 = loc2.split("/");
		
		double lat1 = Double.parseDouble(latlng1[0]);
		double lon1 = Double.parseDouble(latlng1[1]);
		
		double lat2 = Double.parseDouble(latlng2[0]);
		double lon2 = Double.parseDouble(latlng2[1]);
		
	    double radLat1 = rad(lat1);
	    double radLat2 = rad(lat2);
	    double a = radLat1 - radLat2;
	    double b = rad(lon1) - rad(lon2);
	    double s = 2 *Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2))); 
	    s = s * EARTH_RADIUS;    
	   return s; // in meter
	}
	
	public static double getTransDelay(double distance, double taskSize) 
	{
		if(distance == 0) 
		{
			return 0;
		}
		
		double logNum = 1 + (Constants.CANNEL_SIGNAL_POWER/(Constants.ALPHA * (distance/1000)));
		double logValue = customLog(2,logNum);
		//System.out.println("Trans Delay:" + taskSize/logValue);
		
		return taskSize/logValue;
	}
	
	private static double customLog(double base, double logNum) 
	{
		return Math.log(logNum)/Math.log(base);
	}
	
	public static double getCapacityRequired(double distance, double taskSize) 
	{
		double result = taskSize/(Constants.DELAY_THRESH - getTransDelay(distance, taskSize));
		//System.out.println("capacity requirement:" + result);
		return handlePrecision(result);
	}
	
	public static double handlePrecision(double input) 
	{
		return (double)Math.round(input * 1000)/(double)1000;
	}
	
	public static void printResult(ArrayList<BaseStation> result, String method_name) 
	{
		System.out.println("-----------------------------");
		System.out.println(method_name + " " + result.size() + " items");
		int totalCost = 0; 
		for(BaseStation bs : result) 
		{
			totalCost += Constants.COST_EN;
			int serverNum = (int)Math.ceil((bs.getWorkload()) / Constants.SINGLE_SERVER_CAPACITY);
			
			totalCost += serverNum * Constants.COST_SERVER;
			
			//System.out.println(bs.getLocation() + " " + bs.getWorkload());
		}
		
		System.out.println("Total Cost: " + totalCost);
	}
	
	public static int getDistanceThreshold(int workload) 
	{
		double exp_pow = workload * 0.7 / (Constants.DELAY_THRESH * Constants.BANDWIDTH);
		double exp_bottom = Constants.ALPHA * (Math.pow(2, exp_pow) - 1);
		double distance = Constants.CANNEL_SIGNAL_POWER / exp_bottom;
		
		return (int)Math.round(distance);
	}

}
