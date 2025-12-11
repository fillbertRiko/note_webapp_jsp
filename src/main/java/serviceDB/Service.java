package serviceDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

///tao ket noi toi csdl ma khong can goi lai qua cac DAO
///tai day se test ket noi truc tiep toi db
///
public class Service {
	public static void main(String[] args) throws Exception {
		String url = "jdbc:mysql://localhost:3306/webapp_note_servlet";
		String username = "root";
		String password = "Riko1103";
		String query = "SELECT * FROM webapp_note_servlet";
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		
		Connection conn = DriverManager.getConnection(url, username, password);
		System.out.println("Connection good");
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(query);
		
		while(rs.next()) {
			String name = rs.getString("name");
			System.out.println(name);
		}
		
		st.close();
		conn.close();
		System.out.println("Connection close... ");
	}
}
