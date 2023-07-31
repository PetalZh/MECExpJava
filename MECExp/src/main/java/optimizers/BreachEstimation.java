package optimizers;

import objs.BSDistancePair;
import objs.BaseStation;
import objs.TimePoint;
import objs.UserRequest;
import org.slf4j.helpers.Util;
import utilities.Constants;
import utilities.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

public class BreachEstimation {
    private ArrayList<BaseStation> enList;

    public BreachEstimation(ArrayList<BaseStation> result)
    {
        this.enList = result;
    }

	private double getCapacityRequired(BaseStation en)
	{
		double maximum_capacity = 0;
		double enWorkload = getENWorkload(en);
		for(BSDistancePair pair : en.getAssignedBS())
		{
			BaseStation bs = pair.getBS();
			double distance = pair.getDistance();
//			System.out.println(bs.getWorkload() + "  " + en.getWorkload());
			double capacity = Utils.getCapacityRequired(distance, bs.getWorkload()*Constants.SINGLE_TASK_SIZE, enWorkload*Constants.SINGLE_TASK_SIZE);
//			System.out.println(capacity);
			if(maximum_capacity < capacity){
				maximum_capacity = capacity;
			}
		}
		return maximum_capacity;
	}

	private double getENWorkload(BaseStation en)
	{
		double total_workload = en.getWorkload();
		for(BSDistancePair pair : en.getAssignedBS())
		{
			total_workload += pair.getBS().getWorkload();
		}

		return  total_workload;
	}

	private ArrayList<ArrayList<UserRequest>> preprocess(ArrayList<BaseStation> enList)
	{
		ArrayList<ArrayList<UserRequest>> enRequestList = new ArrayList<>();
		for (BaseStation en : enList)
		{
			double enCapacity  = getCapacityRequired(en);
			en.updateCapacityRequired(enCapacity);

			ArrayList<UserRequest> uqList = new ArrayList<>();
			for(UserRequest r : en.getRequestList())
			{
				r.setFrom(en);
				r.setTo(en);
				uqList.add(r);
			}

			for(BSDistancePair pair : en.getAssignedBS())
			{
				BaseStation bs = pair.getBS();
				for(UserRequest r : bs.getRequestList())
				{
					r.setFrom(bs);
					r.setTo(en);
					uqList.add(r);
				}
			}
			enRequestList.add(uqList);
		}


		return enRequestList;
	}

	public void doEstimate()
	{
		ArrayList<ArrayList<UserRequest>> enRequestList = preprocess(enList);
		int violate = 0;
		int totalUR = 0;
		for(ArrayList<UserRequest> urList : enRequestList)
		{
			totalUR += urList.size();
			for(UserRequest ur : urList)
			{
				ArrayList<UserRequest> overlapList = getOverlapList(ur, urList);
				Hashtable<String, ArrayList<UserRequest>> bsTable = new Hashtable<>();
				for (UserRequest ur2 : overlapList){
					String key = ur2.getLocation();
					if(bsTable.containsKey(key))
					{
						bsTable.get(key).add(ur2);
					}else
					{
						ArrayList<UserRequest> list = new ArrayList<>();
						list.add(ur2);
						bsTable.put(key, list);
					}
				}

				// calculate capacity required
				double total_capacity = 0;
				for(String key : bsTable.keySet())
				{
					ArrayList<UserRequest> list = bsTable.get(key);
					String bsLocation = list.get(0).getFrom().getLocation();
					String enLocation = list.get(0).getTo().getLocation();
					double distance = Utils.getDistance(bsLocation, enLocation);
//					System.out.println("aaaa " + list.size() + "  " + bsTable.get(enLocation).size());
					double capacity = Utils.getCapacityRequired(distance, list.size() * Constants.SINGLE_TASK_SIZE, bsTable.get(enLocation).size() * Constants.SINGLE_TASK_SIZE);
					total_capacity += capacity;
				}
//				System.out.println(total_capacity);
//				System.out.println(urList.get(0).getTo().getCapacityRequired());
				if(total_capacity > urList.get(0).getTo().getCapacityRequired())
				{
					// violate
					violate++;
				}


			}
		}
		System.out.println("# of Violate: " + violate);
		System.out.println("# of UR total: " + totalUR);
	}



	private ArrayList<TimePoint> getTimeEntries(ArrayList<UserRequest> requestList)
	{
		ArrayList<TimePoint> pointList = new ArrayList<>();
		for(int i = 0; i < requestList.size(); i++) {
			try {
				pointList.add(new TimePoint(requestList.get(i).getStartTime(), 0));
				pointList.add(new TimePoint(requestList.get(i).getEndTime(), 1));
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}

		}

		Collections.sort(pointList);
		return pointList;
	}

	private ArrayList<UserRequest> getOverlapList(UserRequest ur, ArrayList<UserRequest> ur_list)
	{
		ArrayList<UserRequest> overlap_list = new ArrayList<>();

		for(UserRequest ur2: ur_list)
		{
			if(ur.getStartTime().getTime() <= ur2.getEndTime().getTime() || ur.getEndTime().getTime() >= ur2.getStartTime().getTime())
			{
				overlap_list.add(ur2);
			}
		}
		return overlap_list;
	}
}
