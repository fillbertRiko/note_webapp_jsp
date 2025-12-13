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

import model.Post;

public class PostDAO {
	private final MongoCollection<Document> posts;
	
	public PostDAO() {
		MongoDatabase db = DBConnection.getDatabase();
		this.posts = db.getCollection("post");
	}
	
	private Post documentToPost(Document doc) {
		if(doc == null) return null;
		
		Post post = new Post(
				doc.getObjectId("_id").toHexString(),
				doc.getString("userId"),
				doc.getString("topicId"),
				doc.getString("title"),
				doc.getString("content"),
				doc.getDate("createdAt"),
				doc.getDate("updatedAt"),
				doc.getString("accessLevelId"),
				doc.getString("allowComment"));
		return post;
	}
	
	public Post findPostById(String id) {
		Document query = new Document("_id", new ObjectId(id));
		Document postDoc = posts.find(query).first();
		
		if(postDoc == null) return null;
		
		return documentToPost(postDoc);
	}
	
	public List<Post> readAllPost() {
		List<Post> postList = new ArrayList<>();
		try (MongoCursor<Document> cursor = posts.find().iterator()) {
			while(cursor.hasNext()) {
				Document postDoc = cursor.next();
				postList.add(documentToPost(postDoc));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postList;
	}
	
	public boolean updatePost(Post post) {
		if(post.getId() == null) {
			System.err.println("Error: Cannot update Post, cannot find ID.");
			return false;
		}
		
		try {
			Document filter = new Document("_id", new ObjectId(post.getId()));
			Document updateFields = new Document("$set", new Document())
					.append("title", post.getTitle())
					.append("content", post.getContent())
					.append("allowCommend", post.getNumberAllowComment());
			
			UpdateResult result = posts.updateOne(filter, updateFields);
			return result.getModifiedCount() > 0;
		} catch(Exception e) {
			System.err.println("Error when update post by user ID: " + post.getUserId());
			e.printStackTrace();
			return false;
		}
	}
	
	public void createPost(Post post) {
		Document postDoc = new Document("_id", new ObjectId())
				.append("userId", post.getUserId())
				.append("topicId", post.getTopicId())
				.append("title", post.getTitle())
				.append("content", post.getContent())
				.append("createdAt", post.getTimeCreate())
				.append("updatedAt", post.getTimeUpdate())
				.append("accessLevelId", post.getAccessLevelId())
				.append("allowComment", post.getNumberAllowComment());
		try {
			posts.insertOne(postDoc);
			System.out.println("Post created with user ID: " + post.getUserId() + "\n And post have title: " + post.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean deletePost(String id) {
		try {
			Document filter = new Document("_id", new ObjectId(id));
			DeleteResult result = posts.deleteOne(filter);
			return result.getDeletedCount() == 1;
		} catch (IllegalArgumentException e) {
			System.err.println("Error: ID post unexcepted" + id);
			return false;
		} catch(Exception e) {
			System.err.println("Error when delete Post by ID: " + id);
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deletePostsByUserId(String userId) {
		try {
			Document filter = new Document("userId", new ObjectId(userId));
			DeleteResult result = posts.deleteMany(filter);
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
	
	public Post findPostByTitle(String title) {
		Document query = new Document("title", title);
		Document postDoc = posts.find(query).first();
		
		return documentToPost(postDoc);
	}
	
	public Post findPostByTopic(String topicId) {
		Document query = new Document("topicId", topicId);
		Document postDoc = posts.find(query).first();
		
		return documentToPost(postDoc);
	}
	
	public Post findPostByUserId(String userId) {
		Document query = new Document("userId", userId);
		Document postDoc = posts.find(query).first();
		
		return documentToPost(postDoc);
	}
	
	public Post findPostByContent(String content) {
		Document query = new Document("content", content);
		Document postDoc = posts.find(query).first();
		
		return documentToPost(postDoc);
	}
}
