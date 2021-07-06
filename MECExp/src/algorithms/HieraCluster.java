package algorithms;

import java.util.ArrayList;
import java.util.HashSet;

import objs.BaseStation;
import objs.Cluster;
import utilities.Utils;

public class HieraCluster {
	
	public ArrayList<Cluster> callHieraCluster(ArrayList<BaseStation> bsList) 
	{
		ArrayList<Cluster> clusterList = init(bsList);
		HashSet<String> skipSet = new HashSet<>();
		while(true) 
		{
			if(!aggregate(clusterList, skipSet)) 
			{
				break;
			}
		}
		
		return clusterList;
	}
	
	private boolean aggregate(ArrayList<Cluster> clusterList, HashSet<String> skipSet) 
	{
		double shortest = 9999999;
		Cluster cluster1 = null;
		Cluster cluster2 = null;
		
		if(clusterList.size() == 1) 
		{
			return false;
		}
		
		for(int i = 0; i < clusterList.size(); i++) 
		{
			for(int j = 0; j < clusterList.size(); j++) 
			{
				if(i != j) {
					String c1 = clusterList.get(i).getCenter().getLocation();
					String c2 = clusterList.get(j).getCenter().getLocation();
					
					if(skipSet.contains(c1 + " " + c2) || skipSet.contains(c2 + " " + c1)) 
					{
						continue;
					}
					
					double distance = Utils.getDistance(c1, c2);
					if(distance < shortest) {
						shortest = distance;
						cluster1 = clusterList.get(i);
						cluster2 = clusterList.get(j);
					}
				}
				
			}
			
			if(i == clusterList.size() - 1 && cluster1 == null && cluster2 == null) 
			{
				return false;
			}
		}
		
		if(cluster1.merge(cluster2)) 
		{
			clusterList.remove(cluster2);
			// System.out.println("aaaaaaa");
			
		}else {
			skipSet.add(cluster1.getCenter().getLocation() + " " + cluster2.getCenter().getLocation());
			skipSet.add(cluster2.getCenter().getLocation() + " " + cluster1.getCenter().getLocation());
		}
		
		return true;
//		if(cluster1 != null && cluster2 != null) 
//		{
//			
//		}
	}
	
	private ArrayList<Cluster> init(ArrayList<BaseStation> bsList)
	{
		ArrayList<Cluster> clusterList = new ArrayList<>();
		for(BaseStation bs : bsList) 
		{
			Cluster cluster = new Cluster(bs);
			clusterList.add(cluster);
		}
		
		
		return clusterList;
	}

	
}
