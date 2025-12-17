package DAO;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


///lop nay chiu trach nhiem duy nhat la cung cap doi tuong MongoDatabase
///cau hinh ket noi
///phuong thuc khoi tao va tra ve doi duong MongoDatabase
///phuong thuc donog ket noi de tranh bi hieu ung bottleneck dong du lieu den server
public class DBConnection {
	private static final String CONNECTION_STRING = "mongodb://localhost:27017";
	private static final String DATABASE_NAME = "note_webapp_jsp";
	private static MongoClient mongoClient = null;
	
	public static void initConnection() {
		if(mongoClient == null) {
			try {
				mongoClient = MongoClients.create(CONNECTION_STRING);
				System.out.println(">> MongoDB: connect successfull");
			} catch (Exception e) {
				throw new RuntimeException("DB Connection init fail. ", e);
			}
		}
	}
	
	public static MongoDatabase getDatabase() {
		if(mongoClient == null ) {
			synchronized (DBConnection.class) {
				if(mongoClient == null) {
					initConnection();
				}
			}
		}
		return mongoClient.getDatabase(DATABASE_NAME);
	}
	
	public static void closeConnection() {
		if(mongoClient != null) {
			mongoClient.close();
			mongoClient = null;
			System.out.println("Connection close successful");
		}
	}
}
