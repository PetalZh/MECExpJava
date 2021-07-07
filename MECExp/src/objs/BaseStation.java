package objs;

import java.util.ArrayList;

public class BaseStation implements Comparable {
	private int serverNo;
	private int CTMax;
	private float workload;
	private String location;
	private ArrayList<UserRequest> requestList;
	private ArrayList<BaseStation> assignedBS;
	
	public BaseStation(String location) {
		super();
		this.location = location;
		this.assignedBS = new ArrayList<>();
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
	}

	public float getWorkload() {
		return workload;
	}

	public void setWorkload(float workload) {
		this.workload = workload;
	}

	public ArrayList<BaseStation> getAssignedBS() {
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
	
	public void addBS(BaseStation bs) 
	{
		this.assignedBS.add(bs);
	}
	
	public boolean removeBS(BaseStation bs) 
	{
		return this.assignedBS.remove(bs);
	}

	@Override
	public int compareTo(Object o) {
		float compWorkload = ((BaseStation) o).getWorkload();
		
		return (int)Math.ceil(compWorkload - this.workload);
	}
}
