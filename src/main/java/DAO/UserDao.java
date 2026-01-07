package DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import model.User;

public class UserDao {

    private final MongoCollection<Document> users;

    public UserDao() {
        MongoDatabase db = DBConnection.getDatabase();
        this.users = db.getCollection("user"); // Tên collection gốc của bạn là "user"
    }

    private User documentToUser(Document doc) {
        if (doc == null) {
            return null;
        }
        return new User(
            doc.getObjectId("_id").toHexString(),
            doc.getString("username"),
            doc.getString("password"),
            doc.getString("fullname"),
            doc.getString("email"),
            doc.getDate("createdAt")
        );
    }

    // --- CÁC HÀM CƠ BẢN ---

    public User findUserById(String id) {
        try {
            if (id == null || id.isEmpty()) return null;
            Document doc = users.find(Filters.eq("_id", new ObjectId(id))).first();
            if (doc != null) {
                return documentToUser(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> readAllUsers() {
        List<User> usersList = new ArrayList<>();
        try (MongoCursor<Document> cursor = users.find().iterator()) {
            while (cursor.hasNext()) {
                usersList.add(documentToUser(cursor.next()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersList;
    }

    public boolean updateUser(User user) {
        if (user.getId() == null) {
            return false;
        }
        try {
            Document filter = new Document("_id", new ObjectId(user.getId()));
            Document updateFields = new Document("$set", new Document()
                    .append("username", user.getUsername())
                    .append("fullname", user.getFullname())
                    .append("email", user.getEmail()));
            
            // Chỉ update password nếu có thay đổi
            if(user.getPassword() != null && !user.getPassword().isEmpty()){
                ((Document)updateFields.get("$set")).append("password", user.getPassword());
            }

            UpdateResult result = users.updateOne(filter, updateFields);
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User createUser(User user) {
        Document userDoc = new Document("_id", new ObjectId())
                .append("username", user.getUsername())
                .append("password", user.getPassword())
                .append("fullname", user.getFullname())
                .append("email", user.getEmail())
                .append("createdAt", user.getCreatedAt());

        try {
            users.insertOne(userDoc);
            user.setId(userDoc.getObjectId("_id").toHexString());
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteUser(String id) {
        try {
            DeleteResult result = users.deleteOne(new Document("_id", new ObjectId(id)));
            return result.getDeletedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User findUserByUsername(String username) {
        Document doc = users.find(new Document("username", username)).first();
        return documentToUser(doc);
    }

    public User findUserByUsernameOrEmail(String username, String email) {
        Bson filter = Filters.or(Filters.eq("username", username), Filters.eq("email", email));
        Document doc = users.find(filter).first();
        return documentToUser(doc);
    }

    public boolean updatePassword(String username, String hashedNewPassword) {
        try {
            UpdateResult result = users.updateOne(new Document("username", username),
                    new Document("$set", new Document("password", hashedNewPassword)));
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // --- [MỚI] HÀM TÌM KIẾM NGƯỜI DÙNG (Dùng cho chức năng Tìm bạn) ---
    public List<User> searchUsers(String keyword) {
        List<User> userList = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return userList;
        }

        try {
            // Regex tìm gần đúng, không phân biệt hoa thường (Case Insensitive)
            Pattern pattern = Pattern.compile(".*" + Pattern.quote(keyword) + ".*", Pattern.CASE_INSENSITIVE);
            
            Bson filter = Filters.or(
                Filters.regex("username", pattern),
                Filters.regex("fullname", pattern),
                Filters.regex("email", pattern)
            );

            // Giới hạn 20 kết quả để tối ưu hiệu năng
            try (MongoCursor<Document> cursor = users.find(filter).limit(20).iterator()) { 
                while (cursor.hasNext()) {
                    User u = documentToUser(cursor.next());
                    u.setPassword(null); // Xóa mật khẩu để bảo mật khi hiển thị ra view
                    userList.add(u);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }
}