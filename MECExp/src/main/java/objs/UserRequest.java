package objs;

import java.util.Date;
import java.lang.Comparable;

public class UserRequest implements Comparable<UserRequest> {
	private Date startTime;
	private Date endTime;
	private String userId;
	private String location;
	private double duration;
	private BaseStation from;
	private BaseStation to;
	
	public UserRequest(Date startime, Date endTime, String userId, String location) 
	{
		this.startTime = startime;
		this.endTime = endTime;
		this.userId = userId;
		this.location = location;
	}


	public Date getStartTime() {
		return startTime;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	public Date getEndTime() {
		return endTime;
	}


	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


	public double getDuration() {
		return duration;
	}


	public void setDuration(double duration) {
		this.duration = duration;
	}


	public BaseStation getFrom() {
		return from;
	}


	public void setFrom(BaseStation from) {
		this.from = from;
	}


	public BaseStation getTo() {
		return to;
	}


	public void setTo(BaseStation to) {
		this.to = to;
	}

	@Override
	public int compareTo(UserRequest o) {
		int startComparison = this.startTime.compareTo(o.startTime);
		return startComparison != 0 ? startComparison : this.endTime.compareTo(o.endTime);
	}
}
