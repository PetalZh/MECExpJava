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
	
	public static float getCapacityRequired() 
	{
		return 0;
	}

}
