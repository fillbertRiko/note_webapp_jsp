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
import serviceDB.FriendService;
import serviceDB.PostService;

@WebServlet("/dashboard-note")
public class NoteController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PostService postService = new PostService();
	private FriendService friendService = new FriendService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		
		if(action == null) {
			action = "view-wall";
		}
		
		switch(action) {
		case "view-wall":
			showWall(req,res);
			break;
		case "get-posts-html":
			loadPostJson(req,res);
			break;
		default:
			showWall(req, res);
			break;
		}
	}

	private void loadPostJson(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = req.getSession(false);
	    User currentUser = (User) session.getAttribute("currentUser");
	    System.out.println(currentUser);
	    if(currentUser == null) return;
	    String ownerId = req.getParameter("_id");
	    if(ownerId == null) ownerId = currentUser.getId();
	    
	    String keyword = req.getParameter("search");
	    if(keyword == null) keyword = "";
	    List<Post> listPosts = postService.getPostByVisitor(ownerId, currentUser.getId(), 1, keyword);
	    
	    req.setAttribute("listPosts", listPosts);
	    req.setAttribute("currentUser", currentUser);
	    req.getRequestDispatcher("/note/post-list-fragment.jsp").forward(req, res);
	}

	private void showWall(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String keyword = req.getParameter("search");
	    if(keyword == null) keyword = "";
		
		if(currentUser == null) {
			res.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		
		String ownerId = req.getParameter("_id");
		if(ownerId == null || ownerId.isEmpty()) {
			ownerId = currentUser.getId();
		}
		
		int page = 1;
		try {
			String pageStr = req.getParameter("page");
			if(pageStr != null) page =Integer.parseInt(pageStr);
		} catch (NumberFormatException e) {
			page =1;
		}
		
		List<Post> listPosts = postService.getPostByVisitor(ownerId, currentUser.getId(), page, keyword);
		List<?> friendList = friendService.getFriendOfUser(ownerId);
		
		req.setAttribute("friendList", friendList);
		req.setAttribute("listPosts", listPosts);
		req.setAttribute("ownerId", ownerId);
		req.setAttribute("currentPage", page);
		
		req.getRequestDispatcher("/note/dashboard.jsp").forward(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		
		if("create".equals(action)) {
			createPost(req,res);
		} else if("delete".equals(action)) {
			deletePost(req,res);
		} else if("edit".equals(action)) {
			updatePost(req,res);
		}
	}

	private void createPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		HttpSession session = req.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		if(currentUser == null) {
			res.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		String topicId = req.getParameter("topicId");
		String accessLevelId = req.getParameter("accessLevelId");
		String allowCommentStatus = req.getParameter("allowComment");
		
		String[] viewerArray = req.getParameterValues("allowViewer");
		List<String> allowViewerId = new ArrayList<>();
		
		if(viewerArray != null) {
			allowViewerId = Arrays.asList(viewerArray);
		}
		
		boolean isCreated = postService.create(title, content, topicId,currentUser, accessLevelId, allowViewerId, allowCommentStatus);
		
		if(isCreated) {
			res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&id=" + currentUser.getId());
		} else {
			res.sendRedirect(req.getContextPath() + "error");
		}
	}
	
	private void deletePost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		if(currentUser == null ) {
			res.sendRedirect(req.getContextPath() + "/login");
			return;
		}
		
		String postId = req.getParameter("_id");
		
		boolean isDeleted = postService.deleteMyPost(postId, currentUser.getId());
		
		if(isDeleted) {
			res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&id=" + currentUser.getId());
		} else {
			res.sendRedirect(req.getContextPath() + "error");
		}
	}
	
	private void updatePost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		if(currentUser == null) {
			res.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		String postId = req.getParameter("_id");
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String topicId = req.getParameter("topicId");
        String accessLevelId = req.getParameter("accessLevelId");
        String allowCommentStatus = req.getParameter("allowComment");
        
		String[] viewerArray = req.getParameterValues("allowViewer");
		List<String> allowViewerId = new ArrayList<>();
		if(viewerArray != null) {
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
		System.out.println(isEdit);
		
		if(isEdit) {
			res.sendRedirect(req.getContextPath() + "/dashboard-note?action=view-wall&id=" + currentUser.getId());
		} else {
			res.sendRedirect(req.getContextPath() + "error");
		}
	} 
}
