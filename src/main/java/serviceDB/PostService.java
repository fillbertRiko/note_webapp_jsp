package serviceDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DAO.FriendshipDAO;
import DAO.PostDAO;
import model.Post;
import model.User;

public class PostService {
    private PostDAO postDao = new PostDAO();
    private FriendshipDAO friendshipDao = new FriendshipDAO();

    // Sửa bài viết
    public boolean editPost(String postId, String currentUserId, Post updatedInfo) {
        Post exitingPost = postDao.findPostByUserId(postId, currentUserId);
        if (exitingPost == null) {
            return false;
        }

        exitingPost.setTopicId(updatedInfo.getTopicId());
        exitingPost.setTitle(updatedInfo.getTitle());
        exitingPost.setContent(updatedInfo.getContent());
        exitingPost.setAccessLevelId(updatedInfo.getAccessLevelId());
        exitingPost.setAllowViewer(updatedInfo.getAllowViewer());
        exitingPost.setNumberAllowComment(updatedInfo.getNumberAllowComment());

        return postDao.updatePost(exitingPost);
    }

    // --- [QUAN TRỌNG] LOGIC LẤY BÀI VIẾT CHO KHÁCH XEM ---
    public List<Post> getPostByVisitor(String ownerId, String viewerId, int page, String keyword) {
        List<String> allowedLevels = new ArrayList<>();
        
        // 1. PUBLIC: Mặc định ai cũng thấy
        allowedLevels.add("PUBLIC");

        // 2. Kiểm tra quan hệ
        if (ownerId.equals(viewerId)) {
            // Nếu xem tường nhà mình -> Thấy hết
            allowedLevels.add("PRIVATE");
            allowedLevels.add("PROTECTED_1");
            allowedLevels.add("PROTECTED_2");
        } else {
            // Nếu là khách -> Kiểm tra xem có phải bạn bè không
            if (friendshipDao.checkFriendship(ownerId, viewerId)) {
                // Nếu là bạn -> Được phép xem bài dành cho bạn bè
                allowedLevels.add("PROTECTED_2"); 
            }
            // Lưu ý: Bài PROTECTED_1 (Chỉ định cụ thể) sẽ được PostDAO xử lý riêng 
            // bằng cách kiểm tra viewerId có nằm trong list allowViewer không.
        }

        return postDao.findPostsVisibleToUser(ownerId, allowedLevels, page, viewerId, keyword);
    }

    // Tạo bài viết
    public boolean create(String title, String content, String topicId, User currentUser, String accessLevelId,
            List<String> allowViewer, String allowCommentStatus) {
        
        if ("PROTECTED_1".equals(accessLevelId)) {
            if (allowViewer == null || allowViewer.isEmpty()) {
                return false; // Bắt buộc chọn người xem nếu là chế độ chỉ định
            }
        } else {
            if (allowViewer == null) allowViewer = new ArrayList<>();
        }

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setTopicId(topicId);
        post.setUserId(currentUser.getId());
        post.setTimeCreate(new Date());
        post.setTimeUpdate(new Date());
        post.setAccessLevelId(accessLevelId);
        post.setAllowViewer(allowViewer);
        post.setNumberAllowComment(allowCommentStatus);

        return postDao.createPost(post);
    }

    // Xóa bài viết
    public boolean deleteMyPost(String postId, String currentUserId) {
        Post post = postDao.findPostByUserId(postId, currentUserId);
        if (post == null) {
            return false;
        }
        return postDao.deletePost(postId, currentUserId);
    }
}