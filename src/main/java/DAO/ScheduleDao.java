package DAO;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import model.WorkSchedule;

///Lop DAO giup giao tiep voi tang du lieu
///khai bao 1 lan 
///co ham chuyen doi tu Document sang POJO phu hop voi MongoDB
///tai day se goi cac cau lenh truy van trong db
///tao cac cau truy van CRUD cho Schedule
///check user account bang username
public class ScheduleDao {
	
	private final MongoCollection<Document> schedules;
	
	public ScheduleDao() {
		MongoDatabase db = DBConnection.getDatabase();
		this.schedules = db.getCollection("wokSchedule");
	}
	
	private WorkSchedule documentToSchedule(Document doc) {
		if(doc == null) {
			return null;
		}
		
		WorkSchedule schedule = new WorkSchedule(
				doc.getObjectId("_id").toHexString(),
				doc.getString("userId"),
				doc.getString("subject"),
				doc.getString("description"),
				doc.getDate("startTime"),
				doc.getDate("endTime"),
				doc.getString("prioroty"),
				doc.getString("location"),
				doc.getDate("createdAt"));
		return schedule;
	}

	public WorkSchedule findScheduleById(String id){
		Document query = new Document("_id", new ObjectId(id));
		Document scheduleDoc = schedules.find(query).first();
		
		if(scheduleDoc == null) {
			return null;
		}
		
		return documentToSchedule(scheduleDoc);
	}
	
	public WorkSchedule findScheduleByUserId(String userId){
		Document query = new Document("userId", new ObjectId(userId));
		Document scheduleDoc = schedules.find(query).first();
		
		if(scheduleDoc == null) {
			return null;
		}
		
		return documentToSchedule(scheduleDoc);
	}
	
	public WorkSchedule findScheduleByLocation(String location){
		Document query = new Document("location", new ObjectId(location));
		Document scheduleDoc = schedules.find(query).first();
		
		if(scheduleDoc == null) {
			return null;
		}
		
		return documentToSchedule(scheduleDoc);
	}
	

	public List<WorkSchedule> readAllSchedules(){
		List<WorkSchedule> schedulesList = new ArrayList<>();
		try (MongoCursor<Document> cursor = schedules.find().iterator()) {
			while(cursor.hasNext()) {
				Document scheduleDoc = cursor.next();
				schedulesList.add(documentToSchedule(scheduleDoc));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} 
		return schedulesList;
	}

	public boolean updateSchedule(WorkSchedule schedule){
		if(schedule.getId() == null) {
			System.err.println("Error: Cannot update Schedule, lost ID.");
			return false;
		}
		
		try {
			Document filter = new Document("_id", new ObjectId(schedule.getId()));
			Document updateFields = new Document("$set", new Document()
					.append("subject", schedule.getSubject())
					.append("description", schedule.getDescription())
					.append("startTime", schedule.getStartTime())
					.append("endTime", schedule.getEndTime())
					.append("prioroty", schedule.getPriority())
					.append("location", schedule.getLocation()));
			
			UpdateResult result = schedules.updateOne(filter, updateFields);
			return result.getModifiedCount() > 0;
		} catch (Exception e) {
			System.err.println("Error when update user have ID: " + schedule.getId());
			e.printStackTrace();
			return false;
		}
	}

	public void createSchedule(WorkSchedule schedule) {
		Document scheduleDoc = new Document("$set", new Document()
				.append("userId", schedule.getUserId())
				.append("subject", schedule.getSubject())
				.append("description", schedule.getDescription())
				.append("startTime", schedule.getStartTime())
				.append("endTime", schedule.getEndTime())
				.append("prioroty", schedule.getPriority())
				.append("location", schedule.getLocation())
				.append("createdAt", schedule.getCreatedAt()));
		
		try {
			schedules.insertOne(scheduleDoc);
			System.out.println("User created with ID: " + scheduleDoc.getObjectId("_id"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean deleteSchedule(String id){
		try{
			Document filter = new Document("_id", new ObjectId(id));
			DeleteResult result = schedules.deleteOne(filter);
			return result.getDeletedCount() == 1;
		} catch (IllegalArgumentException e) {
			System.err.println("Error: ID user unexcepted" + id);
			return false;
		} catch (Exception e) {
			System.err.println("Error when delete User by ID: " + id);
			e.printStackTrace();
			return false;
		}
	}
	
	public WorkSchedule findUserBySubject(String subject) {
		Document query = new Document("subject", subject);
		Document scheduleDoc = schedules.find(query).first();
		
		return documentToSchedule(scheduleDoc);
	}
}
