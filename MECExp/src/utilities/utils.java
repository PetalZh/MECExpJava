package utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import objs.TimePoint;
import objs.UserRequest;

public class utils {
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
	
	
	

}
