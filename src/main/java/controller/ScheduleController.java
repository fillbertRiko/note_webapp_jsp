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

///Controller dieu huong cho chuc nang quan ly lich trinh
///Kiem tra session nguoi dung
///Nhan request tu JSP va goi Service xu ly tuong ung
@WebServlet("/schedule")
public class ScheduleController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ScheduleService scheduleService;

    @Override
    public void init() throws ServletException {
        scheduleService = new ScheduleService();
    }

    ///Xu ly cac request GET (Xem danh sach, Xem form sua)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        ///Kiem tra neu chua dang nhap thi da ve trang login
        if (currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "view";

        switch (action) {
            case "view":
                ///Lay danh sach va chuyen ve trang dashboard
                List<WorkSchedule> mySchedules = scheduleService.getSchedulesByUser(currentUser.getId());
                req.setAttribute("scheduleList", mySchedules);
                req.getRequestDispatcher("/note/workDashboard.jsp").forward(req, res);
                break;
                
            case "edit":
                ///Lay thong tin chi tiet de hien thi len form sua
                String id = req.getParameter("id");
                WorkSchedule schedule = scheduleService.getScheduleById(id);
                
                if (schedule != null && schedule.getUserId().equals(currentUser.getId())) {
                    req.setAttribute("schedule", schedule);
                    req.getRequestDispatcher("/note/editSchedule.jsp").forward(req, res);
                } else {
                    res.sendRedirect(req.getContextPath() + "/schedule?action=view");
                }
                break;
                
            default:
                res.sendRedirect(req.getContextPath() + "/schedule?action=view");
                break;
        }
    }

    ///Xu ly cac request POST (Tao moi, Cap nhat, Xoa)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        
        // Lay du lieu tu form
        String subject = req.getParameter("subject");
        String description = req.getParameter("description");
        String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");
        String priority = req.getParameter("priority");
        String location = req.getParameter("location");

        if ("create".equals(action)) {
            boolean success = scheduleService.create(currentUser.getId(), subject, description, startTime, endTime, priority, location);
            if (success) {
                res.sendRedirect(req.getContextPath() + "/schedule?action=view&msg=create_success");
            } else {
                res.sendRedirect(req.getContextPath() + "/schedule?action=view&error=create_failed");
            }
            
        } else if ("update".equals(action)) {
            String id = req.getParameter("id");
            scheduleService.update(id, subject, description, startTime, endTime, priority, location);
            res.sendRedirect(req.getContextPath() + "/schedule?action=view");
            
        } else if ("delete".equals(action)) {
            String id = req.getParameter("id");
            // Kiem tra quyen so huu truoc khi xoa
            WorkSchedule s = scheduleService.getScheduleById(id);
            if (s != null && s.getUserId().equals(currentUser.getId())) {
                scheduleService.delete(id);
            }
            res.sendRedirect(req.getContextPath() + "/schedule?action=view");
        }
    }
}
