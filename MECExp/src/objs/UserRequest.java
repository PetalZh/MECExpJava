package objs;

import java.util.Date;

public class UserRequest {
	private Date startTime;
	private Date endTime;
	private String userId;
	private String location;
	private double duration;
	
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
}