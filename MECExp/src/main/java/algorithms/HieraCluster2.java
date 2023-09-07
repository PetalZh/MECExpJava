package algorithms;

import java.util.ArrayList;
import java.util.HashMap;

import objs.BaseStation;
import smile.clustering.*;
import smile.clustering.linkage.*;
import smile.math.distance.Distance;
import utilities.DistanceImplement;
import utilities.FileIO;
import utilities.Utils;

public class HieraCluster2{
	
	private ArrayList<ArrayList<BaseStation>> clusterList;
	private int cluster_size;
	
	public HieraCluster2(int cluster_size) {
		super();
		clusterList = new ArrayList<>();
		this.cluster_size = cluster_size;
	}

	public ArrayList<ArrayList<BaseStation>> getResult(ArrayList<BaseStation> bsList) 
	{
		
		while(bsList.size() > cluster_size)
		{
			getClusters(bsList);
		}
		
		clusterList.add(bsList);

		System.out.print(bsList.size() + " ");
		
		System.out.println("# of clusters: " + clusterList.size());
		
		FileIO.output_cluster(clusterList);
		
		return clusterList;
	}
	
	private void getClusters(ArrayList<BaseStation> bsList) 
	{	
	    DistanceImplement distance = new DistanceImplement();

		Linkage linkage = UPGMALinkage.of(bsList.toArray(new BaseStation[0]), distance);
	    
	    HierarchicalClustering hc = HierarchicalClustering.fit(linkage);
	    
	    double[] heights = hc.getHeight();
	    for(double h : heights) 
	    {
	    	int[] partitions = hc.partition(h);
	    	if(formCluster(partitions, bsList))
	    		break;
	    }
	    
	}
	
	private boolean formCluster(int[] partitions, ArrayList<BaseStation> bsList) 
	{
		boolean check = false;
		HashMap<Integer, ArrayList<BaseStation>> clusters = new HashMap<>();
	    for(int i = 0; i < partitions.length; i++) 
	    {
	    	int key = partitions[i];
	    	if(clusters.containsKey(key)) 
	    	{
	    		clusters.get(key).add(bsList.get(i));
	    	}else {
	    		ArrayList<BaseStation> items = new ArrayList<>();
	    		items.add(bsList.get(i));
	    		clusters.put(key, items);
	    	}
	    }

	    for (int key : clusters.keySet()) {
	    	if(clusters.get(key).size() >= cluster_size) 
	    	{
	    		System.out.print(clusters.get(key).size() + " ");
	    		clusterList.add(clusters.get(key));
	    		
	    		remove(clusters.get(key), bsList);
	    		check = true;
	    	}
	    }
	    clusters.clear();
	    
	    return check;
	}
	
	private void remove(ArrayList<BaseStation> removeList, ArrayList<BaseStation> bsList) 
	{
		for(BaseStation bs : removeList) 
		{
			bsList.remove(bs);
		}
	}
	
	private void printCluster(int[] partitions, ArrayList<BaseStation> bsList) 
	{
		HashMap<Integer, ArrayList<BaseStation>> clusters = new HashMap<>();
	    for(int i = 0; i < partitions.length; i++) 
	    {
	    	int key = partitions[i];
	    	if(clusters.containsKey(key)) 
	    	{
	    		clusters.get(key).add(bsList.get(i));
	    	}else {
	    		ArrayList<BaseStation> items = new ArrayList<>();
	    		items.add(bsList.get(i));
	    		clusters.put(key, items);
	    	}
//	    	System.out.print(partitions[i] + " ");
	    }
	    System.out.println("# of clusters: " + clusters.size());
	    
	    System.out.println();
	    for (int key : clusters.keySet()) {
	        System.out.println(key + " " + clusters.get(key).size());
	    }
	}


//    KMeans km = KMeans.fit(data, 6);
//    partitions = km.y;

//    DBSCAN db = DBSCAN.fit(bsList.toArray(), distance, 10, 900);
//    partitions = db.y;



}
