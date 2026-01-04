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
	FriendInviteDAO friendInviteDao = new FriendInviteDAO(); 

	public List<User> getFriendOfUser(String userId) {
		// TODO Auto-generated method stub
		List<User> friends = new ArrayList<>();
		List<String> friendIds = friendshipDao.getListFriendIds(userId);
		
		for(String id : friendIds) {
			User u = userDao.findUserById(userId);
			if(u != null) friends.add(u);
		}
		return friends;
	}
	
	public boolean unfriend(String currentUserId, String friendId) {
		return true;
	}
	
	public String checkRelationship(String currentUserId, String targetUserId) {
		if(currentUserId.equals(targetUserId)) {
			return "SELF";
		}
		
		if(friendshipDao.checkFriendship(currentUserId, targetUserId)) {
			return "FRIEND";
		}
		
		List<FriendInvites> sentList = friendInviteDao.findInvitationBySenderId(currentUserId);
		for(FriendInvites inv : sentList) {
			if(inv.getReceiverId().equals(targetUserId) && "PENDING".equals(inv.getStatus())) {
				return "SENT_REQUEST";
			}
		}
		
		List<FriendInvites> receiverList = friendInviteDao.findInvitationForUser(currentUserId);
		for(FriendInvites inv : receiverList) {
			if(inv.getSenderId().equals(targetUserId)) {
				return "RECEIVER_REQUEST";
			}
		}
		
		return "STRANGER";
	}
}
