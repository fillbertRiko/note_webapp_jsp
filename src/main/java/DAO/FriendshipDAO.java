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
import model.Friendship;

public class FriendshipDAO {

    private final MongoCollection<Document> friendships;

    public FriendshipDAO() {
        MongoDatabase db = DBConnection.getDatabase();
        this.friendships = db.getCollection("friendships");
    }

    private Friendship documentToFriendship(Document doc) {
        if (doc == null) return null;
        return new Friendship(doc.getObjectId("_id").toHexString(), doc.getString("userId1"),
                doc.getString("userId2"), doc.getDate("createdAt"));
    }

    public boolean checkFriendship(String userId1, String userId2) {
        Bson filter = Filters.or(
                Filters.and(Filters.eq("userId1", userId1), Filters.eq("userId2", userId2)),
                Filters.and(Filters.eq("userId1", userId2), Filters.eq("userId2", userId1)));
        return friendships.countDocuments(filter) > 0;
    }
    
    public List<String> getListFriendIds(String userId){
        List<String> friendIds = new ArrayList<>();
        
        Bson filter = Filters.or(Filters.eq("userId1", userId), Filters.eq("userId2", userId));
        
        try (MongoCursor<Document> cursor = friendships.find(filter).iterator()){
            while(cursor.hasNext()) {
                Document doc = cursor.next();
                String uId1 = doc.getString("userId1");
                String uId2 = doc.getString("userId2");
                if(uId1.equals(userId)) {
                    friendIds.add(uId2);
                } else {
                    friendIds.add(uId1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friendIds;
    }

    public void createRelationship(Friendship friendship) {
        Document friendshipDoc = new Document("_id", new ObjectId())
                .append("userId1", friendship.getUserId1())
                .append("userId2", friendship.getUserId2())
                .append("createdAt", friendship.getTimeCreate());
        try {
            friendships.insertOne(friendshipDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteSpecificFriendship(String user1, String user2) {
        try {
            Bson filter = Filters.or(
                Filters.and(Filters.eq("userId1", user1), Filters.eq("userId2", user2)),
                Filters.and(Filters.eq("userId1", user2), Filters.eq("userId2", user1))
            );
            
            DeleteResult result = friendships.deleteOne(filter);
            
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRelationshipByUserId(String userId) {
        try {
            Bson filter = Filters.or(Filters.eq("userId1", userId), Filters.eq("userId2", userId));
            friendships.deleteMany(filter);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}