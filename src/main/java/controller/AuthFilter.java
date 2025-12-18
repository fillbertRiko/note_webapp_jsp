package controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

///muc dich bao ve cac noi dung nhay cam
///kiem tra session co chua nguoi dung hay khong
///neu da dang nhap co the di tiep, con neu chua duoc chuyen sang trang dang nhap
///SU dung annotation de anh xa toi filter toi cac URL
///lay session 
///tao logic kiem tra
///lay duong dan theo yeu cau va them cac file tinh nhu css, JS

@WebFilter(urlPatterns = {"/*"})
public class AuthFilter implements Filter {
	public void init(FilterConfig fConfig) throws ServletException {
		
	}
	
	public void destroy() {
		
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
	    HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) res;
	    
	    HttpSession session = request.getSession(false);
	    String requestURI = request.getRequestURI();
	    String contextPath = request.getContextPath();
	    
	    boolean loggedIn = (session != null && session.getAttribute("currentUser") != null);

	    boolean isAuthJSP = requestURI.contains("/auth/");
	    
	    boolean isAuthServlet = requestURI.endsWith("/login") || 
	                            requestURI.endsWith("/register") || 
	                            requestURI.endsWith("/logout") ||
	                            requestURI.endsWith("/reset-password");

	    boolean isIndexPage = requestURI.equals(contextPath + "/") || requestURI.endsWith("/index.jsp");
	    boolean isStaticResource = requestURI.contains("/style/") || requestURI.endsWith(".css") || 
	                               requestURI.endsWith(".png") || requestURI.endsWith(".jpg");

	    // LOGIC CHO PHÉP
	    if (loggedIn || isAuthJSP || isAuthServlet || isIndexPage || isStaticResource) {
	        chain.doFilter(request, response);
	    } else {
	        response.sendRedirect(contextPath + "/auth/login.jsp");
	    }
	}
}
