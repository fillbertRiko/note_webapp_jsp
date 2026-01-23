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

///Controller xu ly cac request tu nguoi dung 
///tao CRUD 
///tao muc tim kiem bai viet
@WebServlet("/dashboard-note")
public class NoteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private PostService postService = new PostService();      
    private FriendService friendService = new FriendService();
    private UserService userService = new UserService();      
    private FriendInviteService inviteService = new FriendInviteService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        String action = req.getParameter("action");
        if (action == null) {
            action = "view-wall";
        }

        switch (action) {
        case "view-wall":
            showWall(req, res); 
            break;
        case "get-posts-html":
            loadPostJson(req, res); 
            break;
        case "load-section":
            loadSectionFragment(req, res);
            break;
        case "search-user":
            searchUserHtml(req, res); 
            break;
        default:
            showWall(req, res);
            break;
        }
    }

    private void searchUserHtml(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return;

        String keyword = req.getParameter("search");
        List<User> results = userService.searchUsers(keyword);
        
        java.util.Map<String, String> relationshipMap = new java.util.HashMap<>();
        for (User u : results) {
            String rel = friendService.checkRelationship(currentUser.getId(), u.getId());
            relationshipMap.put(u.getId(), rel);
        }

        req.setAttribute("userList", results);
        req.setAttribute("relationshipMap", relationshipMap);
        req.getRequestDispatcher("/note/user-search-result.jsp").forward(req, res);
    }

    private void loadSectionFragment(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        String section = req.getParameter("section"); 
        String ownerId = req.getParameter("_id");     
        
        if (currentUser == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (ownerId == null || ownerId.isEmpty()) {
            ownerId = currentUser.getId();
        }

        if ("friend".equals(section)) {
            if(ownerId.equals(currentUser.getId())) {
                req.setAttribute("friendList", friendService.getFriendOfUser(ownerId));
                req.setAttribute("receivedList", inviteService.getReceiverInvites(ownerId));
                req.setAttribute("sentList", inviteService.getSentInvites(ownerId));
                req.setAttribute("suggestionList", friendService.getSuggestedFriends(ownerId));
                req.setAttribute("isOwner", true);
            } else {
                req.setAttribute("friendList", friendService.getFriendOfUser(ownerId));
                req.setAttribute("isOwner", false);
            }
            req.getRequestDispatcher("/note/friend-fragment.jsp").forward(req, res);
        } else if ("info".equals(section)) {
            User wallOwner = userService.showInformation(ownerId);
            req.setAttribute("wallOwner", wallOwner);
            req.setAttribute("isMyWall", ownerId.equals(currentUser.getId()));
            req.getRequestDispatcher("/note/info-fragment.jsp").forward(req, res);
        } else {
            List<Post> listPosts = postService.getPostByVisitor(ownerId, currentUser.getId(), 1, "");
            User wallOwner = userService.showInformation(ownerId);
            req.setAttribute("listPosts", listPosts);
            req.setAttribute("wallOwner", wallOwner);
            req.setAttribute("isMyWall", ownerId.equals(currentUser.getId()));
            
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

    private void showWall(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
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
        
        if (!ownerId.equals(currentUser.getId())) {
            relationship = friendService.checkRelationship(currentUser.getId(), ownerId);
        }
        
        req.setAttribute("relationship", relationship);
        
        try {
            String pageStr = req.getParameter("page");
            if (pageStr != null) page = Integer.parseInt(pageStr);
        } catch (NumberFormatException e) {
            page = 1;
        }
        
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action"); 

        if ("create".equals(action)) {
            createPost(req, res);       
        } else if ("delete".equals(action)) {
            deletePost(req, res);      
        } else if ("edit".equals(action)) {
            updatePost(req, res);     
        } else if("update-profile".equals(action)) {
            updateProfile(req,res);   
        } else if("send-invite".equals(action)) {
            handleSendInvite(req, res);
        } else if("accept-invite".equals(action)) {
            handleAcceptInvite(req, res);
        } else if("cancel-invite".equals(action)) {
            handleCancelInvite(req, res); 
        } else if("cancel-invite-by-user".equals(action)) {
            handleCancelInviteByUser(req, res);
        } else if("unfriend".equals(action)) {
            handleUnfriend(req, res);   
        }
    }

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
        inviteService.deleteInvite(inviteId);
        res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&section=friend");
    }

    private void handleAcceptInvite(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String inviteId = req.getParameter("inviteId");
        inviteService.acceptInvite(inviteId);
        res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&section=friend");
    }

    private void handleSendInvite(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User currentUser = (User) req.getSession().getAttribute("currentUser");
        String receiverId = req.getParameter("receiverId");
        
        inviteService.sendInvite(currentUser.getId(), receiverId);
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
    
    private void createPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
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
            res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&_id=" + currentUser.getId());
        } else {
            res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&error=create_failed");
        }
    }

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
            res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&error=delete_failed");
        }
    }

    private void updatePost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        System.out.println(currentUser.toString());
        
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
            res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&error=edit_failed");
        }
    }
}