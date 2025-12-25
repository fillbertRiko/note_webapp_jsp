<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to Note Basement</title>
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/script/script.js"></script>
</head>
<body class="landing-body">
    <div class="landing-container">
        <div class="welcome-card animate-up">
            <div class="icon-box">
                <i class="fa-solid fa-vault"></i>
            </div>
            
            <h3>Note Basement</h3>
            <p>Chào mừng đến với căn hầm bí mật.<br>Nơi an toàn nhất cho những ghi chú của bạn.</p>
            
            <div class="btn-group">
                <a href="${pageContext.request.contextPath}/auth/login.jsp" class="btn btn-login">
                    Đăng nhập
                </a>
                <a href="${pageContext.request.contextPath}/auth/register.jsp" class="btn btn-register">
                    Đăng ký ngay
                </a>
            </div>
        </div>
    </div>
</body>
</html>