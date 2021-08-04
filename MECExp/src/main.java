import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import algorithms.Greedy;
import algorithms.GreedyNew;
import algorithms.HieraCluster;
import algorithms.MIPAlgo;
import objs.BaseStation;
import objs.Cluster;
import objs.UserRequest;
import utilities.BSUtils;
import utilities.Constants;
import utilities.FileIO;
import utilities.Utils;

public class main {
	public static void main(String args[]) {
		Hashtable<String, ArrayList<UserRequest>> BSTable = readDoc();
		ArrayList<BaseStation> bsList = BSUtils.getBSList(BSTable);
		
		int[] range_input = {199, 499, 999, 1499, 1999, 2499, 2999}; 
		int range = 2;
		if(range >= bsList.size()) 
		{
			range = bsList.size() - 1;
		}
		
		System.out.println(range + 1 + " BS used");
		
//		greedy(new ArrayList<BaseStation>(bsList.subList(0, range)));
//		greedyNew((ArrayList<BaseStation>)(new ArrayList<BaseStation>(bsList.subList(0, range))), 10);


		//hieraCluster((ArrayList<BaseStation>) bsList.clone());
		
		
		MIPAlgo mip = new MIPAlgo();
		mip.getMIP(new ArrayList<BaseStation>(bsList.subList(0, range)));
		
		//System.out.println(Utils.getDistanceThreshold(270));
	}
	
	private static void hieraCluster(ArrayList<BaseStation> bsList) 
	{
		Date start = new Date();
		
		HieraCluster hieraCluster = new HieraCluster();
		ArrayList<Cluster> clusterList = hieraCluster.getResult(bsList);
		
		Date end = new Date();
		System.out.println("Running time: " + (double)(end.getTime() - start.getTime())/(double)1000 + " s");
	}
	
	private static void greedy(ArrayList<BaseStation> bsList) 
	{
		// Greedy methods
		Date start = new Date();
		
		BSUtils.getBSConnection(bsList);
		Greedy greedy = new Greedy();
		ArrayList<BaseStation> greedyResult = greedy.getResult(bsList);
		
		Date end = new Date();
		
		System.out.println("Running time: " + (double)(end.getTime() - start.getTime())/(double)1000 + " s");
	}
	
	private static void greedyNew(ArrayList<BaseStation> bsList, int threshold) 
	{
		Date start = new Date();
		
		BSUtils.getBSConnection(bsList);
		GreedyNew greedy = new GreedyNew();
		greedy.getResult(bsList, threshold);
		
		Date end = new Date();
		
		System.out.println("Running time: " + (double)(end.getTime() - start.getTime())/(double)1000 + " s");
	}
	
	
	private static Hashtable<String, ArrayList<UserRequest>> readDoc() 
	{
		BufferedReader reader = null;
		FileInputStream inputStream = null;
		Hashtable<String, ArrayList<UserRequest>> BSTable = new Hashtable<>();
		try {
			inputStream = new FileInputStream("shanghai15.csv");
			reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
			String line = null;
			reader.readLine();//skip title line
			int count = 0;
			while((line=reader.readLine())!=null){
			    String item[] = line.split(",");
			    if(Utils.timeFormater(item[2]) == null || Utils.timeFormater(item[3]) == null || item[4].equals("")) {
			    	continue;
			    }
			    
			    UserRequest request = new UserRequest(Utils.timeFormater(item[2]), Utils.timeFormater(item[3]), item[5], item[4]);
			    double duration = (Utils.timeFormater(item[3]).getTime() - Utils.timeFormater(item[2]).getTime())/ (1000 * 60);
			    request.setDuration(duration);
			    //System.out.println(utils.timeFormater(item[2]).getTime() + " " + utils.timeFormater(item[3]).getTime()+ " " +duration);
			    
			    //request list to station
			    if(BSTable.containsKey(request.getLocation())) {
			    	BSTable.get(request.getLocation()).add(request);
			    }else {
			    	ArrayList<UserRequest> reqList = new ArrayList<>();
			    	reqList.add(request);
			    	BSTable.put(request.getLocation(), reqList);
			    }
			    
			    // item loaded
			    count ++;
			    if(count == 60) 
			    {
			    	break;
			    }
			  }
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return BSTable;
	}
	
}
