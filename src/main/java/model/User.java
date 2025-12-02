package model;

public class User {
	protected int id;
	protected String username;
	protected String password;
	protected String fullname;
	protected String email;
	protected int age;

	public User() {
	}

	public User(String username, String password, String fullname, String email, int age) {
		super();
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.email = email;
		this.age = age;
	}
	
	public User(int id, String username, String password, String fullname, String email, int age) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.email = email;
		this.age = age;
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
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
}
