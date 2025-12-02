package serviceDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

///tao ket noi toi csdl ma khong can goi lai qua cac DAO
///tai day se test ket noi truc tiep toi db
///
public class Service {
	private static String JDBC_URL ="";
	private static String  JDBC_USER = "root";
	private static String JDBC_PASSWORD = "Riko1103";
	
	public static Connection getConnection(){
		Connection connection = null;
		try {
			Class.forName("com.mongoDB.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
