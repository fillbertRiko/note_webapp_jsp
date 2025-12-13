package serviceDB;

import DAO.UserDao;
import model.User;


///Service check user login into system
///check password not hash, just checking by HTML placeholder
///need to change check password and encrypt to Bcrypt pass code
public class UserService {
	private final UserDao userDao = new UserDao();
	public User login(String username, String password) {
		User user = userDao.findUserByUsername(username);
		
		if(user != null && checkPasswordPlaceholder(password, user.getPassword())) {
			return user;
		}
		
		return null;
	}
	private boolean checkPasswordPlaceholder(String password, String storedHash) {
		return password.equals(storedHash);
	}
	
	
}
