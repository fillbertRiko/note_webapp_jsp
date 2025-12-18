package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import serviceDB.UserService;


@WebServlet("/login")
public class LoginController extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService;
	
	@Override
	public void init() throws ServletException {
		userService = new UserService();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if(session !=  null && session.getAttribute("currentUser") != null) {
			res.sendRedirect(req.getContextPath() + "/note/dashboard.jsp");
			return;
		}
		
		req.getRequestDispatcher("/auth/login.jsp").forward(req, res);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html;charset=UTF-8");
		
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		User user = userService.login(username, password);
		
		if(user != null) {
			HttpSession oldSession = req.getSession(false);
			if(oldSession != null) {
				oldSession.invalidate();
			}
			
			
			HttpSession session = req.getSession(true);
			session.setAttribute("currentUser", user);
			
			res.sendRedirect(req.getContextPath() + "/note/dashboard.jsp");
			return;
		} else {
			HttpSession session = req.getSession(false);
			if(session != null) {
				session.removeAttribute("currentUser");
			}
			
			req.setAttribute("errorMessage", "Username or password wrong, please check your username or password");
			req.getRequestDispatcher("/auth/login.jsp").forward(req, res);
		}
	}
}
