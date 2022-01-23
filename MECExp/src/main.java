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
import algorithms.HieraCluster2;
import algorithms.MIPAlgo;
import algorithms.Partition;
import algorithms.RandomMethod;
import objs.BaseStation;
import objs.Cluster;
import objs.UserRequest;
import optimizers.DynamicGreedy;
import utilities.BSUtils;
import utilities.Constants;
import utilities.FileIO;
import utilities.Utils;

public class main {
	static int input_size = 0;
	static ArrayList<UserRequest> userRequests = new ArrayList<>();
	
	/*
	 * args[0] theta value
	 * args[1] input bs range
	 * args[2] 0: peak 1: avg
	 * */
	public static void main(String args[]) {
		
		Hashtable<String, ArrayList<UserRequest>> BSTable = readDoc();
		ArrayList<BaseStation> bsList = BSUtils.getBSList(BSTable);

//		double[] delay_thresh = {10, 12, 14, 16, 18, 20, 22, 24, 26};
//		double[] delay_thresh = {18};
		
		double delay_thresh = Double.valueOf(args[0]);
		int bs_range = Integer.parseInt(args[1]);
		int workload_type = Integer.parseInt(args[2]);
//		Constants.DELAY_THRESH = delay_thresh;
		
//		int workload_type = Integer.parseInt(args[1]);

		
		Constants.DELAY_THRESH = delay_thresh;
		Constants.DISTANCE_THRESH = Utils.getDistanceThreshold(Constants.CTMAX * Constants.SINGLE_TASK_SIZE);
		
		if(workload_type == 0) 
		{
			Constants.isPeak = true;
		}else {
			Constants.isPeak = false;
		}

		for(int i = 1; i <= 1; i++) 
		{
			startExp(bsList, bs_range, false);
		}


//		for(double d : delay_thresh) 
//		{
//			Constants.DELAY_THRESH = d;
//			Constants.DISTANCE_THRESH = Utils.getDistanceThreshold(Constants.CTMAX * Constants.SINGLE_TASK_SIZE);
//			
//			for(int i = 1; i <= 1; i++) 
//			{
//				startExp(bsList, false);
//			}
//			
//		}

		Constants.DISTANCE_THRESH = Utils.getDistanceThreshold(Constants.CTMAX * Constants.SINGLE_TASK_SIZE);
		
		System.out.println("Distance threshold: " + Constants.DISTANCE_THRESH);
//		int range = 3000;
//		if(range >= bsList.size()) 
//		{
//			range = bsList.size() - 1;
//		}
//		
//		input_size = range + 1;
		
//		for(int i = 5; i <= 30; i++) 
//		{
//			System.out.println("tau: " + i);
//			greedyNew(new ArrayList<BaseStation>(bsList.subList(0, range)), i);
//		}
		
//		random(new ArrayList<BaseStation>(bsList.subList(0, range)));
//		greedy(new ArrayList<BaseStation>(bsList.subList(0, range)));
//		greedyNew(new ArrayList<BaseStation>(bsList.subList(0, range)), 10);
		
		//mip(new ArrayList<BaseStation>(bsList.subList(0, range)));
		
		
		
//		double capacityReq = Utils.getCapacityRequired(1026, 75);
//		System.out.println("capacity: "+ capacityReq);

//		System.out.println("theta: "+ delay_thresh);
//		System.out.println("Distance threshold: "+ Constants.DISTANCE_THRESH);
//		System.out.println("distance: " + Utils.getDistanceThreshold(30));
//		System.out.println("tans delay: " + Utils.getTransDelay(3369, 30));
	}
	
	private static void startExp(ArrayList<BaseStation> bsList, int bs_range, boolean includeMIP) 
	{
		int r = bs_range; 
		
		int range = r - 1;
		if(range >= bsList.size()) 
		{
			range = bsList.size() - 1;
		}
		input_size = range + 1;
		
		System.out.println("---------------------------------");
		System.out.println(input_size + " BS used" + ", theta = " + Constants.DELAY_THRESH);
		
//		random(new ArrayList<BaseStation>(bsList.subList(0, range)));

//		greedy(new ArrayList<BaseStation>(bsList.subList(0, range)));
//		greedyNew(new ArrayList<BaseStation>(bsList.subList(0, range + 1)), 10);
		
		clusterAndMIP(new ArrayList<BaseStation>(bsList.subList(0, range)));
//		mip(new ArrayList<BaseStation>(bsList.subList(0, range)));
//		if(includeMIP && range <= 500) 
//		{
//			mip(new ArrayList<BaseStation>(bsList.subList(0, range)));
//		}

	}
	
