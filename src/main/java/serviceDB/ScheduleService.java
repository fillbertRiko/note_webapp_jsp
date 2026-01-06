package serviceDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DAO.ScheduleDao;
import model.WorkSchedule;

///Lop service xu ly nghiep vu cho lich trinh (Schedule)
///Chuyen doi du lieu String (tu view) sang Date (model)
///Kiem tra logic thoi gian (ngay bat dau phai truoc ngay ket thuc)
///Goi DAO de thao tac du lieu
public class ScheduleService {
    
    private ScheduleDao scheduleDao = new ScheduleDao();
    // Dinh dang ngay gio input HTML5: yyyy-MM-dd'T'HH:mm
    private static final SimpleDateFormat HTML_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    ///Lay danh sach lich cua user
    ///Vi DAO hien tai lay tat ca, can loc thu cong theo userId tai day
    public List<WorkSchedule> getSchedulesByUser(String userId) {
        List<WorkSchedule> allSchedules = scheduleDao.readAllSchedules();
        List<WorkSchedule> userSchedules = new ArrayList<>();
        
        for (WorkSchedule s : allSchedules) {
            if (s.getUserId() != null && s.getUserId().equals(userId)) {
                userSchedules.add(s);
            }
        }
        return userSchedules;
    }

    public WorkSchedule getScheduleById(String id) {
        return scheduleDao.findScheduleById(id);
    }

    ///Xu ly logic tao moi lich trinh
    ///Parse thoi gian va validate logic
    public boolean create(String userId, String subject, String description, String startTimeStr, String endTimeStr, String priority, String location) {
        try {
            Date start = HTML_DATE_FORMAT.parse(startTimeStr);
            Date end = HTML_DATE_FORMAT.parse(endTimeStr);

            ///Kiem tra ngay ket thuc khong duoc truoc ngay bat dau
            if (end.before(start)) {
                return false; 
            }

            WorkSchedule schedule = new WorkSchedule();
            schedule.setUserId(userId);
            schedule.setSubject(subject);
            schedule.setDescription(description);
            schedule.setStartTime(start);
            schedule.setEndTime(end);
            schedule.setPriority(priority);
            schedule.setLocation(location);
            schedule.setCreatedAt(new Date());

            scheduleDao.createSchedule(schedule);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    ///Xu ly logic cap nhat thong tin
    ///Kiem tra ton tai truoc khi update
    public boolean update(String id, String subject, String description, String startTimeStr, String endTimeStr, String priority, String location) {
        try {
            WorkSchedule existing = scheduleDao.findScheduleById(id);
            if (existing == null) return false;

            Date start = HTML_DATE_FORMAT.parse(startTimeStr);
            Date end = HTML_DATE_FORMAT.parse(endTimeStr);
            
            if (end.before(start)) return false;

            existing.setSubject(subject);
            existing.setDescription(description);
            existing.setStartTime(start);
            existing.setEndTime(end);
            existing.setPriority(priority);
            existing.setLocation(location);

            return scheduleDao.updateSchedule(existing);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        return scheduleDao.deleteSchedule(id);
    }
}
