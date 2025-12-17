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
		HttpServletRequest httpRequest = (HttpServletRequest) req;
		HttpServletResponse httpResponse = (HttpServletResponse) res;
	    
		String requestURI = httpRequest.getRequestURI();
		HttpSession session = httpRequest.getSession(false);
		
		boolean isLoginRequest = requestURI.endsWith("/login") || requestURI.endsWith("/login.jsp");
		boolean isRegisterRequest = requestURI.endsWith("/register") || requestURI.endsWith("/register.jsp");
		boolean isPublicResource = isLoginRequest || isRegisterRequest || requestURI.contains("/style/style.css");
		boolean isLoggedIn = (session != null && session.getAttribute("currentUser") != null);
		
		if(isLoggedIn || isPublicResource) {
			chain.doFilter(req, res);
		} else {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/index.jsp");
		}
	}
}
