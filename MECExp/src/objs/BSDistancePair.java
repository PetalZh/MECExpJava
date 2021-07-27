package objs;

public class BSDistancePair implements Comparable{
	private BaseStation base_station;
	private double distance;
	
	
	public BSDistancePair(BaseStation base_station, double distance) {
		super();
		this.base_station = base_station;
		this.distance = distance;
	}


	public BaseStation getBS() {
		return base_station;
	}


//	public void setAssignedBs(BaseStation assignedBs) {
//		this.assignedBs = assignedBs;
//	}


	public double getDistance() {
		return distance;
	}


//	public void setDistance(double distance) {
//		this.distance = distance;
//	}
	
	@Override
	public int compareTo(Object o) {
		double compDistance = ((BSDistancePair) o).getDistance();
		double compare = compDistance - this.distance;
		
		if(compare > 0) 
		{
			return 1;
		}else {
			return -1;
		}

	}
}
