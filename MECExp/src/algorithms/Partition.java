package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import objs.BSPair;
import objs.BaseStation;
import objs.Cluster;
import utilities.Constants;
import utilities.Utils;

public class Partition {
	
	private ArrayList<Cluster> resultList = new ArrayList<Cluster>(); 
	public ArrayList<Cluster> getResult(ArrayList<BaseStation> bsList) 
	{
		ArrayList<Cluster> clusterList = new ArrayList<>();
		HashMap<String, Integer> bs_cluster_map = new HashMap<>();
		
		ArrayList<BSPair> pairList = getDistantList(bsList);
		Collections.sort(pairList, BSPair.getComparator());
		
		for(BSPair p : pairList) 
		{
			String key = p.getBs1().getLocation();
			if(bs_cluster_map.containsKey(key)) 
			{
				int index = bs_cluster_map.get(key);
				clusterList.get(index).getStations().add(p.getBs2());
				
				if(clusterList.get(index).getStations().size() >= 500) 
				{
					// remove
					resultList.add(clusterList.get(index));
					clusterList.remove(clusterList.get(index));
					
				}
				
			}else 
			{
				
			}
		}
		
		
		return clusterList;
	}
	
	private ArrayList<BSPair> getDistantList(ArrayList<BaseStation> bsList) 
	{
		ArrayList<BSPair> pairList = new ArrayList();
		for (int i = 0; i < bsList.size(); i++) 
		{
			for(int j = 0; j < bsList.size(); j ++) {
				if(i > j) 
				{
					BaseStation bs1 = bsList.get(i);
					BaseStation bs2 = bsList.get(j);
					BSPair pair = new BSPair(bs1, bs2, Utils.getDistance(bs1.getLocation(), bs2.getLocation()));
					
					pairList.add(pair);
				}
			}
		}
		return pairList;
	}
	
	private void printResult(ArrayList<Cluster> clusterList) 
	{
		System.out.println("Hierarchical clustering result: " + clusterList.size() +" items");
		
		int totalCost = 0;
		
		for(Cluster c : clusterList) 
		{
			int totalTaskNum = c.getCenter().getCTMax();
			ArrayList<BaseStation> stations = c.getStations();
			System.out.println("this cluster has " + c.getStations().size() + " BSs");
			
			for(BaseStation s : stations) 
			{
				totalTaskNum += s.getCTMax();
			}
			
			totalCost += Constants.COST_EN;
			int serverNum = (int)Math.ceil((totalTaskNum * Constants.SINGLE_TASK_SIZE) / Constants.SINGLE_SERVER_CAPACITY);
			totalCost += serverNum * Constants.SINGLE_SERVER_CAPACITY;
			System.out.println(c.getCenter().getLocation() + " " + totalTaskNum);
		}
		
		System.out.println("Total Cost: " + totalCost);
	}
	
	private boolean aggregate(ArrayList<Cluster> clusterList, HashSet<String> skipSet) 
	{
		double shortest = 99999999;
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
					
					double distance = Utils.getDistance(c1, c2);
					if(distance < shortest) {
						shortest = distance;
						cluster1 = clusterList.get(i);
						cluster2 = clusterList.get(j);
					}
				}
				
			}

		}
		
		cluster1.merge(cluster2);
		clusterList.remove(cluster2);
		if(cluster1.getStations().size() > 500) 
		{
			clusterList.remove(cluster1);
			resultList.add(cluster1);
		}
		
		if(clusterList.size() == 1) 
		{
			resultList.add(clusterList.get(0));
			return false;
		}
		
//		if(cluster1.merge(cluster2)) 
//		{
//			clusterList.remove(cluster2);
//			
//		}else {
//			skipSet.add(cluster1.getCenter().getLocation() + " " + cluster2.getCenter().getLocation());
//			skipSet.add(cluster2.getCenter().getLocation() + " " + cluster1.getCenter().getLocation());
//		}
		
		return true;
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
