package DAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import model.Post;

///Tao Document truy xuat va truy van nguoc ve DB
///Tao muc CRUD de lam viec voi du lieu bai viet (Post)
///hien tai bai viet duoc bao ve thong qua role nen can tao cac ham tim kiem co dieu kien de loc ra, tranh bi chong cheo
///muc update se duoc update theo id va loc de tranh viec user khasc cung co the update duoc
///muc delete se chi duoc xoa boi chinh nguoi dung da tao ra bai viet
public class PostDAO {
	private final MongoCollection<Document> posts;
	
	public PostDAO() {
		MongoDatabase db = DBConnection.getDatabase();
		this.posts = db.getCollection("post");
	}
	
	private Post documentToPost(Document doc) {
		if(doc == null) return null;
		
		return new Post(
				doc.getObjectId("_id").toHexString(),
				doc.getString("userId"),
				doc.getString("topicId"),
				doc.getString("title"),
				doc.getString("content"),
				doc.getDate("createdAt"),
				doc.getDate("updatedAt"),
				doc.getString("accessLevelId"),
				doc.getString("allowComment"), 
				doc.getDate("month"),
				doc.getDate("year"));
	}
	
	/*CURD*/
	///Create
	public boolean createPost(Post post) {
		ObjectId newId = new ObjectId();
		Document postDoc = new Document("_id", newId)
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
			post.setId(newId.toHexString());
			System.out.println("Post created with user ID: " + post.getUserId() + "\n And post have title: " + post.getTitle());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error when create post: " + e.getMessage());
			return false;
		}
	}
	
	///Read
	public Post findPostByUserId(String postId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Post> findByMonth(String userId, int month, int year) {
		List<Post> list = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.set(month -1,1,0,0,0, year);
		Date startDate = cal.getTime();
		
		cal.add(Calendar.MONTH, 1);
		Date endDate = cal.getTime();
		
		Bson filter = Filters.and(
				Filters.eq("userId", userId),
				Filters.gte("createdAt", startDate),
				Filters.lt("createdAt", endDate));
		
		try (MongoCursor<Document> cursor = posts.find(filter).iterator()) {
			while(cursor.hasNext()) {
				Document doc = cursor.next();
				
				Post postObject = documentToPost(doc);
				list.add(postObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Cannot find UserID: " + userId);
		}
		return list;
	}
	
	public List<Post> findPostsVisibleTouser(String ownerId, List<String> allowAccessLevels, String viewerId){
		List<Post> list = new ArrayList<>();
		
		Bson filter = Filters.and(
				Filters.eq("userId", ownerId),
				Filters.or(
						Filters.and(
								Filters.in("accessLevelId", allowAccessLevels),
								Filters.ne("accessLevelId", "PROTECTED_1")
								),
						Filters.and(
								Filters.in("accessLevelId", "PROTECTED_1"),
								Filters.ne("accessLevelId", viewerId)
								)
						)
				);
		try(MongoCursor<Document> cursor = posts.find(filter)
				.sort(new Document("createdAt", -1))
				.iterator()) {
			while(cursor.hasNext()) {
				Document doc = cursor.next();
				list.add(documentToPost(doc));
			}
		} catch (Exception e) {
			System.err.println("Have error when take post " + e.getMessage());
			e.printStackTrace();
		}
		
		return list;
	}
	
	///Update
	public boolean updatePost(Post post) {
		if(post.getId() == null) {
			System.err.println("Error: Cannot update Post, cannot find ID.");
			return false;
		}
		
		try {
			Bson filter = Filters.and(
					Filters.eq("_id", new ObjectId(post.getId())),
					Filters.eq("userId", post.getUserId()));
			Document updateFields = new Document("$set", new Document())
					.append("title", post.getTitle())
					.append("content", post.getContent())
					.append("updatedAt", new Date())
					.append("accessLevelId", post.getAccessLevelId())
					.append("allowComment", post.getNumberAllowComment());
			
			UpdateResult result = posts.updateOne(filter, updateFields);
			return result.getModifiedCount() > 0;
		} catch(Exception e) {
			System.err.println("Error when update post by user ID: " + post.getUserId());
			e.printStackTrace();
			return false;
		}
	}
	
	///Delete
	public boolean deletePost(String postId, String currentUserId) {
		try {
			Bson filter = Filters.and(
					Filters.eq("_id", new ObjectId(postId)),
					Filters.eq("userId", currentUserId));
			DeleteResult result = posts.deleteOne(filter);
			return result.getDeletedCount() == 1;
		} catch (IllegalArgumentException e) {
			System.err.println("Error: ID post unexcepted" + postId);
			return false;
		} catch(Exception e) {
			System.err.println("Error when delete Post by ID: " + postId);
			e.printStackTrace();
			return false;
		}
	}

	public void deletePostsByUserId(String userId) {
		// TODO Auto-generated method stub
		
	}

}
