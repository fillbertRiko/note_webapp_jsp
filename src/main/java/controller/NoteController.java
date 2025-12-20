package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.PostDAO;
import model.Post;
import model.User;

@WebServlet("/dashboard-note")
public class NoteController extends HttpServlet {
	private PostDAO postDao = new PostDAO();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		User user = (session != null) ? (User) session.getAttribute("currentUser") : null;
		
		if(user != null) {
			List<Post> userPost = postDao.findPostByUser(user.getId());
		}
	}
}
