package utilities;

import objs.BaseStation;
import smile.math.distance.Distance;

public class DistanceImplement implements Distance{
	
	public DistanceImplement() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public double d(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		BaseStation bs1 = (BaseStation)arg0;
		BaseStation bs2 = (BaseStation)arg1;
		
		
		return Utils.getDistance(bs1.getLocation(), bs2.getLocation());
	}

}
