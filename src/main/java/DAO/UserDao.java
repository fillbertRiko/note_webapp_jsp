package DAO;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import model.User;

///Lop DAO giup giao tiep voi tang du lieu
///khai bao 1 lan
///co ham chuyen doi tu Document sang POJO phu hop voi MongoDB
///tai day se goi cac cau lenh truy van trong db
///tao cac cau truy van CRUD cho User
///check user account bang username
public class UserDao {

	private final MongoCollection<Document> users;

	public UserDao() {
		MongoDatabase db = DBConnection.getDatabase();
		this.users = db.getCollection("user");
	}

	private User documentToUser(Document doc) {
		if (doc == null) {
			return null;
		}

		User user = new User(doc.getObjectId("_id").toHexString(), doc.getString("username"), doc.getString("password"),
				doc.getString("fullname"), doc.getString("email"), doc.getDate("createdAt"));
//		System.out.println(doc.getString("password"));
		return user;
	}

	public User findUserById(String id) {
		try {
			Document doc = users.find(Filters.eq("_id", new ObjectId(id))).first();
			if (doc != null) {
				User user = new User();
				user.setId(doc.getObjectId("_id").toString());
				user.setFullname(doc.getString("fullname"));
				user.setEmail(doc.getString("email"));
				user.setUsername(doc.getString("username"));
				user.setCreatedAt(doc.getDate("createdAt"));
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<User> readAllUsers() {
		List<User> usersList = new ArrayList<>();
		try (MongoCursor<Document> cursor = users.find().iterator()) {
			while (cursor.hasNext()) {
				Document userDoc = cursor.next();
				usersList.add(documentToUser(userDoc));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usersList;
	}

	public boolean updateUser(User user) {
		if (user.getId() == null) {
			System.err.println("Error: Cannot update User, lost ID.");
			return false;
		}

		try {
			Document filter = new Document("_id", new ObjectId(user.getId()));
			Document updateFields = new Document("$set", new Document().append("username", user.getUsername())
					.append("fullname", user.getFullname()).append("email", user.getEmail()));

			UpdateResult result = users.updateOne(filter, updateFields);
			return result.getModifiedCount() > 0;
		} catch (Exception e) {
			System.err.println("Error when update user have ID: " + user.getId());
			e.printStackTrace();
			return false;
		}
	}

	public User createUser(User user) {
		Document userDoc = new Document("_id", new ObjectId()).append("username", user.getUsername())
				.append("password", user.getPassword()).append("fullname", user.getFullname())
				.append("email", user.getEmail()).append("createdAt", user.getCreatedAt());

		try {
			users.insertOne(userDoc);
			user.setId(userDoc.getObjectId("_id").toHexString());
			System.out.println("User created with ID: " + userDoc.getObjectId("_id"));
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean deleteUser(String id) {
		try {
			Document filter = new Document("_id", new ObjectId(id));
			DeleteResult result = users.deleteOne(filter);
			return result.getDeletedCount() == 1;
		} catch (IllegalArgumentException e) {
			System.err.println("Error: ID user unexcepted" + id);
			return false;
		} catch (Exception e) {
			System.err.println("Error when delete User by ID: " + id);
			e.printStackTrace();
			return false;
		}
	}

	public User findUserByUsername(String username) {
		Document query = new Document("username", username);
		Document userDoc = users.find(query).first();

		if (userDoc == null) {
			System.out.println("DEBUG: Cann't found user: " + username);
			return null;
		}

		System.out.println(userDoc.toString());

		return documentToUser(userDoc);
	}

	public User findUserByUsernameOrEmail(String username, String email) {
		Bson filter = Filters.or(Filters.eq("username", username), Filters.eq("email", email));
		Document userDoc = users.find(filter).first();

		return documentToUser(userDoc);
	}

	public boolean updatePassword(String username, String hashedNewPassword) {
		try {
			Document filter = new Document("username", username);
			Document updateFields = new Document("$set", new Document("password", hashedNewPassword));
			UpdateResult result = users.updateOne(filter, updateFields);
			return result.getModifiedCount() > 0;
		} catch (Exception e) {
			System.err.println("Error when update password for user: " + username);
			e.printStackTrace();
			return false;
		}
	}
}
