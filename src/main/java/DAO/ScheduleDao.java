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

import model.WorkSchedule;

// Lớp này chịu trách nhiệm thao tác trực tiếp với bảng "workSchedule" trong MongoDB
public class ScheduleDao {

    private final MongoCollection<Document> schedules;

    public ScheduleDao() {
        // Kết nối Database và lấy bảng (collection) workSchedule
        MongoDatabase db = DBConnection.getDatabase();
        this.schedules = db.getCollection("workSchedule");
    }

    // Hàm phụ trợ: Chuyển đổi dữ liệu từ MongoDB (Document) sang Object Java (WorkSchedule)
    private WorkSchedule documentToSchedule(Document doc) {
        if (doc == null) return null;
        
        return new WorkSchedule(
            doc.getObjectId("_id").toHexString(), // Chuyển ObjectId sang String
            doc.getString("userId"),
            doc.getString("subject"), 
            doc.getString("description"), 
            doc.getDate("startTime"),
            doc.getDate("endTime"), 
            doc.getString("prioroty"),
            doc.getString("location"), 
            doc.getDate("createdAt")
        );
    }

    // Tìm kiếm lịch trình theo ID (Dùng khi bấm nút Sửa để load dữ liệu cũ)
    public WorkSchedule findScheduleById(String id) {
        try {
            // Tạo bộ lọc theo _id
            Document query = new Document("_id", new ObjectId(id));
            // Lấy kết quả đầu tiên tìm thấy
            Document scheduleDoc = schedules.find(query).first();
            return documentToSchedule(scheduleDoc);
        } catch (Exception e) {
            return null;
        }
    }

    // Lấy toàn bộ danh sách lịch trình
    public List<WorkSchedule> readAllSchedules() {
        List<WorkSchedule> schedulesList = new ArrayList<>();
        try (MongoCursor<Document> cursor = schedules.find().iterator()) {
            while (cursor.hasNext()) {
                Document scheduleDoc = cursor.next();
                // Chuyển đổi từng dòng dữ liệu và thêm vào danh sách
                WorkSchedule s = documentToSchedule(scheduleDoc);
                if(s != null) schedulesList.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedulesList;
    }

    // Cập nhật thông tin lịch trình
    public boolean updateSchedule(WorkSchedule schedule) {
        if (schedule.getId() == null) return false;

        try {
            // 1. Tìm bản ghi cần sửa bằng ID
            Document filter = new Document("_id", new ObjectId(schedule.getId()));
            
            // 2. Định nghĩa các trường cần thay đổi bằng toán tử "$set"
            Document updateFields = new Document("$set",
                    new Document()
                            .append("subject", schedule.getSubject())
                            .append("description", schedule.getDescription())
                            .append("startTime", schedule.getStartTime())
                            .append("endTime", schedule.getEndTime())
                            .append("prioroty", schedule.getPriority())
                            .append("location", schedule.getLocation()));
            
            // 3. Thực hiện update
            UpdateResult result = schedules.updateOne(filter, updateFields);
            return result.getModifiedCount() > 0; // Trả về true nếu có dòng dữ liệu bị thay đổi
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thêm mới lịch trình
    public void createSchedule(WorkSchedule schedule) {
        // Tạo đối tượng Document chứa dữ liệu để insert
        // Lưu ý: Không dùng "$set" ở đây vì đây là lệnh INSERT, không phải UPDATE
        Document scheduleDoc = new Document()
                .append("userId", schedule.getUserId())
                .append("subject", schedule.getSubject())
                .append("description", schedule.getDescription())
                .append("startTime", schedule.getStartTime())
                .append("endTime", schedule.getEndTime())
                .append("prioroty", schedule.getPriority())
                .append("location", schedule.getLocation())
                .append("createdAt", schedule.getCreatedAt());

        try {
            // Chèn vào Database
            schedules.insertOne(scheduleDoc);
            System.out.println("Schedule created: " + schedule.getSubject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xóa một lịch trình cụ thể theo ID
    public boolean deleteSchedule(String id) {
        try {
            Document filter = new Document("_id", new ObjectId(id));
            DeleteResult result = schedules.deleteOne(filter);
            return result.getDeletedCount() == 1; // Trả về true nếu xóa thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa tất cả lịch trình của một người dùng (Dùng khi xóa tài khoản User)
    public boolean deleteSchedulesByUserId(String userId) {
        try {
            // Tìm và xóa tất cả bản ghi có userId tương ứng
            Document filter = new Document("userId", userId);
            DeleteResult result = schedules.deleteMany(filter);
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}