<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<div class="friend-wrapper">

    <c:if test="${isOwner && not empty receivedList}">
        <div class="section-header" style="margin-top: 0;">
            <h3 style="color: #d9534f;"><i class="fa-solid fa-envelope-open-text"></i> Lời mời kết bạn (${receivedList.size()})</h3>
        </div>
        <div class="friend-grid">
            <c:forEach var="req" items="${receivedList}">
                <div class="friend-card request-card">
                    <div class="friend-avatar">
                        <img src="https://ui-avatars.com/api/?name=${req.user.fullname}&background=random" alt="Avatar">
                    </div>
                    <div class="friend-info">
                        <h4>${req.user.fullname}</h4>
                        <p style="font-size: 11px; color: #999;">
                            Gửi lúc: <fmt:formatDate value="${req.sentAt}" pattern="dd/MM HH:mm"/>
                        </p>
                    </div>
                    <div class="friend-actions">
                        <form action="${pageContext.request.contextPath}/dashboard-note" method="POST" style="display:inline;">
                            <input type="hidden" name="action" value="accept-invite">
                            <input type="hidden" name="inviteId" value="${req.inviteId}">
                            <button type="submit" class="btn-visit" style="background:#28a745; color:white;">Đồng ý</button>
                        </form>
                        
                        <form action="${pageContext.request.contextPath}/dashboard-note" method="POST" style="display:inline;">
                            <input type="hidden" name="action" value="cancel-invite">
                            <input type="hidden" name="inviteId" value="${req.inviteId}">
                            <button type="submit" class="btn-visit" style="background:#dc3545; color:white;">Xóa</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
        <hr style="margin: 30px 0; border-top: 1px dashed #ccc;">
    </c:if>

    <div class="section-header">
        <h2>Danh sách bạn bè (${friendList.size()})</h2>
    </div>
    
    <c:if test="${empty friendList}">
        <p style="text-align: center; color: #999; font-style: italic;">Chưa có bạn bè nào.</p>
    </c:if>

    <div class="friend-grid">
        <c:forEach var="friend" items="${friendList}">
            <div class="friend-card">
                <div class="friend-avatar">
                    <img src="https://ui-avatars.com/api/?name=${friend.fullname}&background=random" alt="Avatar">
                </div>
                <div class="friend-info">
                    <h4>${friend.fullname}</h4>
                    <a href="${pageContext.request.contextPath}/dashboard-note?action=view-wall&id=${friend.id}" class="btn-visit">Xem tường</a>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${isOwner && not empty sentList}">
        <div class="section-header" style="margin-top: 40px;">
            <h4 style="color: #777;"><i class="fa-solid fa-paper-plane"></i> Đã gửi (${sentList.size()})</h4>
        </div>
        <div class="friend-grid">
            <c:forEach var="sent" items="${sentList}">
                <div class="friend-card" style="opacity: 0.8;">
                    <div class="friend-avatar">
                         <img src="https://ui-avatars.com/api/?name=${sent.user.fullname}&background=random" alt="Avatar">
                    </div>
                    <div class="friend-info">
                        <h4>${sent.user.fullname}</h4>
                        <span style="font-size: 11px; background: #eee; padding: 2px 5px; border-radius: 4px;">Đang chờ...</span>
                    </div>
                    <div class="friend-actions">
                         <form action="${pageContext.request.contextPath}/dashboard-note" method="POST">
                            <input type="hidden" name="action" value="cancel-invite">
                            <input type="hidden" name="inviteId" value="${sent.inviteId}">
                            <button type="submit" class="btn-visit" style="background:#6c757d; color:white; font-size: 11px;">Hủy lời mời</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>
</div>
</body>
</html>