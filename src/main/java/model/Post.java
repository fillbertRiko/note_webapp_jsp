package model;

import java.sql.Date;

public class Post {
	private int id;
	private int user_id;
	private int topic_id;
	private String title;
	private String content;
	private Date created_at;
	private Date updated_at;
	private int access_level_id;
	private int allow_comment;

	public Post() {
		
	}

	public Post(int id, int user_id, int topic_id, String title, String content, Date created_at, Date updated_at, int access_level_id, int allow_comment) {
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
	
	public int getTopicId() {
		return topic_id;
	}
	
	public void setTopicId(int topic_id) {
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
	
	public int getAccessLevelId() {
		return access_level_id;
	}
	
	public void setAccessLevelId(int access_level_id) {
		this.access_level_id = access_level_id;
	}
	
	public int getNumberAllowComment() {
		return allow_comment;
	}
	
	public void setNumberAllowComment(int allow_comment) {
		this.allow_comment = allow_comment;
	}
}
