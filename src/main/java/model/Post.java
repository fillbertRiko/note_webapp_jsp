package model;

import java.util.Date;

public class Post {
	private String id;
	private String userId;
	private String topicId;
	private String title;
	private String content;
	private Date createdAt;
	private Date updatedAt;
	private String accessLevelId;
	private String allowComment;

	public Post() {
		
	}

	public Post(String id, String userId, String topicId, String title, String content, Date createdAt, Date updatedAt, String accessLevelId, String allowComment) {
		super();
		this.id = id;
		this.userId = userId;
		this.topicId = topicId;
		this.title = title;
		this.content = content;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.accessLevelId = accessLevelId;
		this.allowComment = allowComment;
	}
	
	public Post(String userId, String topicId, String title, String content, String accessLevelId, String allowComment) {
		this.userId = userId;
		this.topicId = topicId;
		this.title = title;
		this.content = content;
		this.accessLevelId = accessLevelId;
		this.allowComment = allowComment;
		this.createdAt = new Date();
		this.updatedAt = new Date();
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
	
	public String getTopicId() {
		return topicId;
	}
	
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
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
	
	public void setTimeCreate(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Date getTimeUpdate() {
		return updatedAt;
	}
	
	public void setTimeUpdate(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public String getAccessLevelId() {
		return accessLevelId;
	}
	
	public void setAccessLevelId(String accessLevelId) {
		this.accessLevelId = accessLevelId;
	}
	
	public String getNumberAllowComment() {
		return allowComment;
	}
	
	public void setNumberAllowComment(String allowComment) {
		this.allowComment = allowComment;
	}
}
