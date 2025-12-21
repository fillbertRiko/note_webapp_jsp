package DAO;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import model.Friendship;

///Lop DAO giup giao tiep voi tang du lieu
///khai bao 1 lan 
///co ham chuyen doi tu Document sang POJO phu hop voi MongoDB
///tai day se goi cac cau lenh truy van trong db
///tao cac cau truy van CRUD cho Friendship
/// check relationship bang ID
public class FriendshipDAO {
	
	private final MongoCollection<Document> friendships;
	
	public FriendshipDAO() {
		MongoDatabase db = DBConnection.getDatabase();
		this.friendships = db.getCollection("friendships");
	}
	
	private Friendship documentToFriendship(Document doc) {
		if(doc == null) {
			return null;
		}
		
		Friendship friendship = new Friendship(
				doc.getObjectId("_id").toHexString(),
				doc.getString("userId"),
				doc.getString("friendId"),
				doc.getDate("createdAt"));
		return friendship;
	}

	///CURD
	///Read
	public boolean checkFriendship(String userId1, String userId2) {
		Bson filter = Filters.or(
				Filters.and(
						Filters.eq("userId1", userId1),
						Filters.eq("userId2", userId2)),
				Filters.and(
						Filters.eq("userId1", userId2),
						Filters.eq("userId2", userId1)));
		return friendships.countDocuments(filter) >0;
	}

	public boolean updateFriendship(Friendship friendship){
		if(friendship.getId() == null) {
			System.err.println("Error: Cannot update Relationship, lost ID.");
			return false;
		}
		
		try {
			Document filter = new Document("_id", new ObjectId(friendship.getId()));
			Document updateFields = new Document("$set", new Document()
					.append("userId", friendship.getUserId())
					.append("friendId", friendship.getFriendId())
					.append("createdAt", friendship.getTimeCreate()));
			
			UpdateResult result = friendships.updateOne(filter, updateFields);
			return result.getModifiedCount() > 0;
		} catch (Exception e) {
			System.err.println("Error when update relationship have ID: " + friendship.getId());
			e.printStackTrace();
			return false;
		}
	}

	public void createRelationship(Friendship friendship) {
		Document friendshipDoc = new Document("_id", new ObjectId())
				.append("userId", friendship.getUserId())
				.append("friendId", friendship.getFriendId())
				.append("created_at", friendship.getTimeCreate());
	
		try {
			friendships.insertOne(friendshipDoc);
			System.out.println("User created with ID: " + friendshipDoc.getObjectId("_id"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean deleteRelationship(String id){
		try{
			Document filter = new Document("_id", new ObjectId(id));
			DeleteResult result = friendships.deleteOne(filter);
			return result.getDeletedCount() == 1;
		} catch (IllegalArgumentException e) {
			System.err.println("Error: Friend ID unexcepted" + id);
			return false;
		} catch (Exception e) {
			System.err.println("Error when delete relationship by ID: " + id);
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteRelationshipByUserId(String userId) {
		try {
			ObjectId userObjectId = new ObjectId(userId);
			
			Bson filter = Filters.or(
					Filters.eq("userId1", userObjectId),
					Filters.eq("userId2", userObjectId));
			
			DeleteResult result = friendships.deleteMany(filter);
			
			//debug
			System.out.println(result.toString());
			
			return true;
		} catch (IllegalArgumentException e) {
			System.err.println("Error: ID user not excepted: " + userId);
			return false;
		} catch (Exception e) {
			System.err.println("Error when delete Friendship: " + userId);
			e.printStackTrace();
			return false;
		}
	}
	
	public Friendship findRelationshipByUserId(String userId) {
		Document query = new Document("userId", userId);
		Document friendshipDoc = friendships.find(query).first();
		
		return documentToFriendship(friendshipDoc);
	}
}
