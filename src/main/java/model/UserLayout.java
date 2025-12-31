package model;

import java.util.Date;

// Class thể hiện cấu hình giao diện trang chủ của người dùng.
public class UserLayout {

	private String id;
	private String userId;
	private String homepageConfig;
	private Date updatedAt;

	public UserLayout() {
	}

	public UserLayout(String id, String userId, String homepageConfig, Date updatedAt) {
		super();
		this.id = id;
		this.userId = userId;
		this.homepageConfig = homepageConfig;
		this.updatedAt = updatedAt;
	}

	public UserLayout(String userId, String homepageConfig) {
		this.userId = userId;
		this.homepageConfig = homepageConfig;
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

	public String getHomepageConfig() {
		return homepageConfig;
	}

	public void setHomepageConfig(String homepageConfig) {
		this.homepageConfig = homepageConfig;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}