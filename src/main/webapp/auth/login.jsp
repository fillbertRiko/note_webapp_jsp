<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Ngăn chặn trình duyệt lưu cache để tránh lỗi xác thực cũ
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>
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
                
                <%-- Hiển thị thông báo lỗi từ LoginController --%>
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
                    <a href="#" class="forgot-password">Quên mật khẩu?</a>
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
        document.querySelectorAll('.input-wrapper input').forEach(input => {
            input.addEventListener('blur', () => {
                if (input.value.trim() !== "") {
                    input.classList.add('has-value');
                } else {
                    input.classList.remove('has-value');
                }
            });
        });
    </script>
</body>
</html>