package model;

import java.util.Date;

public class Friendship {
	private String id;
	private String userId1;
	private String userId2;
	private Date createdAt;

	public Friendship() {
	}

	public Friendship(String id, String userId1, String userId2, Date createdAt) {
		super();
		this.id = id;
		this.userId1 = userId1;
		this.userId2 = userId2;
		this.createdAt = createdAt;
	}

	public Friendship(String userId1, String userId2) {
		this.userId1 = userId1;
		this.userId2 = userId2;
		this.createdAt = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId1() {
		return userId1;
	}

	public void setUserId1(String userId1) {
		this.userId1 = userId1;
	}

	public String getUserId2() {
		return userId2;
	}

	public void setUserId2(String userId2) {
		this.userId2 = userId2;
	}

	public Date getTimeCreate() {
		return createdAt;
	}

	public void setTimeCreate(Date createdAt) {
		this.createdAt = createdAt;
	}
}
