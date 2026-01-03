package serviceDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DAO.FriendInviteDAO;
import DAO.FriendshipDAO;
import DAO.UserDao;
import model.FriendInvites;
import model.Friendship;
import model.User;

///tang service ho tro lay du lieu 
///gui loi moi ket ban
///chap nhan loi moi ket ban
///tu choi/huy loi moi
///lay danh sach loi moi da gui va da nhan
public class FriendInviteService {
	private FriendInviteDAO friendInviteDao = new FriendInviteDAO();
	private UserDao userDao = new UserDao();
	private FriendshipDAO friendshipDao= new FriendshipDAO();
	
	public boolean sendInvite(String senderId, String receiverId) {
		if(friendshipDao.checkFriendship(senderId, receiverId)) return false;
		
		FriendInvites invites = new FriendInvites();
		invites.setReceiverId(receiverId);
		invites.setSenderId(senderId);
		invites.setStatus("PENDING");
		invites.setTimeSent(new Date());
		
		friendInviteDao.createInvitation(invites);
		return true;
	}
	
	public boolean acceptInvite(String inviteId) {
		FriendInvites invites = friendInviteDao.findInvitesById(inviteId);
		if(invites == null) return false;
		
		Friendship newFriendship = new Friendship();
		newFriendship.setUserId1(invites.getSenderId());
		newFriendship.setUserId2(invites.getReceiverId());
		newFriendship.setTimeCreate(new Date());
		friendshipDao.createRelationship(newFriendship);
		
		return friendInviteDao.deleteInvitation(inviteId);
	}
	
	public boolean deleteInvite(String inviteId) {
		return friendInviteDao.deleteInvitation(inviteId);
	}
	
	public List<FriendInvites> getReceiverInvites(String currentUserId){
		List<FriendInvites> list = new ArrayList<>();
		List<FriendInvites> invites = friendInviteDao.findInvitationForUser(currentUserId);
		
		for(FriendInvites inv : invites) {
			User sender = userDao.findUserById(currentUserId);
			if(sender != null) {
				sender.setPassword(null);
				inv.setUserDetails(sender);
				list.add(inv);
			}
		}
		return list;
	}
	
	public List<FriendInvites> getSentInvites(String currentUserId){
		List<FriendInvites> list = new ArrayList<>();
		List<FriendInvites> sent = friendInviteDao.findInvitationForUser(currentUserId);
		
		for(FriendInvites inv : sent) {
			if("PENDING".equals(inv.getStatus())) {
                User receiver = userDao.findUserById(inv.getReceiverId());
                if (receiver != null) {
                    receiver.setPassword(null);
                    list.add((FriendInvites) sent);
                }
            }
		}
		return list;
	}
}
