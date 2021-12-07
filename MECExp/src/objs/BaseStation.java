package objs;

import java.util.ArrayList;
import java.util.Comparator;

import utilities.Constants;
import utilities.Utils;


// implements Comparable
public class BaseStation {
	private int serverNo;
	private int CTMax;
	private double workload;
	private String location;
	private ArrayList<UserRequest> requestList;
	private ArrayList<BSDistancePair> assignedBS;
//	private BaseStation connectedEN;
	private ArrayList<BaseStation> overlapped;
	private ArrayList<UserRequest> assignedURs;
	private double capacityRequired;
	private int cost;
	
	public BaseStation(String location) {
		super();
		this.location = location;
		this.assignedBS = new ArrayList<>();
		this.overlapped = new ArrayList<>();
		assignedURs = new ArrayList<>();
		this.capacityRequired = 0;
	}
	
	public int getServerNo() {
		return serverNo;
	}

	public void setServerNo(int serverNo) {
		this.serverNo = serverNo;
	}

	public int getCTMax() {
		return CTMax;
	}

	public void setCTMax(int cTMax) {
		CTMax = cTMax;
		double value = (cTMax * Constants.SINGLE_TASK_SIZE)/Constants.DELAY_THRESH;
		this.workload += Utils.handlePrecision(value);
		this.workload = Utils.handlePrecision(this.workload);
	}

	public double getWorkload() {
		return workload;
	}
	
	public void initWorkload() 
	{
		double value = (this.CTMax * Constants.SINGLE_TASK_SIZE)/Constants.DELAY_THRESH;
		this.workload = Utils.handlePrecision(value);
	}

//	public void setWorkload(float workload) {
//		this.workload = workload;
//	}

	public ArrayList<BSDistancePair> getAssignedBS() {
		return assignedBS;
	}
//
//	public void setAssignedBS(ArrayList<BaseStation> assignedBS) {
//		this.assignedBS = assignedBS;
//	}
	
	public ArrayList<UserRequest> getAssignedURs() {
		return assignedURs;
	}
	
