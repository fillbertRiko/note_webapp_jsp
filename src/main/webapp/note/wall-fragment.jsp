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
	<c:if test="${not empty wallOwner}">
                <div class="wall-info" style="margin: 20px 0; padding: 20px; background: white; border-radius: 10px; display: flex; align-items: center; gap: 15px; box-shadow: 0 2px 5px rgba(0,0,0,0.05);">
                    <div class="wall-avatar" style="width: 60px; height: 60px; border-radius: 50%; overflow: hidden;">
                        <img src="https://ui-avatars.com/api/?name=${wallOwner.fullname}&background=random&size=128" 
                             alt="${wallOwner.fullname}" 
                             style="width: 100%; height: 100%; object-fit: cover;">
                    </div>
                    
                    <div class="wall-details">
                        <h2 style="margin: 0; font-size: 1.5rem; color: #333;">
                            ${wallOwner.fullname}
                            <c:if test="${isMyWall}">
                                <span style="font-size: 0.8rem; color: #888; font-weight: normal;">(You)</span>
                            </c:if>
                        </h2>
                        <p style="margin: 5px 0 0; color: #666; font-size: 0.9rem;">
                            <i class="fa-regular fa-envelope"></i> ${wallOwner.email}
                        </p>
                    </div>
                    
                    <c:if test="${!isMyWall}">
                        <div class="wall-actions" style="margin-left: auto;">
                            <button style="padding: 8px 15px; background: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer;">
                                <i class="fa-solid fa-user-plus"></i> Add Friend
                            </button>
                        </div>
                    </c:if>
                </div>
            </c:if>
            <div class="notes-grid">
    <jsp:include page="post-list-fragment.jsp"></jsp:include>
</div>
</body>
</html>