package DAO;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DBConnectionListener implements ServletContextListener {
	
	public void contextInit(ServletContextEvent sce) {
		System.out.println("Starting DB Connection init...");
		try {
			DBConnection.initConnection();
			System.out.println("MongoDB Connection successfully");
		} catch (RuntimeException e) {
			System.err.println("FATAL ERROR: Couldn't connect to MongoDB");
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		DBConnection.closeConnection();
		System.out.println("MongoDB Connection close");
	}
}
