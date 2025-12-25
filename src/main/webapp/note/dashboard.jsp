<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
                <a href="${pageContext.request.contextPath}/dashboard-note?action=view-wall" class="menu-item active">
                    <i class="fa-solid fa-note-sticky"></i> Ghi chú
                </a>
                <a href="#" class="menu-item"><i class="fa-solid fa-star"></i> Quan trọng</a>
                <a href="#" class="menu-item"><i class="fa-solid fa-trash"></i> Thùng rác</a>
            </nav>

            <div class="user-profile">
                <div class="user-avatar">
                    <img src="https://ui-avatars.com/api/?name=${currentUser.fullname}&background=random" alt="User">
                </div>
                <div class="user-info">
                    <p class="user-name">${currentUser.fullname}</p>
                    <form action="${pageContext.request.contextPath}/logout" method="GET" style="margin:0;">
                        <button type="submit" class="logout-link">
                            <i class="fa-solid fa-right-from-bracket"></i> Đăng xuất
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
				           placeholder="Tìm kiếm ghi chú..." 
				           oninput="handleSearch()">
				</div>
                
                <button class="btn-create" onclick="toggleModal('addNoteModal', true)">
                    <i class="fa-solid fa-plus"></i> Tạo ghi chú mới
                </button>
            </header>

            <div class="notes-grid">
                <jsp:include page="post-list-fragment.jsp"></jsp:include>
            </div>
        </main>
    </div>

    <div id="addNoteModal" class="modal">
        <div class="modal-overlay" onclick="toggleModal('addNoteModal', false)"></div>
        
        <form class="modal-content animate" action="${pageContext.request.contextPath}/dashboard-note" method="POST">
            <input type="hidden" name="action" value="create">

            <div class="modal-header">
                <h3>Tạo ghi chú mới</h3>
                <span class="close-btn" onclick="toggleModal('addNoteModal', false)">&times;</span>
            </div>

            <div class="modal-body">
                <div class="form-group">
                    <label>Tiêu đề</label>
                    <input type="text" name="title" required placeholder="Nhập tiêu đề...">
                </div>

                <div class="form-group">
                    <label>Nội dung</label>
                    <textarea name="content" rows="5" required placeholder="Viết gì đó..."></textarea>
                </div>

                <input type="hidden" name="topicId" value="general">

                <div class="form-row">
                    <div class="form-group half">
                        <label>Chế độ hiển thị</label>
                        <select name="accessLevelId" id="accessLevelSelect" onchange="toggleFriendList()">
                            <option value="PUBLIC">Công khai (Public)</option>
                            <option value="PROTECTED_1">Bạn bè cụ thể (Protected 1)</option>
                            <option value="PROTECTED_2">Tất cả bạn bè (Protected 2)</option>
                            <option value="PRIVATE">Chỉ mình tôi (Private)</option>
                        </select>
                    </div>
                    
                    <div class="form-group half">
                        <label>Cho phép bình luận?</label>
                        <select name="allowComment">
                            <option value="1">Có</option>
                            <option value="0">Không</option>
                        </select>
                    </div>
                </div>

                <div id="friendSelectSection" class="form-group hidden">
                    <label>Chọn bạn bè được xem:</label>
                    <div class="friend-list-box">
                        <c:if test="${not empty friendList}">
                            <c:forEach var="friend" items="${friendList}">
                                <label class="checkbox-item">
                                    <input type="checkbox" name="allowViewer" value="${friend.id}"> ${friend.fullname}
                                </label>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty friendList}">
                            <p style="font-size: 12px; color: #666; font-style: italic;">Bạn chưa có danh sách bạn bè.</p>
                        </c:if>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="toggleModal('addNoteModal', false)">Hủy</button>
                <button type="submit" class="btn-submit">Đăng bài</button>
            </div>
        </form>
    </div>
    
    <div id="editNoteModal" class="modal">
        <div class="modal-overlay" onclick="toggleModal('editNoteModal', false)"></div>
        
        <form class="modal-content animate" action="${pageContext.request.contextPath}/dashboard-note" method="POST">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="_id" id="edit_id"> <div class="modal-header">
                <h3>Chỉnh sửa ghi chú</h3>
                <span class="close-btn" onclick="toggleModal('editNoteModal', false)">&times;</span>
            </div>

            <div class="modal-body">
                <div class="form-group">
                    <label>Tiêu đề</label>
                    <input type="text" name="title" id="edit_title" required>
                </div>

                <div class="form-group">
                    <label>Nội dung</label>
                    <textarea name="content" id="edit_content" rows="5" required></textarea>
                </div>

                <input type="hidden" name="topicId" id="edit_topicId" value="general">

                <div class="form-row">
                    <div class="form-group half">
                        <label>Chế độ hiển thị</label>
                        <select name="accessLevelId" id="edit_accessLevelId" onchange="toggleEditFriendList()">
                            <option value="PUBLIC">Công khai (Public)</option>
                            <option value="PROTECTED_1">Bạn bè cụ thể (Protected 1)</option>
                            <option value="PROTECTED_2">Tất cả bạn bè (Protected 2)</option>
                            <option value="PRIVATE">Chỉ mình tôi (Private)</option>
                        </select>
                    </div>
                    
                    <div class="form-group half">
                        <label>Cho phép bình luận?</label>
                        <select name="allowComment" id="edit_allowComment">
                            <option value="1">Có</option>
                            <option value="0">Không</option>
                        </select>
                    </div>
                </div>
                
                <div id="edit_friendSelectSection" class="form-group hidden">
                    <p style="font-size: 12px; color: #666; font-style: italic; margin-bottom: 5px;">
                        * Cập nhật danh sách bạn bè được xem:
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
                <button type="button" class="btn-cancel" onclick="toggleModal('editNoteModal', false)">Hủy</button>
                <button type="submit" class="btn-submit">Lưu thay đổi</button>
            </div>
        </form>
    </div>

   <script>
        // 1. Biến Context Path cho JS gọi Controller
        const contextPath = "${pageContext.request.contextPath}";
        
        // 2. [QUAN TRỌNG] Biến serverOwnerId để JS biết tải bài của ai khi gọi AJAX
        // Nếu biến ownerId từ Controller trả về null -> lấy id của currentUser
        const serverOwnerId = "${not empty ownerId ? ownerId : currentUser.id}";
    </script>

    <script type="text/javascript" src="${pageContext.request.contextPath}/script/script.js"></script>
</body>
</html>