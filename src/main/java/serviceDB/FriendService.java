package serviceDB;

import java.util.ArrayList;
import java.util.List;

import DAO.FriendInviteDAO;
import DAO.FriendshipDAO;
import DAO.UserDao;
import model.FriendInvites;
import model.User;

public class FriendService {
    private FriendshipDAO friendshipDao = new FriendshipDAO();
    private UserDao userDao = new UserDao();
    private FriendInviteDAO friendInviteDao = new FriendInviteDAO();

    // Lấy danh sách bạn bè (kèm thông tin user)
    public List<User> getFriendOfUser(String userId) {
        List<User> friends = new ArrayList<>();
        List<String> friendIds = friendshipDao.getListFriendIds(userId);
        
        for(String id : friendIds) {
            User u = userDao.findUserById(id); 
            if(u != null) {
                u.setPassword(null); // Bảo mật
                friends.add(u);
            }
        }
        return friends;
    }
    
    // --- [MỚI] THỰC HIỆN HỦY KẾT BẠN ---
    public boolean unfriend(String currentUserId, String friendId) {
        return friendshipDao.deleteSpecificFriendship(currentUserId, friendId);
    }
    
    // --- [QUAN TRỌNG] KIỂM TRA QUAN HỆ ĐỂ HIỂN THỊ UI ---
    public String checkRelationship(String currentUserId, String targetUserId) {
        if(currentUserId.equals(targetUserId)) {
            return "SELF"; // Chính mình
        }
        
        if(friendshipDao.checkFriendship(currentUserId, targetUserId)) {
            return "FRIEND"; // Đã là bạn bè
        }
        
        // Kiểm tra xem mình có gửi lời mời cho họ không
        List<FriendInvites> sentList = friendInviteDao.findInvitationBySenderId(currentUserId);
        for(FriendInvites inv : sentList) {
            if(inv.getReceiverId().equals(targetUserId) && "PENDING".equals(inv.getStatus())) {
                return "SENT_REQUEST"; // Đã gửi lời mời
            }
        }
        
        // Kiểm tra xem họ có gửi lời mời cho mình không
        List<FriendInvites> receiverList = friendInviteDao.findInvitationForUser(currentUserId);
        for(FriendInvites inv : receiverList) {
            if(inv.getSenderId().equals(targetUserId)) {
                return "RECEIVED_REQUEST"; // Có lời mời đang chờ
            }
        }
        
        return "STRANGER"; // Người lạ
    }
}