<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    // --- [ĐOẠN MỚI THÊM VÀO] ---
    // Kiểm tra xem trang này có được gọi từ Controller hay không
    // Nếu biến "wallOwner" bị null (nghĩa là chưa qua Controller xử lý) -> Chuyển hướng ngay về Controller
    if (request.getAttribute("wallOwner") == null) {
        response.sendRedirect(request.getContextPath() + "/dashboard-note");
        return;
    }
    // ----------------------------
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Note Basement | Dashboard</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="dashboard-body">
    <div class="dashboard-wrapper">
        <aside class="sidebar">
            <div class="logo-section">
                <i class="fa-solid fa-vault"></i>
                <span>Note Basement</span>
            </div>
            
            <nav class="menu">
                <a href="#" onclick="switchSection('wall', this); return false;" class="menu-item active">
			        <i class="fa-solid fa-note-sticky"></i> Note
			    </a>
			    
                <a href="#" onclick="switchSection('friend', this); return false;" class="menu-item">
			        <i class="fa-solid fa-user-group"></i> List friends
			    </a>

                <a href="${pageContext.request.contextPath}/schedule?action=view" class="menu-item">
                    <i class="fa-solid fa-calendar-days"></i> Schedule
                </a>
			    
                <a href="#" onclick="switchSection('info', this); return false;" class="menu-item">
			        <i class="fa-solid fa-circle-info"></i> Information
			    </a>
			</nav>

            <div class="user-profile">
                <div class="user-avatar">
                    <img src="https://ui-avatars.com/api/?name=${currentUser.fullname}&background=random" alt="User">
                </div>
                <div class="user-info">
                    <p class="user-name">${currentUser.fullname}</p>
                    <form action="${pageContext.request.contextPath}/logout" method="GET" style="margin:0;">
                        <button type="submit" class="logout-link">
                            <i class="fa-solid fa-right-from-bracket"></i> Logout
                        </button>
                    </form>
                </div>
            </div>
        </aside>

        <main class="main-content">
            <header class="top-bar">
                <div class="search-box">
                    <i class="fa-solid fa-magnifying-glass"></i>
                    <input type="text" 
                           id="searchInput" 
                           placeholder="Search notes or friends..." 
                           oninput="handleSearch()">
                </div>
                
                <button class="btn-create" onclick="toggleModal('addNoteModal', true)">
                    <i class="fa-solid fa-plus"></i> Create new note
                </button>
            </header>

            <div id="dynamic-content">
                <jsp:include page="wall-fragment.jsp"></jsp:include>
            </div>
        </main>
    </div>

    <div id="addNoteModal" class="modal">
        <div class="modal-overlay" onclick="toggleModal('addNoteModal', false)"></div>
        <form class="modal-content animate" action="${pageContext.request.contextPath}/dashboard-note" method="POST">
            <input type="hidden" name="action" value="create">

            <div class="modal-header">
                <h3>Create new note</h3>
                <span class="close-btn" onclick="toggleModal('addNoteModal', false)">&times;</span>
            </div>

            <div class="modal-body">
                <div class="form-group">
                    <label>Title</label>
                    <input type="text" name="title" required placeholder="Enter new title...">
                </div>

                <div class="form-group">
                    <label>Content</label>
                    <textarea name="content" rows="5" required placeholder="Write something..."></textarea>
                </div>

                <input type="hidden" name="topicId" value="general">

                <div class="form-row">
                    <div class="form-group half">
                        <label>Level access</label>
                        <select name="accessLevelId" id="accessLevelSelect" onchange="toggleFriendList()">
                            <option value="PUBLIC">Public</option>
                            <option value="PROTECTED_1">Choose friend in your list only</option>
                            <option value="PROTECTED_2">All your friend only</option>
                            <option value="PRIVATE">Just me</option>
                        </select>
                    </div>
                    
                    <div class="form-group half">
                        <label>Allow comment?</label>
                        <select name="allowComment">
                            <option value="1">Yes</option>
                            <option value="0">No</option>
                        </select>
                    </div>
                </div>

                <div id="friendSelectSection" class="form-group hidden">
                    <label>Choose friend can see:</label>
                    <div class="friend-list-box">
                        <c:if test="${not empty friendList}">
                            <c:forEach var="friend" items="${friendList}">
                                <label class="checkbox-item">
                                    <input type="checkbox" name="allowViewer" value="${friend.id}"> ${friend.fullname}
                                </label>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty friendList}">
                            <p style="font-size: 12px; color: #666; font-style: italic;">You don't have any friend.</p>
                        </c:if>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="toggleModal('addNoteModal', false)">Cancel</button>
                <button type="submit" class="btn-submit">Posting</button>
            </div>
        </form>
    </div>
    
    <div id="editNoteModal" class="modal">
        <div class="modal-overlay" onclick="toggleModal('editNoteModal', false)"></div>
        <form class="modal-content animate" action="${pageContext.request.contextPath}/dashboard-note" method="POST">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="_id" id="edit_id"> 

            <div class="modal-header">
                <h3>Edit note</h3>
                <span class="close-btn" onclick="toggleModal('editNoteModal', false)">&times;</span>
            </div>

            <div class="modal-body">
                <div class="form-group">
                    <label>Title</label>
                    <input type="text" name="title" id="edit_title" required>
                </div>

                <div class="form-group">
                    <label>Content</label>
                    <textarea name="content" id="edit_content" rows="5" required></textarea>
                </div>

                <input type="hidden" name="topicId" id="edit_topicId" value="general">

                <div class="form-row">
                    <div class="form-group half">
                        <label>Level access</label>
                        <select name="accessLevelId" id="edit_accessLevelId" onchange="toggleEditFriendList()">
                            <option value="PUBLIC">Public</option>
                            <option value="PROTECTED_1">Choose your friend on list</option>
                            <option value="PROTECTED_2">All your friends</option>
                            <option value="PRIVATE">Only me</option>
                        </select>
                    </div>
                    
                    <div class="form-group half">
                        <label>Allow comment?</label>
                        <select name="allowComment" id="edit_allowComment">
                            <option value="1">Yes</option>
                            <option value="0">No</option>
                        </select>
                    </div>
                </div>
                
                <div id="edit_friendSelectSection" class="form-group hidden">
                    <p style="font-size: 12px; color: #666; font-style: italic; margin-bottom: 5px;">
                        * Update list friends:
                    </p>
                    <div class="friend-list-box">
                         <c:if test="${not empty friendList}">
                            <c:forEach var="friend" items="${friendList}">
                                <label class="checkbox-item">
                                    <input type="checkbox" name="allowViewer" value="${friend.id}"> ${friend.fullname}
                                </label>
                            </c:forEach>
                        </c:if>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="toggleModal('editNoteModal', false)">Cancel</button>
                <button type="submit" class="btn-submit">Save changed</button>
            </div>
        </form>
    </div>

   <script>
        const contextPath = "${pageContext.request.contextPath}";
        const serverOwnerId = "${not empty ownerId ? ownerId : currentUser.id}";
    </script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/script/script.js"></script>
</body>
</html>