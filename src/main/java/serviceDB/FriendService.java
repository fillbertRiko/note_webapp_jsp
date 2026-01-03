package serviceDB;

import java.util.ArrayList;
import java.util.List;

import DAO.FriendshipDAO;
import DAO.UserDao;
import model.User;

public class FriendService {
	private FriendshipDAO friendshipDao = new FriendshipDAO();
	private UserDao userDao = new UserDao();

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
}
