package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainController extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final String LOGIN = "LoginController";
	private static final String ERROR = "invalid.html";

	protected void processRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		res.setContentType("text/html;charset=UTF-8");
		String url = ERROR;
		try {
			String action = req.getParameter("btnLogin");
			if (action.equals("Login")) {
				url = LOGIN;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			req.getRequestDispatcher(url).forward(req, res);
		}
	}
}
