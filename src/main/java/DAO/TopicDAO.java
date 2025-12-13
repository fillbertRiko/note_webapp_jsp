package DAO;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import model.Topic;

///Lop DAO giup giao tiep voi tang du lieu
///khai bao 1 lan 
///co ham chuyen doi tu Document sang POJO phu hop voi MongoDB
///tai day se goi cac cau lenh truy van trong db
///tao cac cau truy van CRUD cho User
///check user account bang username
public class TopicDAO {
	
	private final MongoCollection<Document> topics;
	
	public TopicDAO() {
		MongoDatabase db = DBConnection.getDatabase();
		this.topics = db.getCollection("topics");
	}
	
	private Topic documentToTopic(Document doc) {
		if(doc == null) {
			return null;
		}
		
		Topic topic = new Topic(
				doc.getObjectId("_id").toHexString(),
				doc.getString("userId"),
				doc.getString("name"),
				doc.getString("description"),
				doc.getDate("createdAt"));
		return topic;
	}

	public Topic findTopicById(String id){
		Document query = new Document("_id", new ObjectId(id));
		Document topicDoc = topics.find(query).first();
		
		if(topicDoc == null) {
			return null;
		}
		
		return documentToTopic(topicDoc);
	}
	

	public List<Topic> readAllTopics(){
		List<Topic> topicsList = new ArrayList<>();
		try (MongoCursor<Document> cursor = topics.find().iterator()) {
			while(cursor.hasNext()) {
				Document topicDoc = cursor.next();
				topicsList.add(documentToTopic(topicDoc));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} 
		return topicsList;
	}

	public boolean updateTopic(Topic topic){
		if(topic.getId() == null) {
			System.err.println("Error: Cannot update Topic, lost ID.");
			return false;
		}
		
		try {
			Document filter = new Document("_id", new ObjectId(topic.getId()));
			Document updateFields = new Document("$set", new Document()
					.append("userId", topic.getUserId())
					.append("name", topic.getName())
					.append("description", topic.getDescription())
					.append("createdAt", topic.getCreatedAt()));
			
			UpdateResult result = topics.updateOne(filter, updateFields);
			return result.getModifiedCount() > 0;
		} catch (Exception e) {
			System.err.println("Error when update topic have ID: " + topic.getId());
			e.printStackTrace();
			return false;
		}
	}

	public void createTopic(Topic topic) {
		Document topicDoc = new Document("_id", new ObjectId())
				.append("userId", topic.getUserId())
				.append("name", topic.getName())
				.append("description", topic.getDescription())
				.append("createdAt", topic.getCreatedAt());
	
		try {
			topics.insertOne(topicDoc);
			System.out.println("Topic created with ID: " + topicDoc.getObjectId("_id"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean deleteTopic(String id){
		try{
			Document filter = new Document("_id", new ObjectId(id));
			DeleteResult result = topics.deleteOne(filter);
			return result.getDeletedCount() == 1;
		} catch (IllegalArgumentException e) {
			System.err.println("Error: ID topic unexcepted" + id);
			return false;
		} catch (Exception e) {
			System.err.println("Error when delete Topic by ID: " + id);
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteTopicsByUserId(String userId) {
		try {
			Document filter = new Document("userId", new ObjectId(userId));
			DeleteResult result = topics.deleteMany(filter);
			return result.getDeletedCount() >0;
		} catch (IllegalArgumentException e) {
			System.err.println("Error: ID user not found" + userId);
			return false;
		} catch(Exception e) {
			System.err.println("Error when delete Post by UserID: " + userId);
			e.printStackTrace();
			return false;
		}
	}
	
	public Topic findTopicByName(String name) {
		Document query = new Document("name", name);
		Document topicDoc = topics.find(query).first();
		
		return documentToTopic(topicDoc);
	}
}
