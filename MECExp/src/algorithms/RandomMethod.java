package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import objs.BSDistancePair;
import objs.BaseStation;
import utilities.BSUtils;
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
//			System.out.println("**********************");
//			test_print_list(result);
			
			int random = getRandom(0, bsList.size());
			BaseStation candidate = bsList.get(random);
			result.add(candidate);
			
			// remove connected bs from bsList
			ArrayList<BSDistancePair> conn = candidate.getAssignedBS();
			for(BSDistancePair bs : conn) {
				bsList.remove(bs.getBS());
			}
			
			bsList.remove(candidate);
			
			// recompute the connection
			BSUtils.getBSConnection(bsList);
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

}
