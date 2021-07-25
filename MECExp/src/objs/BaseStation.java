package objs;

import java.util.ArrayList;

import utilities.Constants;
import utilities.Utils;


public class BaseStation implements Comparable {
	private int serverNo;
	private int CTMax;
	private float workload;
	private String location;
	private ArrayList<UserRequest> requestList;
	private ArrayList<BSDistancePair> assignedBS;
//	private BaseStation connectedEN;
	private ArrayList<BaseStation> overlapped;
	
	public BaseStation(String location) {
		super();
		this.location = location;
		this.assignedBS = new ArrayList<>();
		this.overlapped = new ArrayList<>();
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
		this.workload += cTMax * Constants.SINGLE_TASK_SIZE;
	}

	public float getWorkload() {
		return workload;
	}
	
	public void initWorkload() 
	{
		this.workload = this.CTMax * Constants.SINGLE_TASK_SIZE;
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
		this.workload += (float)Utils.getCapacityRequired(distance, bs.getCTMax()*Constants.SINGLE_TASK_SIZE);
		//this.workload += bs.getCTMax() * Constants.SINGLE_TASK_SIZE;
	}
	
	
	public boolean removeBS(BaseStation bs) 
	{
		for(BSDistancePair p : this.assignedBS) 
		{
			if(p.getBS().getLocation().equals(bs.getLocation())) 
			{
				this.assignedBS.remove(p);
				this.workload -= (float)Utils.getCapacityRequired(p.getDistance(), p.getBS().getCTMax()*Constants.SINGLE_TASK_SIZE);
				//this.workload -= p.getBS().getCTMax() * Constants.SINGLE_TASK_SIZE;
				return true;
			}
		}
		
		return false;
	}
	
	public void excludeSelfWorkload() 
	{
		this.workload -= this.CTMax * Constants.SINGLE_TASK_SIZE;
	}
	
	public void includeSelfWorkload() 
	{
		this.workload += this.CTMax * Constants.SINGLE_TASK_SIZE;
	}
	

//	public BaseStation getConnectedEN() {
//		return connectedEN;
//	}
//
//	public void setConnectedEN(BaseStation connectedEN) {
//		this.connectedEN = connectedEN;
//	}

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
	
	
//	public void setOverlapped(ArrayList<BaseStation> overlapped) {
//		this.overlapped = overlapped;
//	}

	@Override
	public int compareTo(Object o) {
		float compWorkload = ((BaseStation) o).getWorkload();
		
		double compare = (double)compWorkload - (double)this.workload;
		//System.out.println(compare + " ");
		if(compare != 0 && compare > 0.0) 
		{
			return 1;
		}else {
			return -1;
		}

	}
}
