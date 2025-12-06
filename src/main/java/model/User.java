package model;

import java.sql.Date;


///Class the hien model nguoi dung 
///tao cac getter va setter
///lam viec truc tiep voi DB de lay thong tin ve cho controller
///mapping lai du lieu de gui cho DB 
public class User {
	protected int id;
	protected String username;
	protected String password;
	protected String fullname;
	protected String email;
	protected String hobbies;
	protected Date created_at;

	public User() {
	}

	public User(String username, String password, String fullname, String email, Date created_at) {
		super();
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.email = email;
		this.created_at = created_at;
	}
	
	public User(int id, String username, String password, String fullname, String email, Date created_at) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.email = email;
		this.created_at = created_at;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
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
		return created_at;
	}
	
	public void setTimeCreate(Date created_at) {
		this.created_at = created_at;
	}
}
