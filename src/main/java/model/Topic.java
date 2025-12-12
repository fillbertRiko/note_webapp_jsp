package model;

import java.util.Date;

///Class topic de them chieu voi model danh muc
///tao getter va setter 
///cac ham ket noi voi model thong qua goi ham
///nen luu id theo dang chuoi de tranh viec xung dot view/controller vi dang su dung mongodb
public class Topic {
	private String id;
	private String userId;
	private String name;
	private String description;
	private Date createdAt;
	
	public Topic() {
	}
	
	public Topic(String id, String userId, String name, String description, Date createdAt) {
		super();
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
	}
	
	public Topic(String userId, String name, String description) {
		this.userId = userId;
		this.name = name;
		this.description = description;
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
	
	public String getName()  {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
    
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}