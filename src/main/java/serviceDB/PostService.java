package serviceDB;

import java.util.Date;

import DAO.PostDAO;
import model.Post;
import model.User;

public class PostService {
	private PostDAO postDao = new PostDAO();
	
	public boolean create(String title, String content, String topicId, User currentUser) {
		Post post = new Post();
		
		post.setTitle(title);
		post.setContent(content);
		post.setTopicId(topicId);
		post.setUserId(currentUser.getId());
		Date now = new Date();
		post.setTimeCreate(now);
		post.setTimeUpdate(now);
		post.setAccessLevelId("PUBLIC");
		post.setNumberAllowComment("1");
		
		return postDao.createPost(post);
	}
	
	public boolean delete(String postId, String currentUserId) {
		Post post = postDao.findPostByUserId(postId);
		if(post == null) {
			System.err.println("Post doesn;t exits!");
			return false;
		}
		
		if(post.getUserId().equals(currentUserId)) {
			return postDao.deletePost(postId, currentUserId);
		} else {
			System.err.println("Warning: User " + currentUserId + " trying delete another post");
			return false;
		}
	}
}
