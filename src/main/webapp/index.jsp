<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%
    // Lấy thông tin người dùng từ Session
    User user = (User) session.getAttribute("currentUser");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Note Basement</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
</head>
<body>
    <div class="dashboard-container">
        <header class="header">
            <div class="logo">Note Basement</div>
            <div class="user-info">
                <span>Xin chào, <strong><%= user.getFullname() %></strong></span>
                <form action="${pageContext.request.contextPath}/logout" method="GET" style="display:inline;">
                    <button type="submit" class="btn-logout">Đăng xuất</button>
                </form>
            </div>
        </header>

        <main class="content">
            <div class="welcome-card">
                <h3>Chúc bạn một ngày tốt lành!</h3>
                <p>Đây là căn hầm bí mật cho những ghi chú của bạn.</p>
                <button class="btn-create">+ Tạo ghi chú mới</button>
            </div>

            <div class="notes-grid" id="notesGrid">
                <div class="note-item">
                    <h4>Ghi chú mẫu</h4>
                    <p>Nội dung ghi chú của bạn sẽ hiển thị tại đây...</p>
                </div>
            </div>
        </main>
    </div>
</body>
</html>