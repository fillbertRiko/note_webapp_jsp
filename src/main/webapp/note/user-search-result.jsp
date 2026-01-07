<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .friend-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 20px; }
    .friend-card { background: white; border-radius: 12px; padding: 20px 15px; text-align: center; box-shadow: 0 2px 8px rgba(0,0,0,0.06); border: 1px solid #f0f0f0; display: flex; flex-direction: column; justify-content: space-between; }
    .friend-avatar img { width: 80px; height: 80px; border-radius: 50%; object-fit: cover; border: 3px solid #fff; box-shadow: 0 2px 5px rgba(0,0,0,0.1); margin-bottom: 10px; }
    .friend-info h4 { font-size: 16px; color: #333; margin: 5px 0; font-weight: 600; }
    .friend-actions { display: flex; gap: 5px; justify-content: center; margin-top: 10px; }
    .btn-action { flex: 1; padding: 8px 0; border-radius: 6px; border: none; font-size: 13px; font-weight: 500; cursor: pointer; display: flex; align-items: center; justify-content: center; gap: 5px; text-decoration: none; }
    .btn-view { background: #e3f2fd; color: #0d47a1; }
    .btn-accept { background: #e6f4ea; color: #1e7e34; }
    .btn-cancel { background: #f8f9fa; color: #6c757d; }
</style>

<div class="animate-fade-in" style="padding: 20px;">
    <h3 style="margin-bottom: 20px; color: #555;">Search Result: "${param.search}"</h3>
    
    <div class="friend-grid">
        <c:forEach var="user" items="${userList}">
            <div class="friend-card">
                <div class="friend-avatar">
                    <img src="https://ui-avatars.com/api/?name=${user.fullname}&background=random" alt="Avatar">
                </div>
                <div class="friend-info">
                    <h4>${user.fullname}</h4>
                    <p style="color: #888; font-size: 12px;">@${user.username}</p>
                </div>
                
                <div class="friend-actions">
                    <c:set var="status" value="${relationshipMap[user.id]}" />
                    
                    <c:choose>
                        <c:when test="${status == 'SELF'}"><span style="color:#999; font-size:12px;">(You)</span></c:when>
                        
                        <c:when test="${status == 'FRIEND'}">
                            <button class="btn-action btn-accept" disabled><i class="fa-solid fa-check"></i> Friend</button>
                        </c:when>
                        
                        <c:when test="${status == 'SENT_REQUEST'}">
                            <button class="btn-action btn-cancel" disabled>Sent</button>
                        </c:when>
                        
                        <c:when test="${status == 'RECEIVED_REQUEST'}">
                            <a href="${pageContext.request.contextPath}/dashboard-note?action=load-section&section=friend" class="btn-action btn-view">Check Invite</a>
                        </c:when>

                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/dashboard-note" method="POST" style="width:100%">
                                <input type="hidden" name="action" value="send-invite">
                                <input type="hidden" name="receiverId" value="${user.id}">
                                <button class="btn-action btn-view"><i class="fa-solid fa-user-plus"></i> Add Friend</button>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:forEach>
        
        <c:if test="${empty userList}">
            <p style="color:#999; text-align:center;">No users found.</p>
        </c:if>
    </div>
</div>