	public void addUR(UserRequest r) {
		this.assignedURs.add(r);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public void clearBS() 
	{
		this.assignedBS.clear();
	}
	
	public void addBS(BaseStation bs, double distance) 
	{
		this.assignedBS.add(new BSDistancePair(bs, distance));
		double value = Utils.getCapacityRequired(distance, bs.getCTMax() * Constants.SINGLE_TASK_SIZE);
		if(value < 0) 
		{
			System.out.println("add value: " + value);
			System.out.println("add id: " + bs.getLocation());
			System.out.println("bs: " + bs.getLocation() + " " + distance + " " + bs.getCTMax() * Constants.SINGLE_TASK_SIZE);
		}
//		System.out.println("add value: " + value);
//		System.out.println("add id: " + bs.getLocation());
//		System.out.println("bs: " + bs.getLocation() + " " + distance + " " + bs.getCTMax() * Constants.SINGLE_TASK_SIZE);
		this.workload += value;
		this.workload = Utils.handlePrecision(this.workload);
		//this.workload += bs.getCTMax() * Constants.SINGLE_TASK_SIZE;
	}
	
	
	public boolean removeBS(BaseStation bs) 
	{
		for(BSDistancePair p : this.assignedBS) 
		{
			if(p.getBS().getLocation().equals(bs.getLocation())) 
			{
				this.assignedBS.remove(p);
				double value = Utils.getCapacityRequired(p.getDistance(), p.getBS().getCTMax() * Constants.SINGLE_TASK_SIZE);
//				System.out.println("remove value: " + value);
//				System.out.println("Remove id: " + bs.getLocation());
//				System.out.println("p: " + p.getBS().getLocation() + " " + p.getDistance() + " " + p.getBS().getCTMax() * Constants.SINGLE_TASK_SIZE);
				this.workload -= value;
				this.workload = Utils.handlePrecision(this.workload);
				
				if(value < 0) 
				{
					System.out.println("error capacity: " + value);
				}
				if(this.workload < 0) 
				{
					System.out.println("remove value: " + value);
					System.out.println("Remove id: " + bs.getLocation());
					System.out.println("p: " + p.getBS().getLocation() + " " + p.getDistance() + " " + p.getBS().getCTMax() * Constants.SINGLE_TASK_SIZE);
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	public void excludeSelfWorkload() 
	{
		double value = (this.CTMax * Constants.SINGLE_TASK_SIZE)/Constants.DELAY_THRESH;
		this.workload -= Utils.handlePrecision(value);
		this.workload = Utils.handlePrecision(this.workload);
	}
	
	public void includeSelfWorkload() 
	{
		double value = (this.CTMax * Constants.SINGLE_TASK_SIZE)/Constants.DELAY_THRESH;
		this.workload += Utils.handlePrecision(value);
		this.workload = Utils.handlePrecision(this.workload);
	}
	

	public ArrayList<BaseStation> getOverlapped() {
		return overlapped;
	}

	public void addOverlappedBS(BaseStation bs) 
	{
		if(!this.overlapped.contains(bs)) 
		{
			this.overlapped.add(bs);
		}
		
	}
	
	public void removeOverlappedBS(BaseStation bs) {
		this.overlapped.remove(bs);
	}
	
	public int getCost() 
	{
		return this.cost;
	}
	
	public ArrayList<UserRequest> getRequestList() {
		return requestList;
	}

	public void setRequestList(ArrayList<UserRequest> requestList) {
		this.requestList = requestList;
	}
	
	public double getCapacityRequired() {
		return capacityRequired;
	}

//	public void setCapacityRequired(double capacityRequired) {
//		this.capacityRequired = capacityRequired;
//	}
//	
	public void updateCapacityRequired(double capacityRequired) {
		if(capacityRequired > this.capacityRequired) 
		{
			this.capacityRequired = capacityRequired;
			this.workload = capacityRequired;
		}
		
	}
	

//	@Override
//	public int compareTo(Object o) {
//		double compWorkload = ((BaseStation) o).getWorkload();
//		double compare = compWorkload - this.workload;
//		
//		return Double.compare(compWorkload, this.workload);
//
//	}
	

	public static Comparator<BaseStation> getCapacityComparator()
	{
		return new Comparator<BaseStation>() 
		{
			@Override
			public int compare(BaseStation o1, BaseStation o2) {
				// TODO Auto-generated method stub
				return Double.compare(o2.workload, o1.workload);
			}
			
		};
	}
	
	public static Comparator<BaseStation> getConnectionComparator()
	{
		return new Comparator<BaseStation>() 
		{
			@Override
			public int compare(BaseStation o1, BaseStation o2) {
				// TODO Auto-generated method stub
				return Integer.compare(o2.getAssignedBS().size(), o1.getAssignedBS().size());
			}
			
		};
	}
	
	// comparator for workload/cost
	
//	@Override
//	public int compareTo(Object o) {
//		double ratio_compare = ((BaseStation) o).getWorkload()/((BaseStation) o).getCost();
//		double ratio = this.workload * 1000 / this.cost;
//		
////		System.out.println("ratio_comp: " + ratio_compare);
//		//System.out.println("ratio: " + ratio);
////		System.out.println("cost: " + this.cost);
////		
////		System.out.println(Double.compare(ratio_compare, ratio));
//		
//		return Double.compare(ratio_compare, ratio);
//
//	}
	
	public int getTotalCost() 
	{
//		double totalCapacity = (this.CTMax * Constants.SINGLE_TASK_SIZE)/Constants.DELAY_THRESH;
//		for(BSDistancePair bs : this.assignedBS) 
//		{
//			double distance = bs.getDistance();
//			double bs_task_size = bs.getBS().getCTMax() * Constants.SINGLE_TASK_SIZE;
//			double capacity_req = Utils.getCapacityRequired(distance, bs_task_size);
//			
//			totalCapacity += capacity_req;
//		}
		
//		int serverNum = (int)Math.ceil(totalCapacity / Constants.SINGLE_SERVER_CAPACITY);
//		
//		int cost = serverNum * Constants.COST_SERVER + Constants.COST_EN;
//		
//		this.cost = cost;
		
		int cost = Constants.COST_EN + (int)Math.ceil(this.workload /Constants.SINGLE_SERVER_CAPACITY) * Constants.COST_SERVER;
		this.cost = cost;
		
		return cost;
	}
}
