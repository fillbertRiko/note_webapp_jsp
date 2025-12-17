package model;

import java.util.Date;


///Class the hien model nguoi dung 
///tao cac getter va setter
///lam viec truc tiep voi DB de lay thong tin ve cho controller
///Chi chua du lieu. Lop DAO tuong tac voi DB
///tao 2 contructor mot cho day du va 1 cho dang ky
public class User {
	private String id;
	private String username;
	private String password;
	private String fullname;
	private String email;
	private Date createdAt;

	public User() {}
	
	public User(String id, String username, String password, String fullname, String email, Date createdAt) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.email = email;
		this.createdAt = createdAt;
	}
	
	public User(String username, String password, String fullname, String email) {
		this.username =username;
		this.password = password;
		this.fullname = fullname;
		this.email = email;
		this.createdAt = new Date();
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getFullname() {
		return fullname;
	}
	
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getTimeCreate() {
		return createdAt;
	}
	
	public void setTimeCreate(Date createdAt) {
		this.createdAt = createdAt;
	}
}
