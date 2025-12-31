package model;

import java.util.Date;

public class Friendship {
	private String id;
	private String userId;
	private String friendId;
	private Date createdAt;

	public Friendship() {
	}

	public Friendship(String id, String userId, String friendId, Date createdAt) {
		super();
		this.id = id;
		this.userId = userId;
		this.friendId = friendId;
		this.createdAt = createdAt;
	}

	public Friendship(String userId, String friendId) {
		this.userId = userId;
		this.friendId = friendId;
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

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public Date getTimeCreate() {
		return createdAt;
	}

	public void setTimeCreate(Date createdAt) {
		this.createdAt = createdAt;
	}
}
