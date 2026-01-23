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

public class ScheduleDao {

    private final MongoCollection<Document> schedules;

    public ScheduleDao() {
        MongoDatabase db = DBConnection.getDatabase();
        this.schedules = db.getCollection("workSchedule");
    }

    private WorkSchedule documentToSchedule(Document doc) {
        if (doc == null) return null;
        
        return new WorkSchedule(
            doc.getObjectId("_id").toHexString(),
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

    public WorkSchedule findScheduleById(String id) {
        try {
            Document query = new Document("_id", new ObjectId(id));
            Document scheduleDoc = schedules.find(query).first();
            return documentToSchedule(scheduleDoc);
        } catch (Exception e) {
            return null;
        }
    }

    public List<WorkSchedule> readAllSchedules() {
        List<WorkSchedule> schedulesList = new ArrayList<>();
        try (MongoCursor<Document> cursor = schedules.find().iterator()) {
            while (cursor.hasNext()) {
                Document scheduleDoc = cursor.next();
                WorkSchedule s = documentToSchedule(scheduleDoc);
                if(s != null) schedulesList.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedulesList;
    }

    public boolean updateSchedule(WorkSchedule schedule) {
        if (schedule.getId() == null) return false;

        try {
            Document filter = new Document("_id", new ObjectId(schedule.getId()));
            Document updateFields = new Document("$set",
                    new Document()
                            .append("subject", schedule.getSubject())
                            .append("description", schedule.getDescription())
                            .append("startTime", schedule.getStartTime())
                            .append("endTime", schedule.getEndTime())
                            .append("prioroty", schedule.getPriority())
                            .append("location", schedule.getLocation()));
            UpdateResult result = schedules.updateOne(filter, updateFields);
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createSchedule(WorkSchedule schedule) {
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
            schedules.insertOne(scheduleDoc);
            System.out.println("Schedule created: " + schedule.getSubject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean deleteSchedule(String id) {
        try {
            Document filter = new Document("_id", new ObjectId(id));
            DeleteResult result = schedules.deleteOne(filter);
            return result.getDeletedCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSchedulesByUserId(String userId) {
        try {
            Document filter = new Document("userId", userId);
            DeleteResult result = schedules.deleteMany(filter);
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}