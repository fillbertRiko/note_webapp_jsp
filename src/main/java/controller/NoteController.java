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

// Định nghĩa URL mapping: Mọi request gửi đến "/dashboard-note" sẽ vào Servlet này xử lý
@WebServlet("/dashboard-note")
public class NoteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Khai báo các Service để làm việc với Database (Logic nghiệp vụ)
    private PostService postService = new PostService();          // Xử lý bài viết (Thêm/Sửa/Xóa)
    private FriendService friendService = new FriendService();    // Xử lý quan hệ bạn bè
    private UserService userService = new UserService();          // Xử lý thông tin User
    private FriendInviteService inviteService = new FriendInviteService(); // Xử lý lời mời kết bạn

    // --- PHẦN 1: XỬ LÝ GET REQUEST (Lấy dữ liệu hiển thị) ---
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); // Đảm bảo không lỗi font tiếng Việt
        
        // Lấy tham số "action" từ URL để biết người dùng muốn làm gì
        String action = req.getParameter("action");
        if (action == null) {
            action = "view-wall"; // Mặc định là xem tường nhà (Dashboard)
        }

        // Điều hướng request dựa trên hành động
        switch (action) {
        case "view-wall":
            showWall(req, res); // Hiển thị toàn bộ trang Dashboard chính
            break;
        case "get-posts-html":
            loadPostJson(req, res); // Tải danh sách bài viết (dùng cho cuộn trang/load thêm)
            break;
        case "load-section":
            loadSectionFragment(req, res); // Chuyển đổi giữa các tab (Note/Friend/Info) bằng AJAX
            break;
        case "search-user":
            searchUserHtml(req, res); // Xử lý tìm kiếm người dùng (AJAX)
            break;
        default:
            showWall(req, res);
            break;
        }
    }

    // --- CHỨC NĂNG TÌM KIẾM NGƯỜI DÙNG ---
    private void searchUserHtml(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser"); // Lấy người đang đăng nhập
        if (currentUser == null) return; // Bảo mật: Chưa đăng nhập thì không cho tìm

        String keyword = req.getParameter("search"); // Lấy từ khóa người dùng gõ
        
        // 1. Gọi Service tìm trong Database những ai có tên hoặc email trùng khớp
        List<User> results = userService.searchUsers(keyword);
        
        // 2. Logic phức tạp: Kiểm tra mối quan hệ giữa "Mình" và "Họ" để hiển thị nút bấm đúng
        // Ví dụ: User A tìm thấy User B -> Nút là "Thêm bạn", "Hủy kết bạn" hay "Đồng ý"?
        java.util.Map<String, String> relationshipMap = new java.util.HashMap<>();
        for (User u : results) {
            // checkRelationship trả về: FRIEND, STRANGER, SENT_REQUEST, v.v.
            String rel = friendService.checkRelationship(currentUser.getId(), u.getId());
            relationshipMap.put(u.getId(), rel);
        }

        // 3. Gửi dữ liệu sang file JSP nhỏ (fragment) để render kết quả
        req.setAttribute("userList", results);
        req.setAttribute("relationshipMap", relationshipMap);
        
        req.getRequestDispatcher("/note/user-search-result.jsp").forward(req, res);
    }

    // --- CHỨC NĂNG CHUYỂN TAB (AJAX) ---
    // Hàm này giúp bấm menu Note/Friend/Info mà không cần load lại cả trang
    private void loadSectionFragment(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        String section = req.getParameter("section"); // Tab muốn xem (friend, info, wall)
        String ownerId = req.getParameter("_id");     // ID của chủ nhà đang xem
        
        if (currentUser == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Nếu không có _id, mặc định là xem nhà của chính mình
        if (ownerId == null || ownerId.isEmpty()) {
            ownerId = currentUser.getId();
        }

        // --- Xử lý Tab Bạn Bè ---
        if ("friend".equals(section)) {
            if(ownerId.equals(currentUser.getId())) {
                // Nếu là nhà mình: Xem được cả lời mời kết bạn (Đến và Đi)
                req.setAttribute("friendList", friendService.getFriendOfUser(ownerId));
                req.setAttribute("receivedList", inviteService.getReceiverInvites(ownerId)); // Ai mời mình?
                req.setAttribute("sentList", inviteService.getSentInvites(ownerId));         // Mình mời ai?
                req.setAttribute("isOwner", true);
            } else {
                // Nếu xem nhà người khác: Chỉ thấy danh sách bạn của họ
                req.setAttribute("friendList", friendService.getFriendOfUser(ownerId));
                req.setAttribute("isOwner", false);
            }
            req.getRequestDispatcher("/note/friend-fragment.jsp").forward(req, res);
            
        // --- Xử lý Tab Thông tin ---
        } else if ("info".equals(section)) {
            User wallOwner = userService.showInformation(ownerId);
            req.setAttribute("wallOwner", wallOwner);
            req.setAttribute("isMyWall", ownerId.equals(currentUser.getId()));
            req.getRequestDispatcher("/note/info-fragment.jsp").forward(req, res);
            
        // --- Xử lý Tab Ghi chú (Mặc định) ---
        } else {
            // Lấy danh sách bài viết (có kiểm tra quyền riêng tư: Bạn bè mới thấy bài Friend Only)
            List<Post> listPosts = postService.getPostByVisitor(ownerId, currentUser.getId(), 1, "");
            User wallOwner = userService.showInformation(ownerId);
            
            req.setAttribute("listPosts", listPosts);
            req.setAttribute("wallOwner", wallOwner);
            req.setAttribute("isMyWall", ownerId.equals(currentUser.getId()));
            
            // Xác định mối quan hệ để hiện nút "Kết bạn" trên bìa hồ sơ
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

    // --- HÀM TẢI BÀI VIẾT (Dùng cho load thêm/tìm kiếm bài viết) ---
    private void loadPostJson(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        String ownerId = req.getParameter("_id");
        String keyword = req.getParameter("search");
        
        if (currentUser == null) return;
        if (ownerId == null) ownerId = currentUser.getId();
        if (keyword == null) keyword = "";

        // Lấy bài viết dựa trên ID chủ nhà, ID khách xem, và từ khóa
        List<Post> listPosts = postService.getPostByVisitor(ownerId, currentUser.getId(), 1, keyword);

        req.setAttribute("listPosts", listPosts);
        req.setAttribute("currentUser", currentUser);
        req.getRequestDispatcher("/note/post-list-fragment.jsp").forward(req, res);
    }

    // --- HÀM HIỂN THỊ TRANG CHỦ (Full Page) ---
    private void showWall(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Chưa đăng nhập -> Đá về trang Login
        if (currentUser == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login.jsp");
            return;
        }

        String keyword = req.getParameter("search");
        String ownerId = req.getParameter("_id"); // ID người mà mình đang vào xem tường
        String relationship = "SELF";
        int page = 1;
        
        if (keyword == null) keyword = "";
        // Nếu không có _id trên URL, tức là đang xem tường nhà mình
        if (ownerId == null || ownerId.isEmpty()) {
            ownerId = currentUser.getId();
        }
        
        // Lấy thông tin chủ nhà
        User wallOwner = userService.showInformation(ownerId);
        
        // Nếu xem tường người khác, kiểm tra quan hệ
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
        
        // Đẩy dữ liệu ra JSP
        req.setAttribute("wallOwner", wallOwner);
        req.setAttribute("isMyWall", ownerId.equals(currentUser.getId())); // Biến boolean để JSP biết ẩn/hiện nút sửa xóa
        
        List<Post> listPosts = postService.getPostByVisitor(ownerId, currentUser.getId(), page, keyword);
        List<?> friendList = friendService.getFriendOfUser(ownerId);

        req.setAttribute("friendList", friendList);
        req.setAttribute("listPosts", listPosts);
        req.setAttribute("ownerId", ownerId);
        req.setAttribute("currentPage", page);
        
        // Chuyển hướng về file giao diện chính
        req.getRequestDispatcher("/note/dashboard.jsp").forward(req, res);
    }

    // --- PHẦN 2: XỬ LÝ POST REQUEST (Gửi form dữ liệu) ---
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action"); // Xem form gửi lên là hành động gì

        if ("create".equals(action)) {
            createPost(req, res);       // Tạo bài viết mới
        } else if ("delete".equals(action)) {
            deletePost(req, res);       // Xóa bài viết
        } else if ("edit".equals(action)) {
            updatePost(req, res);       // Sửa bài viết
        } else if("update-profile".equals(action)) {
            updateProfile(req,res);     // Cập nhật thông tin cá nhân
        } else if("send-invite".equals(action)) {
            handleSendInvite(req, res); // Gửi lời mời kết bạn
        } else if("accept-invite".equals(action)) {
            handleAcceptInvite(req, res); // Đồng ý kết bạn
        } else if("cancel-invite".equals(action)) {
            handleCancelInvite(req, res); // Hủy lời mời (người nhận hủy)
        } else if("cancel-invite-by-user".equals(action)) {
            handleCancelInviteByUser(req, res); // (Chức năng phụ)
        } else if("unfriend".equals(action)) { 
            handleUnfriend(req, res);   // Hủy kết bạn (Unfriend)
        }
    }

    // --- CÁC HÀM XỬ LÝ LOGIC CHI TIẾT CHO POST ---

    private void handleUnfriend(HttpServletRequest req, HttpServletResponse res) throws IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        String friendId = req.getParameter("friendId");
        
        if(currentUser != null && friendId != null) {
            friendService.unfriend(currentUser.getId(), friendId); // Gọi service xóa quan hệ
        }
        // Sau khi xóa xong, reload lại tab danh sách bạn bè
        res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&section=friend");
    }

    private void handleCancelInviteByUser(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall");
    }

    // Hủy lời mời kết bạn (Từ phía người nhận hoặc người gửi muốn thu hồi)
    private void handleCancelInvite(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String inviteId = req.getParameter("inviteId");
        inviteService.deleteInvite(inviteId);
        res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&section=friend");
    }

    // Chấp nhận lời mời kết bạn
    private void handleAcceptInvite(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String inviteId = req.getParameter("inviteId");
        inviteService.acceptInvite(inviteId); // Chuyển trạng thái sang "Bạn bè"
        res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&section=friend");
    }

    // Gửi lời mời kết bạn mới
    private void handleSendInvite(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        String receiverId = req.getParameter("receiverId");
        
        inviteService.sendInvite(currentUser.getId(), receiverId);
        // Load lại tường của người đó để thấy nút chuyển thành "Đã gửi lời mời"
        res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&_id=" + receiverId);
    }

    // Cập nhật thông tin cá nhân (Tên, Mật khẩu)
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
        
        // Chỉ đổi mật khẩu nếu người dùng có nhập vào ô password
        if(newPassword != null && !newPassword.trim().isEmpty()) {
            currentUser.setPassword(newPassword); 
        } else {
            currentUser.setPassword(null); // Null để Service biết là không đổi pass
        }
        
        boolean isUpdated = userService.updateUser(currentUser);
        if (isUpdated) {
            // Cập nhật lại session với thông tin mới nhất từ DB
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
        
        // Lấy dữ liệu từ Form
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String topicId = req.getParameter("topicId");
        String accessLevelId = req.getParameter("accessLevelId"); // Public, Private, Friend Only
        String allowCommentStatus = req.getParameter("allowComment");
        String[] viewerArray = req.getParameterValues("allowViewer"); // Danh sách bạn bè được xem (nếu chọn chế độ cụ thể)
        
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
            res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&_id=" + currentUser.getId());
        } else {
            res.sendRedirect(req.getContextPath() + "/error");
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
        // Gọi Service xóa (Service sẽ kiểm tra xem bài đó có đúng là của user này không)
        boolean isDeleted = postService.deleteMyPost(postId, currentUser.getId());
        
        if (isDeleted) {
            res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&_id=" + currentUser.getId());
        } else {
            res.sendRedirect(req.getContextPath() + "/error");
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
            res.sendRedirect(req.getContextPath() + "/error");
        }
    }
}