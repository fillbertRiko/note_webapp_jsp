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

import model.FriendInvites;

///Lop DAO giup giao tiep voi tang du lieu
///khai bao 1 lan
///co ham chuyen doi tu Document sang POJO phu hop voi MongoDB
///tai day se goi cac cau lenh truy van trong db
///tao cac cau truy van CRUD cho Friendship
/// check relationship bang ID
public class FriendInviteDAO {

	private final MongoCollection<Document> friendInvites;

	public FriendInviteDAO() {
		MongoDatabase db = DBConnection.getDatabase();
		this.friendInvites = db.getCollection("friendInvites");
	}

	private FriendInvites documentToFriendInvite(Document doc) {
		if (doc == null) {
			return null;
		}

		FriendInvites friendInvite = new FriendInvites(
				doc.getObjectId("_id").toHexString(), 
				doc.getString("senderId"),
				doc.getString("receiverId"), 
				doc.getString("status"), 
				doc.getDate("createdAt"));
		return friendInvite;
	}

	///CRUD
	///Read
	public FriendInvites findInvitesById(String id) {
		Document query = new Document("_id", new ObjectId(id));
		Document friendInviteDoc = friendInvites.find(query).first();

		if (friendInviteDoc == null) {
			return null;
		}

		return documentToFriendInvite(friendInviteDoc);
	}

	public List<FriendInvites> checkStatus(String status) {
		List<FriendInvites> friendInviteStatus = new ArrayList<>();
		Bson filter = Filters.eq("status", status);

		try (MongoCursor<Document> cursor = friendInvites.find(filter).iterator()) {
			while (cursor.hasNext()) {
				Document friendInviteDoc = cursor.next();
				friendInviteStatus.add(documentToFriendInvite(friendInviteDoc));
			}
		} catch (Exception e) {
			System.err.println("Error, can't find status");
			e.printStackTrace();
		}

		return friendInviteStatus;
	}

	public List<FriendInvites> readAllInvites() {
		List<FriendInvites> friendInvitesList = new ArrayList<>();
		try (MongoCursor<Document> cursor = friendInvites.find().iterator()) {
			while (cursor.hasNext()) {
				Document friendInviteDoc = cursor.next();
				friendInvitesList.add(documentToFriendInvite(friendInviteDoc));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return friendInvitesList;
	}
	
	public List<FriendInvites> findInvitationBySenderId(String senderId) {
		List<FriendInvites> list = new ArrayList<>();
		Document query = new Document("senderId", senderId);
		try(MongoCursor<Document> cursor = friendInvites.find(query).iterator()){
			while(cursor.hasNext()) {
				list.add(documentToFriendInvite(cursor.next()));
			}
		}

		return list;
	}

	public List<FriendInvites> findInvitationForUser(String receiverId) {
		List<FriendInvites> list = new ArrayList<>();
		Bson filter = Filters.and(
				Filters.eq("receiverId", receiverId),
				Filters.eq("status", "PENDING"));
		
		try(MongoCursor<Document> cursor = friendInvites.find(filter).iterator()){
			while(cursor.hasNext()) {
				list.add(documentToFriendInvite(cursor.next()));
			}
		}
		
		return list;
	}

	///Update
	public boolean updateInvite(FriendInvites friendInvite) {
		if (friendInvite.getId() == null) {
			System.err.println("Error: Cannot update Invitation, lost ID.");
			return false;
		}

		try {
			Document filter = new Document("_id", new ObjectId(friendInvite.getId()));
			Document updateFields = new Document("$set",
					new Document()
							.append("senderId", friendInvite.getSenderId())
							.append("receiverId", friendInvite.getReceiverId())
							.append("status", friendInvite.getStatus())
							.append("createdAt", friendInvite.getTimeSent()));

			UpdateResult result = friendInvites.updateOne(filter, updateFields);
			return result.getModifiedCount() > 0;
		} catch (Exception e) {
			System.err.println("Error when update invite have ID: " + friendInvite.getId());
			e.printStackTrace();
			return false;
		}
	}

	///Create
	public void createInvitation(FriendInvites friendInvite) {
		Document friendInviteDoc = new Document("_id", new ObjectId())
											.append("senderId", friendInvite.getSenderId())
											.append("receiverId", friendInvite.getReceiverId())
											.append("status", friendInvite.getStatus())
											.append("createdAt", friendInvite.getTimeSent());

		try {
			friendInvites.insertOne(friendInviteDoc);
			System.out.println("Invitation created with ID: " + friendInviteDoc.getObjectId("_id"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	///Delete
	public boolean deleteInvitation(String id) {
		try {
			Document filter = new Document("_id", new ObjectId(id));
			DeleteResult result = friendInvites.deleteOne(filter);
			return result.getDeletedCount() == 1;
		} catch (IllegalArgumentException e) {
			System.err.println("Error: Invitation ID unexcepted" + id);
			return false;
		} catch (Exception e) {
			System.err.println("Error when delete invitation by ID: " + id);
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteFriendInviteByUserId(String userId) {
		try {
			Bson filter = Filters.or(Filters.eq("senderId", userId), Filters.eq("receiverId", userId));
			DeleteResult result = friendInvites.deleteMany(filter);

			// debug
			System.out.println(result.toString());

			return true;
		} catch (IllegalArgumentException e) {
			System.err.println("Error: ID user not accepted: " + userId);
			return false;
		} catch (Exception e) {
			System.err.println("Error when delete FriendInvites: " + userId);
			e.printStackTrace();
			return false;
		}
	}
}
