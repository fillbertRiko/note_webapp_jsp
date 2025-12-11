package model;

import java.sql.Date;

public class UserLayout {
	private String id;
	private String user_id;
	private String homepage_config;
	private Date updated_at;
	
	public UserLayout() {}
	
	public UserLayout(String id, String user_id, String homepage_config, Date updated_at) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.homepage_config = homepage_config;
		this.updated_at = updated_at;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id =id;
	}
	
	public String getUserId() {
		return user_id;
	}
	
	public void setUserId(String user_id) {
		this.user_id = user_id;
	}
	
	public String getHomepageConfig() {
		return homepage_config;
	}
	
	public void setHomepageConfig(String homepage_config) {
		this.homepage_config = homepage_config;
	}
	
	public Date getTimeUpdate() {
		return updated_at;
	}
	
	public void setTimeUpdate(Date updated_at) {
		this.updated_at = updated_at;
	}
}
