package model;

import java.util.Date;

public class WorkSchedule {
	
	private String id;
	private String userId;
	private String subject;
	private String description;
	private Date startTime;
	private Date endTime;
	private String priority; 
	private String location;
	private Date createdAt;
	
	public WorkSchedule() {}
	
	public WorkSchedule(String id, String userId, String subject, String description, Date startTime, Date endTime, String priority, String location, Date createdAt) {
		super();
		this.id = id;
		this.userId = userId;
		this.subject = subject;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.priority = priority;
		this.location = location;
		this.createdAt = createdAt;
	}
	
	public WorkSchedule(String userId, String subject, String description, String priority, String location) {
		this.userId = userId;
		this.subject = subject;
		this.description = description;
		this.priority = priority;
		this.location = location;
		this.startTime = new Date();
		this.endTime =new Date();
		this.createdAt = new Date();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
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
	
	public String getPriority() {
		return priority;
	}
	
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}