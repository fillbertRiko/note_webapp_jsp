package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import serviceDB.UserService;

@WebServlet("/forgotPassword")
public class ForgotPassword extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService;
	
	public void init() throws ServletException {
		userService = new UserService();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String newPass = req.getParameter("newPassword");
		String confirmPass = req.getParameter("confirmPassword");
		String username = req.getParameter("username");
		
		System.out.println(username.toString());
		if(newPass != null && newPass.equals(confirmPass)) {
			System.out.println(newPass.toString());
			
			
			boolean success = userService.updatePass(username, newPass);
			if(success) {
				res.sendRedirect(req.getContextPath() + "/auth/login.jsp?msg=reset_success");
			} else {
				req.setAttribute("errorMessage", "Username doesn't exits in system");
				req.getRequestDispatcher("/auth/forgotPassword.jsp").forward(req, res);
			}
		} else {
			req.setAttribute("errorMesage", "Password not match");
			req.getRequestDispatcher("/auth/forgotPassword.jsp").forward(req, res);
		}
	}
	
}
