package model;

import java.sql.Date;
import java.util.List;

public class WorkSchedule {
	private String id;
	private String user_id;
	private String subject;
	private String description;
	private Date start_time;
	private Date end_time;
	private List<String> piority;
	private String location;
	private Date created_at;
	
	public WorkSchedule() {}
	
	public WorkSchedule(String id, String user_id, String subject, String description, Date start_time, Date end_time, List<String> piority, String location, Date created_at) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.subject = subject;
		this.description = description;
		this.start_time = start_time;
		this.end_time = end_time;
		this.piority = piority;
		this.location = location;
		this.created_at = created_at;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserId() {
		return user_id;
	}
	
	public void setUserId(String user_id) {
		this.user_id = user_id;
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
		return start_time;
	}
	
	public void setStartTime(Date start_time) {
		this.start_time = start_time;
	}
	
	public Date getEndTime() {
		return end_time;
	}
	
	public void setEndTime(Date end_time) {
		this.end_time = end_time;
	}
	
	public List<String> getPiority() {
		return piority;
	}
	
	public void setPiority(List<String> piority) {
		this.piority = piority;
	}
	
	public String getLocaion() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public Date getTimeCreated() {
		return created_at;
	}
	
	public void setTimeCreated(Date created_at) {
		this.created_at = created_at;
	}
}
