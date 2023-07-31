package objs;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

/*
 * type:
 * 0 - start time
 * 1 - end time 
 * */
public class TimePoint implements Comparable{
	private Date time;
	private int type;
	
	public TimePoint(Date time, int type) {
		super();
		this.time = time;
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public int compareTo(Object o) {
		
		long comparageTime = ((TimePoint) o).getTime().getTime();
		int comparageType = ((TimePoint) o).getType();
		if(this.time.getTime() == comparageTime) {
			//end before start
			return comparageType - this.type; 
		}else {
			// Asc order
			return Long.compare(this.time.getTime(), comparageTime); // (int) (this.time.getTime() - comparageTime);
		}
		
	}
	
	

}
