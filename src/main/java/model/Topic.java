package model;

import java.sql.Date;

///Class topic de them chieu voi model danh muc
///tao getter va setter 
///cac ham ket noi voi model thong qua goi ham
///nen luu id theo dang chuoi de tranh viec xung dot view/controller vi dang su dung mongodb
public class Topic {
	protected String id;
	protected String user_id;
	protected String name;
	protected String description;
	protected Date created_at;
	
	public Topic() {
		
	}
	
	public Topic(String id, String user_id, String name, String description, Date created_at) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.name = name;
		this.description = description;
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
	
	public Date getTimeCreate() {
		return created_at;
	}
	
	public void setTimeCreate(Date created_at) {
		this.created_at = created_at;
	}
}