	private static void startExp(ArrayList<BaseStation> bsList, boolean includeMIP) 
	{
		int[] range_input = {100, 150, 200, 250, 300, 350, 400, 450, 500, 600, 800, 1000, 1500, 2000, 2500, 3100};
//		int[] range_input = {500, 800, 1000, 1500, 2000, 2500, 3100}; 
//		int[] range_input = {1500};
		
		for(int r : range_input) 
		{
			int range = r - 1;
			if(range >= bsList.size()) 
			{
				range = bsList.size() - 1;
			}
			input_size = range + 1;
			
//			System.out.println("---------------------------------");
//			System.out.println(input_size + " BS used" + ", theta = " + Constants.DELAY_THRESH);
			
//			random(new ArrayList<BaseStation>(bsList.subList(0, range)));
//			
			greedy(new ArrayList<BaseStation>(bsList.subList(0, range)));
			greedyNew(new ArrayList<BaseStation>(bsList.subList(0, range + 1)), 10);
			
//			HieraCluster cluster = new HieraCluster();
//			cluster.getResult(new ArrayList<BaseStation>(bsList.subList(0, range)));
			
//			HieraCluster2 cluster = new HieraCluster2(210);
//			ArrayList<ArrayList<BaseStation>> clusters = cluster.getResult(new ArrayList<BaseStation>(bsList.subList(0, range)));
			
			clusterAndMIP(new ArrayList<BaseStation>(bsList.subList(0, range)));
			
			//mip(new ArrayList<BaseStation>(bsList.subList(0, range)));

//			if(includeMIP && range <= 500) 
//			{
//				mip(new ArrayList<BaseStation>(bsList.subList(0, range)));
//			}
			
		}
		
		//hieraCluster((ArrayList<BaseStation>) bsList.clone());
	}
	
	private static void hieraCluster(ArrayList<BaseStation> bsList) 
	{
		Date start = new Date();
		
		HieraCluster hieraCluster = new HieraCluster();
		ArrayList<Cluster> clusterList = hieraCluster.getResult(bsList);
		
		Date end = new Date();
		System.out.println("Running time: " + (double)(end.getTime() - start.getTime())/(double)1000 + " s");
	}
	
	private static void random(ArrayList<BaseStation> bsList) 
	{
		// Greedy methods
		Date start = new Date();
		
		//BSUtils.getBSConnection(bsList);
		RandomMethod random = new RandomMethod();
		ArrayList<BaseStation> result = random.getResult(bsList);
		
		Date end = new Date();
		
		String time = String.valueOf((double)(end.getTime() - start.getTime())/(double)1000);
		FileIO.output(result, input_size, time,  "random");
		FileIO.outputDistribution(result, input_size, "random");
		
		//FileIO.outputResult(result, time,  "random");
		System.out.println("Running time: " + time + " s");
	}
	
	private static void greedy(ArrayList<BaseStation> bsList) 
	{
		// Greedy methods
		Date start = new Date();
		
		ArrayList<BaseStation> bsList_copy = (ArrayList<BaseStation>)bsList.clone(); 
		
		BSUtils.getBSConnection(bsList);

		Greedy greedy = new Greedy();
		ArrayList<BaseStation> result = greedy.getResult(bsList);
		
		Date end = new Date();
		
		String time = String.valueOf((double)(end.getTime() - start.getTime())/(double)1000);
		
		FileIO.output(result, input_size, time,  "greedy");
//		FileIO.outputDistribution(result, input_size, "greedy");
		
		//FileIO.outputResult(result, time,  "Greedy" + Constants.DELAY_THRESH);
		
		System.out.println("Running time: " + time + " s");
		
		
		if(Constants.isPeak) 
		{
			Date start2 = new Date();
			ArrayList<BaseStation> result2 = greedy_dynamic(bsList_copy, result, false);
			Date end2 = new Date();
			
			String time2 = String.valueOf(((double)(end.getTime() - start.getTime()) + (double)(end2.getTime() - start2.getTime()))/(double)1000);
			
			FileIO.output(result2, input_size, time2,  "greedy_fine");
		}
	}
	
