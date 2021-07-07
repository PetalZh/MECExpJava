package objs;

import java.util.ArrayList;

import utilities.Constants;
import utilities.Utils;

public class Cluster {
	private BaseStation center;
	private ArrayList<BaseStation> stations;
	
	public Cluster(BaseStation center) {
		super();
		this.center = center;
		this.stations = new ArrayList<>();
		this.stations.add(center);
	}
	
	
	public BaseStation getCenter() {
		return center;
	}
	public void setCenter(BaseStation center) {
		this.center = center;
	}
	public ArrayList<BaseStation> getStations() {
		return stations;
	}
//	public void setStations(ArrayList<BaseStation> stations) {
//		this.stations = stations;
//	}
	
	public boolean merge(Cluster c) 
	{
		ArrayList<BaseStation> bsList = new ArrayList<>();
		bsList.addAll(this.stations);
		bsList.addAll(c.getStations());
		
		BaseStation center = getCenter(bsList);
		if(center != null) {
			this.stations = bsList;
			this.center = center;
			return true;
		}else {
			return false;
		}
	}
	
	private BaseStation getCenter(ArrayList<BaseStation> bsList) 
	{
		double shortestAvg = 9999999;
		double longestToCenter = 0;
		BaseStation center = null;
		
		for(int i = 0; i < bsList.size(); i++) 
		{
			double longest = 0;
			double totalDist = 0;
			for(int j = 0; j < bsList.size(); j++) {
				if(i != j) {
					double distance = Utils.getDistance(bsList.get(i).getLocation(), bsList.get(j).getLocation());
					totalDist += distance;
					
					if(distance > longest) 
					{
						longest = distance;
					}
				}
			}
			
			double averageDist = totalDist/(bsList.size() - 1);
			
			if(averageDist < shortestAvg) {
				shortestAvg = averageDist;
				center = bsList.get(i);
				longestToCenter = longest;
			}
		}
		
		// if center can cover all stations
		// TODO update distance theta later
		if(longestToCenter > Constants.DISTANCE_THRESH) 
		{
			return null;
		}
		
		return center;
	}
	  
}
