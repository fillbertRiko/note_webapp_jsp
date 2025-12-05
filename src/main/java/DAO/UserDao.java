package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import serviceDB.Service;
import model.User;

///Lop DAO giup giao tiep voi tang du lieu
///tai day se goi cac cau lenh truy van trong db
///tao cac cau truy van CRUD cho User
///check user account
public class UserDao {
	final Connection conn = Service.getConnection();
	final Statement stmt = conn.createStatement();

	private static final String INSERT_USER_SQL = "INSERT INTO user"
			+ "(username, fullname, password, email, age)VALUES" + "(?, ?, ?, ?, ?);";
	private static final String SELECT_USER_BY_ID = "SELECT id,username,fullname,password,email,age FROM user WHERE id =?";
	private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
	private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";

	final PreparedStatement rsRead = conn.prepareStatement(SELECT_USER_BY_ID);
	final PreparedStatement cstmt = conn.prepareStatement(INSERT_USER_SQL);
	final PreparedStatement ustmt = conn.prepareStatement(UPDATE_USERS_SQL);
	final PreparedStatement dstmt = conn.prepareStatement(DELETE_USERS_SQL);

	public UserDao() throws SQLException {
		try (conn; stmt; rsRead) {

		} catch (SQLException e) {
			e.printStackTrace();
		}
	};

	public User readUser(int id) throws SQLException {
		User user = null;
		try (conn; stmt; rsRead) {
			rsRead.setInt(1, id);
			System.out.println(rsRead);
			ResultSet rs = rsRead.executeQuery();
			while (rs.next()) {
				user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
						rs.getString("fullname"), rs.getString("email"), rs.getDate("created_at"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public List<User> readAllUsers(){
		List<User> users = new ArrayList<>();
		try (conn;rsRead) {
			String SELECT_ALL_USERS = "SELECT * FROM user";
			PreparedStatement ps = conn.prepareStatement(SELECT_ALL_USERS);
			System.out.println(ps);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
						rs.getString("fullname"), rs.getString("email"), rs.getDate("created_at"));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated;
		try (conn; ustmt) {
			ustmt.setString(1, user.getFullname());
			ustmt.setString(2, user.getUsername());
			ustmt.setString(3, user.getEmail());
			ustmt.setString(4, user.getPassword());
			ustmt.setDate(5, user.getTimeCreate());
			rowUpdated = ustmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return rowUpdated;
	}

	public void createUser(User user) throws SQLException {
		System.out.println(INSERT_USER_SQL);
		try (conn; cstmt) {
			cstmt.setString(1, user.getUsername());
			cstmt.setString(2, user.getFullname());
			cstmt.setString(3, user.getPassword());
			cstmt.setString(4, user.getEmail());
			cstmt.setDate(5, user.getTimeCreate());
			cstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (conn; dstmt) {
			dstmt.setInt(1, id);
			rowDeleted = dstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return rowDeleted;
	}

	public User getUserByUsernameAndPassword(String username, String password) {
		User user = null;
		String SELECT_USER_BY_USERNAME_AND_PASSWORD = "SELECT * FROM user WHERE username = ? AND password = ?";
		try (conn) {
			PreparedStatement ps = conn.prepareStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD);
			ps.setString(1, username);
			ps.setString(2, password);
			System.out.println(ps);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
						rs.getString("fullname"), rs.getString("email"), rs.getDate("created_at"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
}
