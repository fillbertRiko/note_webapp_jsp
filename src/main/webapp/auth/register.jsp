<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - Note Basement</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
    <script src="${pageContext.request.contextPath}/script/script.jsp"></script>
</head>
<body class="auth-body">
    <div class="login-container">
        <div class="login-card">
            <div class="login-header">
                <h2>Note Basement</h2>
                <p>Tạo tài khoản mới</p>
                <% if (request.getAttribute("errorMessage") != null) { %>
                    <div class="error-text" style="color: #ff4d4d; margin-top: 10px; font-size: 14px;">
                        ${errorMessage}
                    </div>
                <% } %>
            </div>

            <form action="${pageContext.request.contextPath}/register" method="POST" id="registerForm">
                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="text" id="fullname" name="fullname" required autocomplete="off">
                        <label for="fullname">Họ và Tên</label>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="email" id="email" name="email" required autocomplete="off" value="${oldEmail != null ? oldEmail: ''}">
                        <label for="email">Email</label>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="text" id="username" name="username" required autocomplete="off" value="${oldUsername != null ? oldUsername: ''}">
                        <label for="username">Tên đăng nhập</label>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="password" id="password" name="password" required value="${oldPassword != null ?oldPassword: ''}">
                        <label for="password">Mật khẩu</label>
                    </div>
                </div>

                <button type="submit" class="btn login-btn">
                    <span class="btn-text">Đăng ký ngay</span>
                </button>
            </form>

            <div class="signup-link">
                <p>Đã có tài khoản? <a href="${pageContext.request.contextPath}/auth/login.jsp">Đăng nhập</a></p>
            </div>
        </div>
    </div>
</body>
</html>