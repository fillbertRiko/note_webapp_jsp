package DAO;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import model.Friendship;

// Lớp này chịu trách nhiệm làm việc trực tiếp với bảng "friendships" trong MongoDB
public class FriendshipDAO {

    private final MongoCollection<Document> friendships;

    public FriendshipDAO() {
        // Kết nối đến database và lấy collection (bảng) tên là "friendships"
        MongoDatabase db = DBConnection.getDatabase();
        this.friendships = db.getCollection("friendships");
    }

    // Hàm phụ trợ: Chuyển đổi từ dữ liệu thô (Document) của MongoDB sang Object Java (Friendship)
    private Friendship documentToFriendship(Document doc) {
        if (doc == null) return null;
        return new Friendship(doc.getObjectId("_id").toHexString(), doc.getString("userId1"),
                doc.getString("userId2"), doc.getDate("createdAt"));
    }

    // Kiểm tra xem 2 người đã là bạn chưa (Quan trọng: Kiểm tra 2 chiều)
    public boolean checkFriendship(String userId1, String userId2) {
        // Tạo bộ lọc tìm kiếm theo logic HOẶC (OR):
        // Trường hợp 1: userId1 là A và userId2 là B
        // Trường hợp 2: userId1 là B và userId2 là A
        // (Vì trong DB không quy định ai đứng trước, nên phải check cả 2 chiều)
        Bson filter = Filters.or(
                Filters.and(Filters.eq("userId1", userId1), Filters.eq("userId2", userId2)),
                Filters.and(Filters.eq("userId1", userId2), Filters.eq("userId2", userId1)));
        
        // Nếu tìm thấy số lượng bản ghi > 0 nghĩa là họ đã là bạn bè
        return friendships.countDocuments(filter) > 0;
    }
    
    // Lấy danh sách ID tất cả bạn bè của một User
    public List<String> getListFriendIds(String userId){
        List<String> friendIds = new ArrayList<>();
        
        // Tìm tất cả các bản ghi mà userId này có tham gia (dù đứng ở cột userId1 hay userId2)
        Bson filter = Filters.or(Filters.eq("userId1", userId), Filters.eq("userId2", userId));
        
        try (MongoCursor<Document> cursor = friendships.find(filter).iterator()){
            while(cursor.hasNext()) {
                Document doc = cursor.next();
                String uId1 = doc.getString("userId1");
                String uId2 = doc.getString("userId2");
                
                // Logic lấy ID người kia:
                // Nếu mình là uId1 -> Bạn là uId2
                // Nếu mình là uId2 -> Bạn là uId1
                if(uId1.equals(userId)) {
                    friendIds.add(uId2);
                } else {
                    friendIds.add(uId1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return friendIds;
    }

    // Tạo tình bạn mới (Lưu vào DB khi chấp nhận kết bạn)
    public void createRelationship(Friendship friendship) {
        // Tạo một Document mới chứa thông tin 2 người và thời gian kết bạn
        Document friendshipDoc = new Document("_id", new ObjectId())
                .append("userId1", friendship.getUserId1())
                .append("userId2", friendship.getUserId2())
                .append("createdAt", friendship.getTimeCreate());
        try {
            // Chèn vào database
            friendships.insertOne(friendshipDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- [MỚI] HÀM XÓA BẠN (UNFRIEND) ---
    // Xóa mối quan hệ cụ thể giữa 2 người
    public boolean deleteSpecificFriendship(String user1, String user2) {
        try {
            // Tìm bản ghi tình bạn bất kể chiều nào (A-B hoặc B-A)
            // Phải dùng OR vì không biết lúc lưu ai là userId1, ai là userId2
            Bson filter = Filters.or(
                Filters.and(Filters.eq("userId1", user1), Filters.eq("userId2", user2)),
                Filters.and(Filters.eq("userId1", user2), Filters.eq("userId2", user1))
            );
            
            // Thực hiện xóa 1 dòng tìm thấy
            DeleteResult result = friendships.deleteOne(filter);
            
            // Trả về true nếu xóa thành công (số dòng bị xóa > 0)
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa tất cả bạn bè (Dùng khi người dùng xóa tài khoản vĩnh viễn)
    public boolean deleteRelationshipByUserId(String userId) {
        try {
            // Tìm tất cả bản ghi có dính dáng đến userId này và xóa hết
            Bson filter = Filters.or(Filters.eq("userId1", userId), Filters.eq("userId2", userId));
            friendships.deleteMany(filter);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}