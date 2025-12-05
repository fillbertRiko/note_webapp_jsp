package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DAO.UserDao;
import model.User;
import serviceDB.Service;

public class LoginServlet extends HttpServlet{
	private Service userDAO;
	private UserDao checkUser;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");

		if(username == null || password == null ||
			username.trim().isEmpty() || password.trim().isEmpty()) {
				req.setAttribute("error", "Vui long nhap day du thong tin!");
				req.getRequestDispatcher("/login.jsp").forward(req,resp);
				return;
		}
		
		try {
			User user = checkUser.getUserByUsernameAndPassword(username, password);
			if(user != null) {
				req.getSession();
				req.getSession().setAttribute("user", user);
				resp.sendRedirect("index.jsp");
			} else {
				req.setAttribute("error", "Sai thong tin dang nhap!");
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("error", "Co loi xay ra trong qua trinh dang nhap!");
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}
	}

//	@Override
//	public void destroy() {
//		Service.closeConnection(userDAO);
//	}
}
