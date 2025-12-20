<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Note Basement</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
    <script src="${pageContext.request.contextPath}/script/script.jsp"></script>
</head>
<body>
    <div class="dashboard-container">

        <main class="content">
            <div class="welcome-card">
                <h3>Chúc bạn một ngày tốt lành!</h3>
                <p>Đây là căn hầm bí mật cho những ghi chú của bạn.</p>
                <a href="${pageContext.request.contextPath}/auth/login.jsp" class="login">Sign in</a>
                <a href="${pageContext.request.contextPath}/auth/register.jsp" class="register">Sign Up</a>
            </div>
        </main>
    </div>
</body>
</html>