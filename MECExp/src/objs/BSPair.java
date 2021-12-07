package objs;

import java.util.Comparator;

public class BSPair {
	private BaseStation bs1;
	private BaseStation bs2;
	private double distance;
	
	
	
	public BSPair() {
		super();
	}
	
	public BSPair(BaseStation bs1, BaseStation bs2, double distance) {
		super();
		this.bs1 = bs1;
		this.bs2 = bs2;
		this.distance = distance;
	}



	public BaseStation getBs1() {
		return bs1;
	}
	public void setBs1(BaseStation bs1) {
		this.bs1 = bs1;
	}
	public BaseStation getBs2() {
		return bs2;
	}
	public void setBs2(BaseStation bs2) {
		this.bs2 = bs2;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public static Comparator<BSPair> getComparator()
	{
		return new Comparator<BSPair>() 
		{
			@Override
			public int compare(BSPair o1, BSPair o2) {
				// ASD
				return Double.compare(o1.distance, o2.distance);
			}
			
		};
	}
	

}
