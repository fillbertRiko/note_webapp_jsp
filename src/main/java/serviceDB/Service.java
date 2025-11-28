package serviceDB;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Service {
	private String jdbcURL = "";
	protected Connection getConnection() throws ClassNotFoundException {
		Connection conn = null;
		try {
			Class.forName("");
			conn = DriverManager.getConnection(jbdcURL)
		} catch(SQLException e) {
			
		}
	}
}
