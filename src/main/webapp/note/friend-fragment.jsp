<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<div style="background: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 5px rgba(0,0,0,0.05);">
    <c:if test="${empty friendList}">
        <p>Bạn chưa có người bạn nào.</p>
    </c:if>
    <ul style="list-style: none; padding: 0;">
        <c:forEach var="friend" items="${friendList}">
            <li style="display: flex; align-items: center; padding: 10px 0; border-bottom: 1px solid #eee;">
                <img src="https://ui-avatars.com/api/?name=${friend.fullname}&background=random" style="width: 40px; height: 40px; border-radius: 50%; margin-right: 15px;">
                <span style="font-weight: bold;">${friend.fullname}</span>
            </li>
        </c:forEach>
    </ul>
</div>

</body>
</html>