package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

///Lay session hien tai kiem tra
///xoa bo session va huy hieu luc session 
///chuyen huong nguoi dung ve trang dang nhap

@WebServlet("/logout")
public class LogoutController extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		
		if(session != null) {
			session.removeAttribute("currentUser");
			session.invalidate();
		}
//		System.out.println("currentUser");
        res.sendRedirect(req.getContextPath() + "/index.jsp");
	}
}
