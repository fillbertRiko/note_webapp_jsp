package DAO;

///Lop DAO giup giao tiep voi tang du lieu
///tai day se goi cac cau lenh truy van trong db
///tao cac cau truy van CRUD cho User
public class UserDao {
	private String jdbcURL = "";
	private String jdbcUsername = "root";
	private String jdbcPassword = "";
	
	private static final String INSERT_USER_SQL = "INSERT INTO user" + "(username, fullname, password, email, age)VALUES"+"(?, ?, ?, ?, ?, ?);";
	private static final String SELECT_USER_BY_ID = "SELECT id,username,fullname,password,email,age FROM user WHERE id =?";
	private static final String DELETE_USERS_SQL = "delete from users where id = ?;";   
	private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";
	
	public UserDao() {};
}
