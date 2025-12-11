package model;

import java.sql.Date;

public class Friendship {
	private String id;
	private String user_id;
	private String friend_id;
	private Date created_at;
	
	public Friendship() {}
	
	public Friendship(String id, String user_id, String friend_id, Date created_at) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.friend_id = friend_id;
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
	
	public String getFriendId() {
		return friend_id;
	}
	
	public void setFriendId(String friend_id) {
		this.friend_id = friend_id;
	}
	
	public Date getTimeCreate() {
		return created_at;
	}
	
	public void setTimeCreate(Date created_at) {
		this.created_at = created_at;
	}
}
