<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<style>
.animate-fade-in { animation: fadeIn 0.4s ease-out; }
.friend-wrapper { max-width: 1000px; margin: 0 auto; padding-bottom: 50px; }
.section-header { margin-bottom: 20px; border-bottom: 2px solid #f0f2f5; padding-bottom: 10px; display: flex; align-items: center; }
.title-request { color: #d9534f; font-size: 18px; display: flex; align-items: center; gap: 10px; }
.title-sent { color: #6c757d; font-size: 16px; display: flex; align-items: center; gap: 10px; }
.title-friend { color: #333; font-size: 20px; }
.badge { background: #d9534f; color: white; font-size: 12px; padding: 2px 8px; border-radius: 10px; vertical-align: middle; }
.badge.gray { background: #adb5bd; }
.spacer { height: 30px; }
.friend-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 20px; }
.friend-card { background: white; border-radius: 12px; padding: 20px 15px; text-align: center; box-shadow: 0 2px 8px rgba(0,0,0,0.06); transition: transform 0.2s, box-shadow 0.2s; border: 1px solid #f0f0f0; position: relative; display: flex; flex-direction: column; justify-content: space-between; }
.friend-card:hover { transform: translateY(-5px); box-shadow: 0 8px 16px rgba(0,0,0,0.12); }
.friend-avatar img { width: 80px; height: 80px; border-radius: 50%; object-fit: cover; border: 3px solid #fff; box-shadow: 0 2px 5px rgba(0,0,0,0.1); margin-bottom: 10px; }
.friend-avatar.small img { width: 60px; height: 60px; }
.friend-info h4 { font-size: 16px; color: #333; margin: 5px 0; font-weight: 600; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.meta-time { font-size: 12px; color: #999; margin-bottom: 15px; }
.status-waiting { font-size: 12px; color: #f0ad4e; font-style: italic; margin-bottom: 15px; }
.link-wall { font-size: 13px; color: #666; text-decoration: none; display: block; margin-bottom: 15px;}
.friend-actions { display: flex; gap: 5px; justify-content: center; }
.btn-action { flex: 1; padding: 8px 0; border-radius: 6px; border: none; font-size: 13px; font-weight: 500; cursor: pointer; transition: background 0.2s; display: flex; align-items: center; justify-content: center; gap: 5px; text-decoration: none; }
.btn-accept { background: #e6f4ea; color: #1e7e34; }
.btn-accept:hover { background: #28a745; color: white; }
.btn-reject { background: #fbecec; color: #dc3545; }
.btn-reject:hover { background: #dc3545; color: white; }
.btn-view { background: #e3f2fd; color: #0d47a1; }
.btn-view:hover { background: #1976d2; color: white; }
.btn-cancel { background: #f8f9fa; color: #6c757d; font-size: 12px; }
.btn-cancel:hover { background: #e2e6ea; }
.request-card { border: 1px solid #ffeeba; background: #fffdf5; }
.empty-state { padding: 40px; background: #fff; border-radius: 12px; text-align: center; color: #888; border: 2px dashed #eee; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
</style>
<div class="friend-wrapper animate-fade-in">

    <c:if test="${isOwner && not empty strangerList}">
        <div class="section-header">
            <h3 class="title-friend" style="font-size: 18px; color: #007bff;">
                <i class="fa-solid fa-user-plus"></i> Suggested for you
            </h3>
        </div>
        <div class="friend-grid">
            <c:forEach var="stranger" items="${strangerList}">
                <div class="friend-card">
                    <div class="friend-avatar">
                        <img src="https://ui-avatars.com/api/?name=${stranger.fullname}&background=random" alt="Avatar">
                    </div>
                    <div class="friend-info">
                        <h4>${stranger.fullname}</h4>
                        <a href="${pageContext.request.contextPath}/dashboard-note?action=view-wall&_id=${stranger.id}" class="link-wall">@${stranger.username}</a>
                    </div>
                    <div class="friend-actions">
                        <form action="${pageContext.request.contextPath}/dashboard-note" method="POST">
                            <input type="hidden" name="action" value="send-invite">
                            <input type="hidden" name="receiverId" value="${stranger.id}">
                            <button class="btn-action btn-add">
                                <i class="fa-solid fa-user-plus"></i> Add
                            </button>
                        </form>
                        
                        <a href="${pageContext.request.contextPath}/dashboard-note?action=view-wall&_id=${stranger.id}" class="btn-action btn-view">View</a>
                    </div>
                </div>
            </c:forEach>
        </div>
        <div class="spacer"></div>
    </c:if>

    <c:if test="${isOwner && not empty receivedList}">
        <div class="section-header">
            <h3 class="title-request">
                <i class="fa-solid fa-envelope-open-text"></i> Requests <span class="badge">${receivedList.size()}</span>
            </h3>
        </div>
        <div class="friend-grid">
            <c:forEach var="req" items="${receivedList}">
                <div class="friend-card request-card">
                    <div class="friend-avatar">
                        <img src="https://ui-avatars.com/api/?name=${req.userDetails.fullname}&background=random" alt="Avatar">
                    </div>
                    <div class="friend-info">
                        <h4>${req.userDetails.fullname}</h4>
                        <p class="meta-time"><fmt:formatDate value="${req.createdAt}" pattern="dd/MM HH:mm"/></p>
                    </div>
                    <div class="friend-actions">
                        <form action="${pageContext.request.contextPath}/dashboard-note" method="POST">
                            <input type="hidden" name="action" value="accept-invite">
                            <input type="hidden" name="inviteId" value="${req.id}">
                            <button class="btn-action btn-accept">Accept</button>
                        </form>
                        <form action="${pageContext.request.contextPath}/dashboard-note" method="POST">
                            <input type="hidden" name="action" value="cancel-invite">
                            <input type="hidden" name="inviteId" value="${req.id}">
                            <button class="btn-action btn-reject">Reject</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
        <div class="spacer"></div>
    </c:if>

    <c:if test="${isOwner && not empty sentList}">
        <div class="section-header">
            <h3 class="title-sent">
                <i class="fa-solid fa-paper-plane"></i> Sent Requests <span class="badge gray">${sentList.size()}</span>
            </h3>
        </div>
        <div class="friend-grid">
            <c:forEach var="sent" items="${sentList}">
                <div class="friend-card sent-card">
                    <div class="friend-avatar small">
                        <img src="https://ui-avatars.com/api/?name=${sent.userDetails.fullname}&background=random" alt="Avatar">
                    </div>
                    <div class="friend-info">
                        <h4>${sent.userDetails.fullname}</h4>
                        <p class="status-waiting">Waiting...</p>
                    </div>
                    <div class="friend-actions">
                         <form action="${pageContext.request.contextPath}/dashboard-note" method="POST">
                            <input type="hidden" name="action" value="cancel-invite">
                            <input type="hidden" name="inviteId" value="${sent.id}">
                            <button class="btn-action btn-cancel">Cancel</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
        <div class="spacer"></div>
    </c:if>

    <div class="section-header">
        <h2 class="title-friend">
            <i class="fa-solid fa-user-group"></i> My Friends <span class="count">(${friendList.size()})</span>
        </h2>
    </div>

    <c:if test="${empty friendList}">
        <div class="empty-state">
            <p>No friends yet.</p>
        </div>
    </c:if>

    <c:if test="${not empty friendList}">
        <div class="friend-grid">
            <c:forEach var="friend" items="${friendList}">
                <div class="friend-card">
                    <div class="friend-avatar">
                        <img src="https://ui-avatars.com/api/?name=${friend.fullname}&background=random" alt="Avatar">
                    </div>
                    <div class="friend-info">
                        <h4>${friend.fullname}</h4>
                        <a href="${pageContext.request.contextPath}/dashboard-note?action=view-wall&_id=${friend.id}" class="link-wall">@${friend.username}</a>
                    </div>
                    <div class="friend-actions">
                        <a href="${pageContext.request.contextPath}/dashboard-note?action=view-wall&_id=${friend.id}" class="btn-action btn-view">See friend wall</a>
                        <c:if test="${isOwner}">
                            <form action="${pageContext.request.contextPath}/dashboard-note" method="POST" onsubmit="return confirm('Endding this love :(( ${friend.fullname}?');">
                                <input type="hidden" name="action" value="unfriend"> <input type="hidden" name="friendId" value="${friend.id}">
                                <button class="btn-action btn-reject" title="Get away from me!!!"><i class="fa-solid fa-user-xmark"></i></button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${empty friendList}">
        
        <div class="empty-state" style="margin-bottom: 30px; border: none; padding-bottom: 0;">
            <p style="font-size: 15px; color: #555;">You don't have any friend. Let's make a new friend!</p>
        </div>

        <div class="section-header" style="border: none; margin-top: 0;">
            <h3 style="font-size: 16px; color: #007bff; text-transform: uppercase; letter-spacing: 1px;">
                <i class="fa-solid fa-wand-magic-sparkles"></i> Suggest for you only.
            </h3>
        </div>

        <div class="friend-grid">
            <c:forEach var="stranger" items="${suggestionList}">
                <div class="friend-card" style="border-top: 3px solid #007bff;">
                    <div class="friend-avatar">
                        <img src="https://ui-avatars.com/api/?name=${stranger.fullname}&background=random" alt="Avatar">
                    </div>
                    <div class="friend-info">
                        <h4>${stranger.fullname}</h4>
                        <span style="font-size: 12px; color: #888;">Maybe you know</span>
                    </div>
                    <div class="friend-actions">
                        <form action="${pageContext.request.contextPath}/dashboard-note" method="POST" style="width: 100%;">
                            <input type="hidden" name="action" value="send-invite">
                            <input type="hidden" name="receiverId" value="${stranger.id}">
                            <button type="submit" class="btn-action btn-accept" style="background: #007bff; color: white;">
                                <i class="fa-solid fa-user-plus"></i> Add friend
                            </button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
        
        <c:if test="${empty suggestionList}">
             <div class="empty-state">
                <p>No one in here.</p>
            </div>
        </c:if>
    </c:if>
</div>