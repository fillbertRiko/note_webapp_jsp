package controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import serviceDB.UserService;

@WebServlet("/register")
public class RegisterController extends HttpServlet {

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
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		res.setContentType("text/html;charset=UTF-8");

		String fullname = req.getParameter("fullname");
		String username = req.getParameter("username");
		String email = req.getParameter("email");
		String password = req.getParameter("password");

		if (fullname == null || username == null || email == null || password == null || fullname.isEmpty()
				|| username.isEmpty() || password.length() < 6) {
			req.setAttribute("errorMessage", "Please, fill full blank");
			req.getRequestDispatcher("/auth/register.jsp").forward(req, res);
			return;
		}

		User newUser = new User();
		newUser.setFullname(fullname);
		newUser.setEmail(email);
		newUser.setPassword(password);
		newUser.setUsername(username);
		newUser.setCreatedAt(new Date());

		User result = userService.register(newUser);

		if (result != null) {
			res.sendRedirect(req.getContextPath() + "/auth/login.jsp?msg=register_success");
		} else {
			req.setAttribute("errorMessage", "Username or email hasbeen use, please change!");
			req.setAttribute("oldUsername", username);
			req.setAttribute("oldPassword", password);
			req.setAttribute("oldEmail", email);

			req.getRequestDispatcher("/auth/register.jsp").forward(req, res);
		}
	}

}
