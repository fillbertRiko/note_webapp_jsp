package serviceDB;

import java.util.ArrayList;
import java.util.List;

import DAO.FriendInviteDAO;
import DAO.FriendshipDAO;
import DAO.PostDAO;
import DAO.ScheduleDao;
import DAO.TopicDAO;
import DAO.UserDao;
import model.User;

public class UserService {
    private final UserDao userDao = new UserDao();
    private final PostDAO postDao = new PostDAO();
    private final TopicDAO topicDao = new TopicDAO();
    private final ScheduleDao scheduleDao = new ScheduleDao();
    private final FriendInviteDAO inviteDao = new FriendInviteDAO();
    private final FriendshipDAO friendshipDao = new FriendshipDAO();

    public User login(String username, String password) {
        User user = userDao.findUserByUsername(username);
        if (user != null) {
            String hashedInputPassword = SHA256Hasher.hash(password);
            String storedHasherPassword = user.getPassword();

            if (hashedInputPassword.equals(storedHasherPassword)) {
                return user;
            }
        }
        return null;
    }

    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return userDao.searchUsers(keyword);
    }

    public User register(User newUser) {
        User exitingUser = userDao.findUserByUsernameOrEmail(newUser.getUsername(), newUser.getEmail());
        if (exitingUser != null) {
            return null;
        }
        String hashPassword = SHA256Hasher.hash(newUser.getPassword());
        newUser.setPassword(hashPassword);
        return userDao.createUser(newUser);
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

    public boolean updatePass(String username, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }
        String hashedNewPassword = SHA256Hasher.hash(newPassword);
        User user = userDao.findUserByUsername(username);
        if (user != null) {
            return userDao.updatePassword(username, hashedNewPassword);
        }
        return false;
    }

    public User showInformation(String id) {
        if (id == null || id.isEmpty()) return null;
        return userDao.findUserById(id);
    }

    public boolean updateUser(User user) {
        return userDao.updateUser(user);
    }
}