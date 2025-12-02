package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.UserDao;
import model.User;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDao userDao;

    public void init() {
        try {
			userDao = new UserDao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getServletPath();

        try{
            switch (action) {
                case "/new":
                    showNewForm(req, res);
                    break;
                case "/insert":
                    insertUser(req, res);
                    break;
                case "/delete":
                    deleteUser(req, res);
                    break;
                case "/edit":
                    showEditForm(req, res);
                    break;
                case "/update":
                    updateUser(req, res);
                    break;
                default:
                    listUser(req, res);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listUser(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, IOException, ServletException {
        List<User> listUser = userDao.readAllUsers();
        req.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user-list.jsp");
        dispatcher.forward(req, res);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("user-form.jsp");
        dispatcher.forward(req, res);
    }
    private void showEditForm(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        User existingUser = userDao.readUser(id);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user-form.jsp");
        req.setAttribute("user", existingUser);
        dispatcher.forward(req, res);
    }

    private void insertUser(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        int age = Integer.parseInt(req.getParameter("age"));
        User newUser = new User(username, password, fullname, email, age);
        userDao.createUser(newUser);
        res.sendRedirect("list");
    }   

    private void updateUser(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        int age = Integer.parseInt(req.getParameter("age"));

        User user = new User(id, username, password, fullname, email, age);
        userDao.updateUser(user);
        res.sendRedirect("list");
    }   

    private void deleteUser(HttpServletRequest req, HttpServletResponse res)
            throws SQLException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        userDao.deleteUser(id);
        res.sendRedirect("list");
    }
}