	private static void greedyNew(ArrayList<BaseStation> bsList, int threshold) 
	{
		Date start = new Date();
		
		ArrayList<BaseStation> bsList_copy = (ArrayList<BaseStation>)bsList.clone(); // this is for original list for dynamic case
		
		BSUtils.getBSConnection(bsList);
		GreedyNew greedy = new GreedyNew();
		ArrayList<BaseStation> result = greedy.getResult(bsList, threshold);
		
		Date end = new Date();
		
		String time = String.valueOf((double)(end.getTime() - start.getTime())/(double)1000);
		FileIO.output(result, input_size, time,  "greedy_new");
		
		//FileIO.outputTau(result, threshold);
		//FileIO.outputDistribution(result, input_size, "greedy_new");
		
		FileIO.outputResult(result, time,  "GreedyNew" + Constants.DELAY_THRESH);
		
		System.out.println("Running time: " + time + " s");
		
		if(Constants.isPeak) 
		{
			Date start2 = new Date();
			ArrayList<BaseStation> result2 = greedy_dynamic(bsList_copy, result, true);
			Date end2 = new Date();
			String time2 = String.valueOf(((double)(end.getTime() - start.getTime()) + (double)(end2.getTime() - start2.getTime()))/(double)1000);
			
			FileIO.output(result2, input_size, time2,  "greedy_new_fine");
		}
	}
	
	private static ArrayList<BaseStation> greedy_dynamic(ArrayList<BaseStation> bsList, ArrayList<BaseStation> enList, boolean withCandidate) 
	{
		Date start = new Date();
		DynamicGreedy dynamicGreedy = new DynamicGreedy();
		ArrayList<BaseStation> result = dynamicGreedy.dynamicAssign(bsList, enList, userRequests, withCandidate);
		Date end = new Date();
		String time = String.valueOf((double)(end.getTime() - start.getTime())/(double)1000);
		//System.out.println("Running time: " + time + " s");
		
		return result;
	}
	
	private static void clusterAndMIP(ArrayList<BaseStation> bsList)
	{
		Date start = new Date();
		HieraCluster2 hie_cluster = new HieraCluster2(210);
		ArrayList<ArrayList<BaseStation>> clusters = hie_cluster.getResult(bsList);
		
		double total_cost = 0;
		int total_en = 0;

		ArrayList<BaseStation> result = new ArrayList<>();
		for(ArrayList<BaseStation> cluster : clusters)
		{
			MIPAlgo mip = new MIPAlgo();
			mip.getMIP(cluster);

			total_cost += mip.getCost();
			total_en += mip.getEn_num();

			result.addAll(mip.getEnList());
		}
		Date end = new Date();
		
		String time = String.valueOf((double)(end.getTime() - start.getTime())/(double)1000);

		FileIO.outputDistribution(result, input_size, "mip_cluster");
		FileIO.output_mip((int)Math.round(total_cost), total_en, input_size, time, "mip_cluster");
		
		System.out.println("Total cost: " + total_cost);
		System.out.println("Total en: " + total_en);
		System.out.println("Running time: " + time + " s");
	}
	
	private static void mip(ArrayList<BaseStation> input)
	{
		Date start = new Date();
		
		MIPAlgo mip = new MIPAlgo();
		mip.getMIP(input);
		
		Date end = new Date();
		
		String time = String.valueOf((double)(end.getTime() - start.getTime())/(double)1000);
		FileIO.output_mip(mip.getCost(), mip.getEn_num(), input_size, time, "mip");
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
			    
			    userRequests.add(request);
			    //request list to station
			    if(BSTable.containsKey(request.getLocation())) {
			    	BSTable.get(request.getLocation()).add(request);
			    }else {
			    	ArrayList<UserRequest> reqList = new ArrayList<>();
			    	reqList.add(request);
			    	BSTable.put(request.getLocation(), reqList);
			    }
			    
//			     item loaded
//			    count ++;
//			    if(count == 100)
//			    {
//			    	break;
//			    }
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
