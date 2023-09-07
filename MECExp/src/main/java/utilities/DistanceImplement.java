package utilities;

import objs.BaseStation;
import smile.math.distance.Distance;

public class DistanceImplement implements Distance<BaseStation>{
	
	public DistanceImplement() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public double d(BaseStation bs1, BaseStation bs2) {
		return Utils.getDistance(bs1.getLocation(), bs2.getLocation());
	}
}
