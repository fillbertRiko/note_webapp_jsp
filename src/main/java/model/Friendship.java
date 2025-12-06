package model;

import java.sql.Date;

public class Friendship {
	private int id;
	private int user_id;
	private int friend_id;
	private Date created_at;
	
	public Friendship() {}
	
	public Friendship(int id, int user_id, int friend_id, Date created_at) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.friend_id = friend_id;
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
	
	public int getFriendId() {
		return friend_id;
	}
	
	public void setFriendId(int friend_id) {
		this.friend_id = friend_id;
	}
	
	public Date getTimeCreate() {
		return created_at;
	}
	
	public void setTimeCreate(Date created_at) {
		this.created_at = created_at;
	}
}
