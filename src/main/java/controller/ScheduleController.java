package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.WorkSchedule;
import serviceDB.ScheduleService;

// Định nghĩa URL: Khi truy cập ".../schedule" thì Servlet này sẽ chạy
@WebServlet("/schedule")
public class ScheduleController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ScheduleService scheduleService;

    // Hàm khởi tạo: Chạy 1 lần duy nhất khi Server bật để chuẩn bị Service
    @Override
    public void init() throws ServletException {
        scheduleService = new ScheduleService();
    }

    // --- XỬ LÝ GET (Lấy dữ liệu để hiển thị) ---
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); // Hỗ trợ tiếng Việt
        
        // 1. Kiểm tra đăng nhập
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            // Chưa đăng nhập -> Đuổi về trang Login
            res.sendRedirect(req.getContextPath() + "/auth/login.jsp");
            return;
        }

        // 2. Xác định hành động (Action)
        String action = req.getParameter("action");
        if (action == null) action = "view"; // Mặc định là xem danh sách

        switch (action) {
            case "view":
                // --- XEM DANH SÁCH LỊCH ---
                // Lấy tất cả lịch của user hiện tại từ Database
                List<WorkSchedule> mySchedules = scheduleService.getSchedulesByUser(currentUser.getId());
                
                // Gửi danh sách sang JSP
                req.setAttribute("scheduleList", mySchedules);
                
                // Chuyển hướng đến giao diện hiển thị lịch
                req.getRequestDispatcher("/workSchedule/workDashboard.jsp").forward(req, res); 
                break;
                
            case "edit":
                // --- HIỂN THỊ FORM SỬA ---
                String id = req.getParameter("id"); // Lấy ID lịch cần sửa
                WorkSchedule schedule = scheduleService.getScheduleById(id);
                
                // Kiểm tra bảo mật: Lịch phải tồn tại VÀ phải là của chính mình tạo ra mới được sửa
                if (schedule != null && schedule.getUserId().equals(currentUser.getId())) {
                    req.setAttribute("schedule", schedule); // Gửi thông tin lịch cũ sang form để điền sẵn
                    req.getRequestDispatcher("/workSchedule/editSchedule.jsp").forward(req, res); 
                } else {
                    // Nếu cố tình sửa lịch người khác -> Đẩy về trang danh sách
                    res.sendRedirect(req.getContextPath() + "/schedule?action=view");
                }
                break;

            default:
                res.sendRedirect(req.getContextPath() + "/schedule?action=view");
                break;
        }
    }

    // --- XỬ LÝ POST (Nhận dữ liệu từ Form gửi lên) ---
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        // 1. Kiểm tra đăng nhập lại (cho chắc chắn)
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        
        // 2. Lấy dữ liệu từ các ô input trong form
        String subject = req.getParameter("subject");       // Tiêu đề
        String description = req.getParameter("description"); // Mô tả
        String startTime = req.getParameter("startTime");   // Giờ bắt đầu
        String endTime = req.getParameter("endTime");       // Giờ kết thúc
        String priority = req.getParameter("priority");     // Độ ưu tiên
        String location = req.getParameter("location");     // Địa điểm

        // 3. Phân loại xử lý
        if ("create".equals(action)) {
            // --- TẠO MỚI ---
            boolean success = scheduleService.create(currentUser.getId(), subject, description, startTime, endTime, priority, location);
            if (success) {
                // Thành công: Quay lại trang danh sách kèm thông báo
                res.sendRedirect(req.getContextPath() + "/schedule?action=view&msg=create_success");
            } else {
                // Thất bại: Quay lại kèm báo lỗi
                res.sendRedirect(req.getContextPath() + "/schedule?action=view&error=create_failed");
            }
            
        } else if ("update".equals(action)) {
            // --- CẬP NHẬT ---
            String id = req.getParameter("id"); // Lấy ID cần sửa
            // Gọi service để update vào DB
            scheduleService.update(id, subject, description, startTime, endTime, priority, location);
            res.sendRedirect(req.getContextPath() + "/schedule?action=view");

        } else if ("delete".equals(action)) {
            // --- XÓA ---
            String id = req.getParameter("id");
            WorkSchedule s = scheduleService.getScheduleById(id);
            
            // Bảo mật: Kiểm tra xem lịch này có đúng là của mình không rồi mới xóa
            if (s != null && s.getUserId().equals(currentUser.getId())) {
                scheduleService.delete(id);
            }
            res.sendRedirect(req.getContextPath() + "/schedule?action=view");
        }
    }
}