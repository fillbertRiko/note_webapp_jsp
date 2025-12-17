package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
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

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("/index.jsp");
		rd.forward(req, res);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html;charset=UTF-8");
		
			String username = req.getParameter("username");
			String password = req.getParameter("password");
//			System.out.println(username.toString());
		
			UserService service = new UserService();
			User user = service.login(username, password);
			if(user != null) {
				HttpSession oldSession = req.getSession(true);
				if(oldSession != null) {
					oldSession.invalidate();
				}
				HttpSession session = req.getSession(true);
		        session.setAttribute("currentUser", user);
		        res.sendRedirect(req.getContextPath() + "/index.jsp");
		        System.out.println("SessionId : " + req.getSession().getId());
		        return;
			}else {
				HttpSession session = req.getSession(false);
				if(session != null) {
					session.invalidate();
				}
				req.setAttribute("errorMessage", "Wrong username or password");
				res.sendRedirect(req.getContextPath() + "/auth/login.jsp?error=1");
				System.out.println(req.getContextPath().toString());
			}
	}
}
