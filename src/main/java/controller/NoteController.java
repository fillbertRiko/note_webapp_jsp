package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Post;
import model.User;
import serviceDB.FriendInviteService;
import serviceDB.FriendService;
import serviceDB.PostService;
import serviceDB.UserService;

// Định nghĩa đường dẫn: Mọi yêu cầu đến "/dashboard-note" sẽ do Servlet này xử lý
@WebServlet("/dashboard-note")
public class NoteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Khai báo các Service để xử lý nghiệp vụ với Database
    private PostService postService = new PostService();          // Quản lý bài viết
    private FriendService friendService = new FriendService();    // Quản lý bạn bè
    private UserService userService = new UserService();          // Quản lý thông tin user
    private FriendInviteService inviteService = new FriendInviteService(); // Quản lý lời mời kết bạn

    // --- PHẦN 1: XỬ LÝ GET REQUEST (Lấy dữ liệu hiển thị) ---
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); // Đảm bảo không lỗi font tiếng Việt
        
        // Lấy tham số "action" để biết người dùng muốn xem gì (Tường nhà, tìm kiếm, hay chuyển tab...)
        String action = req.getParameter("action");
        if (action == null) {
            action = "view-wall"; // Mặc định là xem tường nhà
        }

        switch (action) {
        case "view-wall":
            showWall(req, res); // Hiển thị trang Dashboard đầy đủ
            break;
        case "get-posts-html":
            loadPostJson(req, res); // Tải thêm bài viết (dùng cho tính năng cuộn trang)
            break;
        case "load-section":
            loadSectionFragment(req, res); // Chuyển đổi giữa các tab Note/Friend/Info (AJAX)
            break;
        case "search-user":
            searchUserHtml(req, res); // Tìm kiếm người dùng
            break;
        default:
            showWall(req, res);
            break;
        }
    }

    // --- CHỨC NĂNG TÌM KIẾM NGƯỜI DÙNG ---
    private void searchUserHtml(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return; // Chưa đăng nhập thì không tìm được

        String keyword = req.getParameter("search");
        // 1. Tìm user trong DB theo tên hoặc email
        List<User> results = userService.searchUsers(keyword);
        
        // 2. Xác định mối quan hệ với từng người tìm được (Để hiển thị nút Kết bạn, Hủy kết bạn, hay Chấp nhận)
        java.util.Map<String, String> relationshipMap = new java.util.HashMap<>();
        for (User u : results) {
            String rel = friendService.checkRelationship(currentUser.getId(), u.getId());
            relationshipMap.put(u.getId(), rel);
        }

        // 3. Gửi dữ liệu sang file JSP kết quả
        req.setAttribute("userList", results);
        req.setAttribute("relationshipMap", relationshipMap);
        req.getRequestDispatcher("/note/user-search-result.jsp").forward(req, res);
    }

    // --- CHỨC NĂNG CHUYỂN TAB (AJAX) ---
    private void loadSectionFragment(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        String section = req.getParameter("section"); // Tab muốn xem (friend, info, wall)
        String ownerId = req.getParameter("_id");     // ID của người mình đang xem tường
        
        if (currentUser == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (ownerId == null || ownerId.isEmpty()) {
            ownerId = currentUser.getId(); // Mặc định xem tường nhà mình
        }

        // Xử lý Tab Bạn Bè
        if ("friend".equals(section)) {
            if(ownerId.equals(currentUser.getId())) {
                // Nếu là nhà mình: Xem được danh sách bạn, lời mời đến, lời mời đi
                req.setAttribute("friendList", friendService.getFriendOfUser(ownerId));
                req.setAttribute("receivedList", inviteService.getReceiverInvites(ownerId));
                req.setAttribute("sentList", inviteService.getSentInvites(ownerId));
                req.setAttribute("isOwner", true);
            } else {
                // Nếu xem nhà người khác: Chỉ thấy danh sách bạn của họ
                req.setAttribute("friendList", friendService.getFriendOfUser(ownerId));
                req.setAttribute("isOwner", false);
            }
            req.getRequestDispatcher("/note/friend-fragment.jsp").forward(req, res);
            
        // Xử lý Tab Thông tin cá nhân
        } else if ("info".equals(section)) {
            User wallOwner = userService.showInformation(ownerId);
            req.setAttribute("wallOwner", wallOwner);
            req.setAttribute("isMyWall", ownerId.equals(currentUser.getId()));
            req.getRequestDispatcher("/note/info-fragment.jsp").forward(req, res);
            
        // Xử lý Tab Bài viết (Wall)
        } else {
            // Lấy danh sách bài viết (có kiểm tra quyền riêng tư)
            List<Post> listPosts = postService.getPostByVisitor(ownerId, currentUser.getId(), 1, "");
            User wallOwner = userService.showInformation(ownerId);
            req.setAttribute("listPosts", listPosts);
            req.setAttribute("wallOwner", wallOwner);
            req.setAttribute("isMyWall", ownerId.equals(currentUser.getId()));
            
            // Kiểm tra quan hệ để hiện nút trên bìa hồ sơ
            String relationship = "STRANGER";
            if(!ownerId.equals(currentUser.getId())) {
                relationship = friendService.checkRelationship(currentUser.getId(), ownerId);
            } else {
                relationship = "SELF";
            }
            
            req.setAttribute("relationship", relationship);
            req.getRequestDispatcher("/note/wall-fragment.jsp").forward(req, res);
        }
    }

    // --- HÀM TẢI BÀI VIẾT (Dùng cho load thêm hoặc search bài) ---
    private void loadPostJson(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        String ownerId = req.getParameter("_id");
        String keyword = req.getParameter("search");
        
        if (currentUser == null) return;
        if (ownerId == null) ownerId = currentUser.getId();
        if (keyword == null) keyword = "";

        List<Post> listPosts = postService.getPostByVisitor(ownerId, currentUser.getId(), 1, keyword);

        req.setAttribute("listPosts", listPosts);
        req.setAttribute("currentUser", currentUser);
        req.getRequestDispatcher("/note/post-list-fragment.jsp").forward(req, res);
    }

    // --- HIỂN THỊ TRANG CHỦ FULL (Lần đầu truy cập) ---
    private void showWall(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Kiểm tra đăng nhập
        if (currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login.jsp");
            return;
        }

        String keyword = req.getParameter("search");
        String ownerId = req.getParameter("_id");
        String relationship = "SELF";
        int page = 1;
        
        if (keyword == null) keyword = "";
        if (ownerId == null || ownerId.isEmpty()) {
            ownerId = currentUser.getId();
        }
        
        User wallOwner = userService.showInformation(ownerId);
        
        // Kiểm tra quan hệ nếu đang xem tường người khác
        if (!ownerId.equals(currentUser.getId())) {
            relationship = friendService.checkRelationship(currentUser.getId(), ownerId);
        }
        
        req.setAttribute("relationship", relationship);
        
        // Xử lý phân trang
        try {
            String pageStr = req.getParameter("page");
            if (pageStr != null) page = Integer.parseInt(pageStr);
        } catch (NumberFormatException e) {
            page = 1;
        }
        
        // Đẩy dữ liệu ra JSP chính (dashboard.jsp)
        req.setAttribute("wallOwner", wallOwner);
        req.setAttribute("isMyWall", ownerId.equals(currentUser.getId()));
        
        List<Post> listPosts = postService.getPostByVisitor(ownerId, currentUser.getId(), page, keyword);
        List<?> friendList = friendService.getFriendOfUser(ownerId);

        req.setAttribute("friendList", friendList);
        req.setAttribute("listPosts", listPosts);
        req.setAttribute("ownerId", ownerId);
        req.setAttribute("currentPage", page);
        
        req.getRequestDispatcher("/note/dashboard.jsp").forward(req, res);
    }

    // --- PHẦN 2: XỬ LÝ POST REQUEST (Gửi form) ---
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action"); // Kiểm tra hành động

        if ("create".equals(action)) {
            createPost(req, res);       // Tạo bài mới
        } else if ("delete".equals(action)) {
            deletePost(req, res);       // Xóa bài
        } else if ("edit".equals(action)) {
            updatePost(req, res);       // Sửa bài
        } else if("update-profile".equals(action)) {
            updateProfile(req,res);     // Cập nhật thông tin cá nhân
        } else if("send-invite".equals(action)) {
            handleSendInvite(req, res); // Gửi lời mời kết bạn
        } else if("accept-invite".equals(action)) {
            handleAcceptInvite(req, res); // Đồng ý kết bạn
        } else if("cancel-invite".equals(action)) {
            handleCancelInvite(req, res); // Hủy lời mời
        } else if("cancel-invite-by-user".equals(action)) {
            handleCancelInviteByUser(req, res);
        } else if("unfriend".equals(action)) {
            handleUnfriend(req, res);   // Hủy kết bạn (Unfriend)
        }
    }

    // --- CÁC HÀM XỬ LÝ LOGIC CHI TIẾT CHO POST ---

    private void handleUnfriend(HttpServletRequest req, HttpServletResponse res) throws IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        String friendId = req.getParameter("friendId");
        
        if(currentUser != null && friendId != null) {
            friendService.unfriend(currentUser.getId(), friendId);
        }
        res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&section=friend");
    }

    private void handleCancelInviteByUser(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall");
    }

    private void handleCancelInvite(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String inviteId = req.getParameter("inviteId");
        inviteService.deleteInvite(inviteId); // Xóa lời mời khỏi DB
        res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&section=friend");
    }

    private void handleAcceptInvite(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String inviteId = req.getParameter("inviteId");
        inviteService.acceptInvite(inviteId); // Chuyển trạng thái thành bạn bè
        res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&section=friend");
    }

    private void handleSendInvite(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        String receiverId = req.getParameter("receiverId");
        
        inviteService.sendInvite(currentUser.getId(), receiverId);
        // Load lại tường người đó để thấy nút chuyển trạng thái
        res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&_id=" + receiverId);
    }

    private void updateProfile(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        String fullname = req.getParameter("fullname");
        String newPassword = req.getParameter("newPassword");
        
        if(currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login.jsp");
            return;
        }
        
        currentUser.setFullname(fullname);
        
        // Chỉ đổi mật khẩu nếu ô nhập không để trống
        if(newPassword != null && !newPassword.trim().isEmpty()) {
            currentUser.setPassword(newPassword); 
        } else {
            currentUser.setPassword(null);
        }
        
        boolean isUpdated = userService.updateUser(currentUser);
        if (isUpdated) {
            User updatedUser = userService.showInformation(currentUser.getId());
            session.setAttribute("currentUser", updatedUser);
            res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&section=info");
        } else {
            res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&section=info&error=update_failed");
        }
    }
    
    // Tạo bài viết mới
    private void createPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Lấy dữ liệu từ form
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String topicId = req.getParameter("topicId");
        String accessLevelId = req.getParameter("accessLevelId");
        String allowCommentStatus = req.getParameter("allowComment");
        String[] viewerArray = req.getParameterValues("allowViewer");
        
        if (currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login.jsp");
            return;
        }

        List<String> allowViewerId = new ArrayList<>();
        if (viewerArray != null) {
            allowViewerId = Arrays.asList(viewerArray);
        }

        boolean isCreated = postService.create(title, content, topicId, currentUser, accessLevelId, allowViewerId, allowCommentStatus);
        if (isCreated) {
            // Thành công: Quay về trang chủ của mình
            res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&_id=" + currentUser.getId());
        } else {
            // Thất bại: Quay về Dashboard kèm thông báo lỗi (Thay vì trang /error)
            res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&error=create_failed");
        }
    }

    // Xóa bài viết
    private void deletePost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login.jsp");
            return;
        }

        String postId = req.getParameter("_id");
        boolean isDeleted = postService.deleteMyPost(postId, currentUser.getId());
        
        if (isDeleted) {
            res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&_id=" + currentUser.getId());
        } else {
            // Thất bại: Quay về Dashboard báo lỗi
            res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&error=delete_failed");
        }
    }

    // Cập nhật bài viết
    private void updatePost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Lấy dữ liệu cần sửa
        String postId = req.getParameter("_id");
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String topicId = req.getParameter("topicId");
        String accessLevelId = req.getParameter("accessLevelId");
        String allowCommentStatus = req.getParameter("allowComment");
        String[] viewerArray = req.getParameterValues("allowViewer");
        
        if (currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login.jsp");
            return;
        }

        List<String> allowViewerId = new ArrayList<>();
        if (viewerArray != null) {
            allowViewerId = Arrays.asList(viewerArray);
        }

        Post updatedInfo = new Post();
        updatedInfo.setTitle(title);
        updatedInfo.setContent(content);
        updatedInfo.setTopicId(topicId);
        updatedInfo.setAccessLevelId(accessLevelId);
        updatedInfo.setNumberAllowComment(allowCommentStatus);
        updatedInfo.setAllowViewer(allowViewerId);
        
        boolean isEdit = postService.editPost(postId, currentUser.getId(), updatedInfo);

        if (isEdit) {
            res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&_id=" + currentUser.getId());
        } else {
            // Thất bại: Quay về Dashboard báo lỗi
            res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&error=edit_failed");
        }
    }
}