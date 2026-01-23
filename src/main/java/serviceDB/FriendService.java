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

    public List<User> getFriendOfUser(String userId) {
        List<User> friends = new ArrayList<>();
        List<String> friendIds = friendshipDao.getListFriendIds(userId);
        
        for(String id : friendIds) {
            User u = userDao.findUserById(id); 
            if(u != null) {
                u.setPassword(null);
                friends.add(u);
            }
        }
        return friends;
    }
    
    public boolean unfriend(String currentUserId, String friendId) {
        return friendshipDao.deleteSpecificFriendship(currentUserId, friendId);
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
                return "RECEIVED_REQUEST";
            }
        }
        
        return "STRANGER"; 
    }
    
    public List<User> getSuggestedFriends(String currentUserId) {
        List<User> potentialUsers = userDao.getPotentialUsers(currentUserId);
        List<String> currentFriendIds = friendshipDao.getListFriendIds(currentUserId);
        FriendInviteDAO inviteDAO = new FriendInviteDAO(); 
        List<FriendInvites> sent = inviteDAO.findInvitationBySenderId(currentUserId);
        List<FriendInvites> received = inviteDAO.findInvitationForUser(currentUserId);
        
        List<String> pendingIds = new ArrayList<>();
        for(FriendInvites i : sent) pendingIds.add(i.getReceiverId()); 
        for(FriendInvites i : received) pendingIds.add(i.getSenderId());

        List<User> suggestions = new ArrayList<>();
        for (User u : potentialUsers) {
            String uid = u.getId();
            if (!currentFriendIds.contains(uid) && !pendingIds.contains(uid)) {
                suggestions.add(u);
            }
        }
        
        return suggestions;
    }
}