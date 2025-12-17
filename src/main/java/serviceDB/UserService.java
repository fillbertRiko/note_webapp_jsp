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
		
		if(user != null ) {
			String hashedInputPassword = SHA256Hasher.hash(password);
			String storedHasherPassword = user.getPassword();
			System.out.println(hashedInputPassword.toString());
			
			if(hashedInputPassword.equals(storedHasherPassword)) {
				System.out.println("Login successfully: " + username);
				return user;
			} else {
				System.out.println("Wrong password");
			}
			return user;
		} else {
			System.out.println("Cannot found user");
		}
		return null;
	}
	
	public boolean deleteUser(String userId) {
		postDao.deletePostsByUserId(userId);
		topicDao.deleteTopicsByUserId(userId);
		scheduleDao.deleteSchedulesByUserId(userId);
		inviteDao.deleteFriendInviteByUserId(userId);
		friendshipDao.deleteRelationshipByUserId(userId);
		userDao.deleteUser(userId);
		
		return true;
	}
	
	public User register(User newUser) {
		String userAccount = newUser.getUsername();
		String emailAccount = newUser.getEmail();
		String rawPassword = newUser.getPassword();
		String hashPassword = SHA256Hasher.hash(rawPassword);
		newUser.setPassword(hashPassword);
		
		User existingUser = userDao.findUserByUsernameOrEmail(userAccount, emailAccount);
		
		if(existingUser != null) {
			return null;
		}
		
		return userDao.createUser(newUser);
	}
}
