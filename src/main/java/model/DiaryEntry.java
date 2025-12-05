package model;

import java.sql.Date;

public class DiaryEntry {
	protected int id;
	protected int user_id;
	protected String name;
	protected String description;
	protected Date created_at;
	
	public DiaryEntry() {
		
	}
	
	public DiaryEntry(int id, int user_id, String name, String description, Date created_at) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.name = name;
		this.description = description;
		this.created_at = created_at;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getUserId() {
		return user_id;
	}
	
	public void setUserId(int user_id) {
		this.user_id = user_id;
	}
	
	public String getName()  {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getTimeCreate() {
		return created_at;
	}
	
	public void setTimeCreate(Date created_at) {
		this.created_at = created_at;
	}
}
