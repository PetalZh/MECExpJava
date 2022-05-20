package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import objs.BSDistancePair;
import objs.BaseStation;
import utilities.BSUtils;
import utilities.Constants;
import utilities.Utils;

public class RandomMethod {

	public RandomMethod() {
		super();
	}
	
	private void test_print_list(ArrayList<BaseStation> list) 
	{
		for(BaseStation bs : list) 
		{
			System.out.println("id: " + bs.getLocation() + " " + bs.getWorkload()); // + " " + bs.getCTMax()
			for(BSDistancePair p: bs.getAssignedBS()) 
			{
				System.out.println(" " + p.getBS().getLocation() + " " + p.getBS().getWorkload()); //+ " " + p.getBS().getCTMax()
			}
		}
	}
	
	public ArrayList<BaseStation> getResult(ArrayList<BaseStation> bsList) 
	{
		ArrayList<BaseStation> result = new ArrayList<>();
		//test_print_list(bsList); 
		
		while(bsList.size() != 0) {
			
			int random = getRandom(0, bsList.size());
			BaseStation candidate = bsList.get(random);
			
			
			result.add(candidate);
			
			ArrayList<BaseStation> connList = getConnected(candidate, bsList);
			
			// remove connected bs from bsList
			for(BaseStation bs : connList) {
				bsList.remove(bs);
			}
			
			bsList.remove(candidate);
			
//			System.out.println("**********************");
//			test_print_list(result);

		}
		//test_print_list(result);
		Utils.printResult(result, "Random result: ");
		
		return result;
	}
	
	private int  getRandom(int min, int max) 
	{
		Random random = new Random();
		int s = random.nextInt(max)%(max-min+1) + min;

		//System.out.println(s);
		
		return s;
	}

	private ArrayList<BaseStation> getConnected(BaseStation en, ArrayList<BaseStation> bsList)
	{
		ArrayList<BaseStation> connList = new ArrayList<>();
		for(BaseStation bs : bsList) 
		{
			double distance = Utils.getDistance(en.getLocation(), bs.getLocation());
			double trans_delay = Utils.getTransDelay(distance, bs.getCTMax() * Constants.SINGLE_TASK_SIZE);
			//trans_delay < Constants.DELAY_THRESH; distance <= Constants.DISTANCE_THRESH
			if( distance != 0 && trans_delay < Constants.DELAY_THRESH) {
				en.addBS(bs, distance, Constants.isPeak);
				connList.add(bs);
			}
		}
		return connList;
	}
}
