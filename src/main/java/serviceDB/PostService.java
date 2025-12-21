package serviceDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DAO.FriendshipDAO;
import DAO.PostDAO;
import model.Post;
import model.User;

///Service chuan bi cho truoc khi day du lieu vao DAO
///De xu ly loc khi tim keim bai viet se duoc xu ly duoi tang DAO
///
public class PostService {
	private PostDAO postDao = new PostDAO();
	private FriendshipDAO friendshipDao = new FriendshipDAO();
	
	///CRUD
	///Update
	public boolean editPost(String postId, String currentUserId, Post updatedInfo) {
		Post exitingPost = postDao.findPostByUserId(postId, currentUserId);
		if(exitingPost == null)	return false;
		
		exitingPost.setTopicId(updatedInfo.getTopicId());
		exitingPost.setTitle(updatedInfo.getTitle());
		exitingPost.setContent(updatedInfo.getContent());
		exitingPost.setAccessLevelId(updatedInfo.getAccessLevelId());
		exitingPost.setNumberAllowComment(updatedInfo.getNumberAllowComment());
		
		return postDao.updatePost(exitingPost);
	}
	
	///Read
	public List<Post> getOwnPost(String currentUserId, int page){
		return postDao.findMyPost(currentUserId, page);
	}
	
	public List<Post> getPostByVisitor(String ownerId, String viewerId, int page){
		if(ownerId.equals(viewerId)) {
			return getOwnPost(ownerId, page);
		}
		
		List<String> allowedLevels = new ArrayList<>();
		allowedLevels.add("PUBLIC");
		
		if(friendshipDao.checkFriendship(ownerId, viewerId)) {
			allowedLevels.add("PROTECTED_2");
		}
		
		return postDao.findPostsVisibleToUser(ownerId, allowedLevels, page, viewerId);
	}
	
	///Create
	public boolean create(String title, String content, String topicId, User currentUser, String accessLevelId, List<String> allowViewer, String allowCommentStatus) {
		if("PROTECTED_1".equals(accessLevelId)) {
			if(allowViewer == null || allowViewer.isEmpty()) {
				System.err.println("Erroe: If you choose choose friend only, you must choose at least once person");
				return false;
			}
		} else {
			allowViewer = new ArrayList<>();
		}
		
		Post post = new Post();
		
		post.setTitle(title);
		post.setContent(content);
		post.setTopicId(topicId);
		post.setUserId(currentUser.getId());
		Date now = new Date();
		post.setTimeCreate(now);
		post.setTimeUpdate(now);
		post.setAccessLevelId(accessLevelId);
		post.setAllowViewer(allowViewer);
		post.setNumberAllowComment(allowCommentStatus);
		
		return postDao.createPost(post);
	}
	
	///Delete
	public boolean deleteMyPost(String postId, String currentUserId) {
		Post post = postDao.findPostByUserId(postId, currentUserId);
		if(post == null) {
			System.err.println("Post doesn't exits!");
			return false;
		}
		boolean isDeleted = postDao.deletePost(postId, currentUserId);
		
		if(isDeleted){
			return true;
		} else {
			System.err.println("Warning: User " + currentUserId + " trying delete another post");
			return false;
		}
	}
}
