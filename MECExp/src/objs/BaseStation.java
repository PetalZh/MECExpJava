package objs;

import java.util.ArrayList;

public class BaseStation {
	private int serverNo;
	private int CTMax;
	private int workload;
	private String location;
	private ArrayList<UserRequest> requestList;
	private ArrayList<BaseStation> assignedBS;
	
	public BaseStation(String location) {
		super();
		this.location = location;
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

	public int getWorkload() {
		return workload;
	}

	public void setWorkload(int workload) {
		this.workload = workload;
	}

	public ArrayList<BaseStation> getAssignedBS() {
		return assignedBS;
	}

	public void setAssignedBS(ArrayList<BaseStation> assignedBS) {
		this.assignedBS = assignedBS;
	}
	
	
	
	
}
