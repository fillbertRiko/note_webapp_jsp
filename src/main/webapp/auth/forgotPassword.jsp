<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu - Note Basement</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
</head>
<body>
    <div class="login-container">
        <div class="login-card">
            <div class="login-header">
                <h2>Note Basement</h2>
                <p>Khôi phục mật khẩu của bạn</p>
                
                <%-- Hiển thị lỗi nếu có --%>
                <% if (request.getAttribute("errorMessage") != null) { %>
                    <div class="error-text" style="color: #ff4d4d; margin-top: 10px; font-size: 14px;">
                        <%= request.getAttribute("errorMessage") %>
                    </div>
                <% } %>
            </div>

            <form action="${pageContext.request.contextPath}/forgotPassword" method="POST">
                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="text" id="username" name="username" required autocomplete="off">
                        <label for="username">Tên đăng nhập</label>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="password" id="newPassword" name="newPassword" required>
                        <label for="newPassword">Mật khẩu mới</label>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="password" id="confirmPassword" name="confirmPassword" required>
                        <label for="confirmPassword">Xác nhận mật khẩu</label>
                    </div>
                </div>

                <button type="submit" class="btn login-btn">
                    <span class="btn-text">Cập nhật mật khẩu</span>
                </button>
                
                <div class="signup-link" style="margin-top: 20px;">
                    <a href="${pageContext.request.contextPath}/auth/login.jsp">Quay lại Đăng nhập</a>
                </div>
            </form>
        </div>
    </div>

    <script>
        document.querySelectorAll('.input-wrapper input').forEach(input => {
            const check = () => {
                if (input.value.trim() !== "") input.classList.add('has-value');
                else input.classList.remove('has-value');
            };
            input.addEventListener('blur', check);
            input.addEventListener('input', check);
        });
    </script>
</body>
</html>