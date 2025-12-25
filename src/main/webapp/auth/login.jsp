<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Xóa cache để đảm bảo an toàn khi back lại
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
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
    
    <style>
        /* Icon trong input */
        .input-icon {
            position: absolute;
            left: 0;
            top: 15px;
            color: rgba(255,255,255,0.6);
            transition: 0.3s;
        }
        
        /* Chỉnh lại padding input để tránh đè lên icon */
        .input-wrapper input {
            padding-left: 30px; 
            padding-right: 35px; /* Chỗ cho nút hiện pass */
        }
        
        .input-wrapper label {
            left: 30px; /* Label cũng phải dịch sang phải */
        }
        
        /* Hiệu ứng focus: Icon sáng lên */
        .input-wrapper input:focus ~ .input-icon,
        .input-wrapper input:valid ~ .input-icon {
            color: var(--secondary-color);
        }

        /* Nút hiện mật khẩu */
        .toggle-password {
            position: absolute;
            right: 0;
            top: 15px;
            color: rgba(255,255,255,0.5);
            cursor: pointer;
            transition: 0.3s;
        }
        .toggle-password:hover { color: white; }

        /* Style cho thông báo (Alerts) */
        .alert {
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 14px;
            display: flex;
            align-items: center;
            gap: 10px;
            animation: fadeIn 0.5s ease;
        }
        
        .alert-success {
            background: rgba(74, 222, 128, 0.2);
            border: 1px solid rgba(74, 222, 128, 0.4);
            color: #4ade80;
        }
        
        .alert-error {
            background: rgba(255, 77, 77, 0.2);
            border: 1px solid rgba(255, 77, 77, 0.4);
            color: #ff4d4d;
        }

        /* Layout cho phần Remember & Forgot Pass */
        .form-options {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 20px;
            font-size: 14px;
            color: rgba(255,255,255,0.8);
        }

        .form-options a {
            color: white;
            text-decoration: none;
            transition: 0.3s;
        }
        .form-options a:hover { color: var(--secondary-color); text-decoration: underline; }

        /* Custom Checkbox */
        .remember-wrapper {
            display: flex;
            align-items: center;
            gap: 8px;
            cursor: pointer;
        }
        .remember-wrapper input { accent-color: var(--primary-color); cursor: pointer; }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-10px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
</head>
<body class="auth-body">
    <div class="login-container">
        <div class="login-card">
            <div class="login-header">
                <h2>Note Basement</h2>
                <p>Chào mừng trở lại!</p>
            </div>

            <% if ("register_success".equals(request.getParameter("msg"))) { %>
                <div class="alert alert-success">
                    <i class="fa-solid fa-circle-check"></i>
                    <span>Đăng ký thành công! Hãy đăng nhập.</span>
                </div>
            <% } %>

            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-error">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    <span>${errorMessage}</span>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm">
                
                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="text" id="username" name="username" required autocomplete="off">
                        <label for="username">Tài khoản</label>
                        <i class="fa-solid fa-user input-icon"></i> <span class="focus-border"></span>
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-wrapper">
                        <input type="password" id="password" name="password" required>
                        <label for="password">Mật khẩu</label>
                        <i class="fa-solid fa-lock input-icon"></i> <i class="fa-solid fa-eye toggle-password" onclick="togglePassword()"></i>
                        
                        <span class="focus-border"></span>
                    </div>
                </div>

                <div class="form-options">
                    <label class="remember-wrapper">
                        <input type="checkbox" name="remember">
                        <span>Ghi nhớ đăng nhập</span>
                    </label>
                    <a href="${pageContext.request.contextPath}/auth/forgotPassword.jsp" class="forgot-password">Quên mật khẩu?</a>
                </div>

                <button type="submit" class="btn login-btn">
                    Đăng Nhập <i class="fa-solid fa-arrow-right" style="margin-left: 8px;"></i>
                </button>
            </form>

            <div class="signup-link">
                <p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/auth/register.jsp">Đăng ký ngay</a></p>
            </div>
        </div>
    </div>

    <script>
        // 1. Logic Floating Label (Nhãn bay)
        function checkInput() {
            document.querySelectorAll('.input-wrapper input').forEach(input => {
                // Nếu input có giá trị -> thêm class has-value để label bay lên
                if (input.value && input.value.trim() !== "") {
                    input.classList.add('has-value');
                } else {
                    input.classList.remove('has-value');
                }
            });
        }

        // Gán sự kiện
        document.querySelectorAll('.input-wrapper input').forEach(input => {
            input.addEventListener('blur', checkInput);
            input.addEventListener('input', checkInput);
        });
        
        // Chạy ngay khi load trang (phòng trường hợp trình duyệt tự điền)
        window.onload = checkInput;

        // 2. Logic Ẩn/Hiện Mật khẩu
        function togglePassword() {
            const passwordInput = document.getElementById('password');
            const toggleIcon = document.querySelector('.toggle-password');
            
            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                toggleIcon.classList.remove('fa-eye');
                toggleIcon.classList.add('fa-eye-slash'); // Đổi icon thành mắt gạch chéo
            } else {
                passwordInput.type = 'password';
                toggleIcon.classList.remove('fa-eye-slash');
                toggleIcon.classList.add('fa-eye');
            }
        }
    </script>
</body>
</html>