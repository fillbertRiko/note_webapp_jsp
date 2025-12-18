package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import serviceDB.SHA256Hasher;
import serviceDB.UserService;

public class ForgotPassword extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String newPass = req.getParameter("newPassword");
		String confirmPass = req.getParameter("comfirmPassword");
		String username = req.getParameter("username");
		
		if(newPass != null && newPass.equals(confirmPass)) {
			String hashedPass = SHA256Hasher.hash(newPass);
			
			boolean success = userService.updatePass(username, hashedPass);
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
