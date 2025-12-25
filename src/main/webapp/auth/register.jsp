<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - Note Basement</title>
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
    
    <style>
        .input-wrapper input { padding-left: 35px; } /* Chừa chỗ cho icon bên trái */
        .input-icon { position: absolute; left: 0; top: 15px; color: rgba(255,255,255,0.6); transition: 0.3s; }
        .input-wrapper input:focus ~ .input-icon { color: var(--secondary-color); }
        .toggle-password { position: absolute; right: 0; top: 15px; color: rgba(255,255,255,0.5); cursor: pointer; }
        
        /* Alert Styles */
        .alert { padding: 12px; border-radius: 8px; margin-bottom: 20px; font-size: 14px; display: flex; align-items: center; gap: 10px; }
        .alert-error { background: rgba(255, 77, 77, 0.2); border: 1px solid rgba(255, 77, 77, 0.4); color: #ff4d4d; }
    </style>
</head>
<body class="auth-body">
    <div class="login-container">
        <div class="login-card">
            <div class="login-header">
                <h2>Note Basement</h2>
                <p>Tạo tài khoản mới</p>
                
                <% if (request.getAttribute("errorMessage") != null) { %>
                    <div class="alert alert-error">
                        <i class="fa-solid fa-circle-exclamation"></i>
                        <span>${errorMessage}</span>
                    </div>
                <% } %>
            </div>

            <form action="${pageContext.request.contextPath}/register" method="POST" id="registerForm">
                
                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="text" id="fullname" name="fullname" required autocomplete="off" value="${oldFullname != null ? oldFullname : ''}">
                        <label for="fullname">Họ và Tên</label>
                        <i class="fa-solid fa-id-card input-icon"></i>
                        <span class="focus-border"></span>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="email" id="email" name="email" required autocomplete="off" value="${oldEmail != null ? oldEmail : ''}">
                        <label for="email">Email</label>
                        <i class="fa-solid fa-envelope input-icon"></i>
                        <span class="focus-border"></span>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="text" id="username" name="username" required autocomplete="off" value="${oldUsername != null ? oldUsername : ''}">
                        <label for="username">Tên đăng nhập</label>
                        <i class="fa-solid fa-user input-icon"></i>
                        <span class="focus-border"></span>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="password" id="password" name="password" required value="${oldPassword != null ? oldPassword : ''}">
                        <label for="password">Mật khẩu</label>
                        <i class="fa-solid fa-lock input-icon"></i>
                        
                        <i class="fa-solid fa-eye toggle-password" id="togglePasswordBtn"></i>
                        
                        <span class="focus-border"></span>
                    </div>
                </div>

                <button type="submit" class="btn login-btn">
                    <span class="btn-text">Đăng ký ngay</span> 
                    <i class="fa-solid fa-user-plus" style="margin-left: 8px;"></i>
                </button>
            </form>

            <div class="signup-link">
                <p>Đã có tài khoản? <a href="${pageContext.request.contextPath}/auth/login.jsp">Đăng nhập</a></p>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/script/script.js"></script>
</body>
</html>