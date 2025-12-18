<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<% if ("register_success".equals(request.getParameter("msg"))) { %>
    <p style="color: #4ade80;">Đăng ký thành công! Hãy đăng nhập để bắt đầu.</p>
<% } %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Note Basement</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
</head>
<body>
    <div class="login-container">
        <div class="login-card">
            <div class="login-header">
                <h2>Note Basement</h2>
                <p>Đăng nhập để tiếp tục</p>
                <% if (request.getAttribute("errorMessage") != null) { %>
                    <div class="error-text" style="color: #ff4d4d; margin-top: 10px; font-size: 14px;">
                        ${errorMessage}
                    </div>
                <% } %>
            </div>

            <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm">
                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="text" id="username" name="username" required autocomplete="off">
                        <label for="username">Tài khoản</label>
                        <span class="focus-border"></span>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="password" id="password" name="password" required>
                        <label for="password">Mật khẩu</label>
                        <span class="focus-border"></span>
                    </div>
                </div>

                <div class="form-options">
                    <label class="remember-wrapper">
                        <input type="checkbox" name="remember">
                        <span class="checkbox-label">Ghi nhớ tôi</span>
                    </label>
                    <a href="${pageContext.request.contextPath}/auth/forgotPassword.jsp" class="forgot-password">Quên mật khẩu?</a>
                </div>

                <button type="submit" class="btn login-btn">
                    <span class="btn-text">Sign In</span>
                </button>
            </form>

            <div class="signup-link">
                <p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/auth/register.jsp">Đăng ký ngay</a></p>
            </div>
        </div>
    </div>

    <script>
    function checkInput() {
        document.querySelectorAll('.input-wrapper input').forEach(input => {
            if (input.value.trim() !== "") {
                input.classList.add('has-value');
            } else {
                input.classList.remove('has-value');
            }
        });
    }

    document.querySelectorAll('.input-wrapper input').forEach(input => {
        input.addEventListener('blur', checkInput);
        input.addEventListener('input', checkInput);
    });

    window.onload = checkInput;
    </script>
</body>
</html>