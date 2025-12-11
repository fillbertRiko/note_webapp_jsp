package model;

import java.sql.Date;

public class Post {
	private String id;
	private String user_id;
	private String topic_id;
	private String title;
	private String content;
	private Date created_at;
	private Date updated_at;
	private String access_level_id;
	private String allow_comment;

	public Post() {
		
	}

	public Post(String id, String user_id, String topic_id, String title, String content, Date created_at, Date updated_at, String access_level_id, String allow_comment) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.topic_id = topic_id;
		this.title = title;
		this.content = content;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.access_level_id = access_level_id;
		this.allow_comment = allow_comment;
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
	
	public String getTopicId() {
		return topic_id;
	}
	
	public void setTopicId(String topic_id) {
		this.topic_id = topic_id;
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
		return created_at;
	}
	
	public void setTimeCreate(Date created_at) {
		this.created_at = created_at;
	}
	
	public Date getTimeUpdate() {
		return updated_at;
	}
	
	public void setTimeUpdate(Date updated_at) {
		this.updated_at = updated_at;
	}
	
	public String getAccessLevelId() {
		return access_level_id;
	}
	
	public void setAccessLevelId(String access_level_id) {
		this.access_level_id = access_level_id;
	}
	
	public String getNumberAllowComment() {
		return allow_comment;
	}
	
	public void setNumberAllowComment(String allow_comment) {
		this.allow_comment = allow_comment;
	}
}
