<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
<script src="${pageContext.request.contextPath}/script/script.jsp"></script>
<title>Insert title here</title>
</head>
<body class="dashboard-body">
    <div class="dashboard-wrapper">
        <aside class="sidebar">
            <div class="logo-section">
                <i class="fa-solid fa-vault"></i>
                <span>Note Basement</span>
            </div>
            
            <nav class="menu">
                <a href="#" class="menu-item active"><i class="fa-solid fa-note-sticky"></i> Ghi chú</a>
                <a href="#" class="menu-item"><i class="fa-solid fa-star"></i> Quan trọng</a>
                <a href="#" class="menu-item"><i class="fa-solid fa-trash"></i> Thùng rác</a>
            </nav>

            <div class="user-profile">
                <div class="user-avatar">
                    <img src="https://ui-avatars.com/api/?name=${currentUser.fullname}&background=random" alt="User">
                </div>
                <div class="user-info">
                    <p class="user-name">${currentUser.fullname}</p>
                    <form action="${pageContext.request.contextPath}/logout" method="GET">
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
                    <input type="text" placeholder="Tìm kiếm ghi chú...">
                </div>
                <button class="btn-create" onclick="openModal()">
                    <i class="fa-solid fa-plus"></i> Tạo ghi chú
                </button>
            </header>

            <div class="notes-grid">
                <div class="note-card">
                    <div class="note-header">
                        <span class="note-date">18 Th12, 2025</span>
                        <i class="fa-regular fa-star"></i>
                    </div>
                    <h3 class="note-title">Học lập trình Java Servlet</h3>
                    <p class="note-excerpt">Hôm nay học về AuthFilter và cách kết nối MongoDB với dự án Note...</p>
                    <div class="note-footer">
                        <span class="tag">Học tập</span>
                        <div class="note-actions">
                            <button title="Sửa"><i class="fa-solid fa-pen-to-square"></i></button>
                            <button title="Xóa"><i class="fa-solid fa-trash"></i></button>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</body>
</html>