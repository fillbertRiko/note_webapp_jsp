package model;

import java.sql.Date;

public class Comments {
	private String id;
	private String post_id;
	private String user_id;
	private String content;
	private Date created_at;
	
	public Comments() {};
	public Comments(String id, String post_id, String user_id, String content, Date created_at) {
		super();
		this.id = id;
		this.post_id = post_id;
		this.content = content;
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
	
	public String getPostId() {
		return post_id;
	}
	
	public void setPostId(String post_id) {
		this.post_id = post_id;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getTimeCreate() {
		return created_at;
	}
	
	public void setTimeCteate(Date created_at) {
		this.created_at = created_at;
	}
}
