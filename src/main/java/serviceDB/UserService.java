package serviceDB;

import DAO.FriendInviteDAO;
import DAO.FriendshipDAO;
import DAO.PostDAO;
import DAO.ScheduleDao;
import DAO.TopicDAO;
import DAO.UserDao;
import model.User;


///Service check user login into system
///check password not hash, just checking by HTML placeholder
///need to change check password and encrypt to Bcrypt pass code
public class UserService {
	private final UserDao userDao = new UserDao();
	private final PostDAO postDao = new PostDAO();
	private final TopicDAO topicDao = new TopicDAO();
	private final ScheduleDao scheduleDao = new ScheduleDao();
	private final FriendInviteDAO inviteDao = new FriendInviteDAO();
	private final FriendshipDAO friendshipDao = new FriendshipDAO();
	
	public User login(String username, String password) {
		User user = userDao.findUserByUsername(username);
		
		if(user != null) {
			String hashedInputPassword = SHA256Hasher.hash(password);
			String storedHasherPassword = user.getPassword();
//			System.out.println(hashedInputPassword.toString());
			
			if(hashedInputPassword.equals(storedHasherPassword)) {
				System.out.println("Login successfully: " + username);
				return user;
			} else {
				System.out.println("Wrong password");
				return null;
			}
		}
		return null;
	}
	
	public boolean deleteUser(String userId) {
		try {
			postDao.deletePostsByUserId(userId);
			topicDao.deleteTopicsByUserId(userId);
			scheduleDao.deleteSchedulesByUserId(userId);
			inviteDao.deleteFriendInviteByUserId(userId);
			friendshipDao.deleteRelationshipByUserId(userId);
			userDao.deleteUser(userId);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public User register(User newUser) {
		User exitingUser = userDao.findUserByUsernameOrEmail(newUser.getUsername(), newUser.getEmail());
		if(exitingUser != null) {
			return null;
		}
		
		String hashPassword = SHA256Hasher.hash(newUser.getPassword());
		newUser.setPassword(hashPassword);
		
		return userDao.createUser(newUser);
	}
	
	public boolean updatePass(String username, String newPassword) {
		if(newPassword == null || newPassword.trim().isEmpty()) {
			return false;
		}
		
		String hashedNewPassword = SHA256Hasher.hash(newPassword);
		User user = userDao.findUserByUsername(username);
		System.out.println(username.toString());
		if(user!=null) {
//			user.setPassword(hashedNewPassword);
			return userDao.updatePassword(username, hashedNewPassword);
		}
		return false;
	}
}
