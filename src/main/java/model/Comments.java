package model;

import java.util.Date;

public class Comments {
	private String id;
	private String postId;
	private String userId;
	private String content;
	private Date createdAt;

	public Comments() {
	}

	public Comments(String id, String postId, String userId, String content, Date createdAt) {
		super();
		this.id = id;
		this.postId = postId;
		this.userId = userId;
		this.content = content;
		this.createdAt = createdAt;
	}

	public Comments(String postId, String userId, String content) {
		this.postId = postId;
		this.userId = userId;
		this.content = content;
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

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTimeCreate() {
		return createdAt;
	}

	public void setTimeCteate(Date createdAt) {
		this.createdAt = createdAt;
	}
}
