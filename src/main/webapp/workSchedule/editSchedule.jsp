<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa Lịch trình</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
    
    <style>
        /* CSS căn giữa khung edit ra giữa màn hình */
        .edit-container {
            display: flex; justify-content: center; align-items: center; min-height: 100vh;
        }
        /* Khung chứa form */
        .edit-card {
            background: white; width: 100%; max-width: 600px; padding: 30px; border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
        }
        /* CSS cho các input */
        .form-group label { color: #333; font-weight: bold; display: block; margin-bottom: 5px; margin-top: 15px; }
        .form-group input, .form-group textarea, .form-group select {
            width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 5px; color: #333;
        }
    </style>
</head>
<body class="dashboard-body">
    <div class="edit-container">
        <div class="edit-card animate-up">
            <h2 style="text-align: center; color: #333; margin-bottom: 20px;">Cập nhật công việc</h2>
            
            <form action="${pageContext.request.contextPath}/schedule" method="POST">
                
                <input type="hidden" name="action" value="update">
                
                <input type="hidden" name="id" value="${schedule.id}">
                
                <div class="form-group">
                    <label>Tiêu đề</label>
                    <input type="text" name="subject" value="${schedule.subject}" required>
                </div>

                <div style="display: flex; gap: 20px;">
                    <div class="form-group" style="flex:1">
                        <label>Độ ưu tiên</label>
                        <select name="priority">
                            <option value="LOW" ${schedule.priority == 'LOW' ? 'selected' : ''}>Thấp</option>
                            <option value="MEDIUM" ${schedule.priority == 'MEDIUM' ? 'selected' : ''}>Bình thường</option>
                            <option value="HIGH" ${schedule.priority == 'HIGH' ? 'selected' : ''}>Cao</option>
                        </select>
                    </div>
                    
                    <div class="form-group" style="flex:1">
                         <label>Địa điểm</label>
                         <input type="text" name="location" value="${schedule.location}">
                    </div>
                </div>

                <div style="display: flex; gap: 20px;">
                    <div class="form-group" style="flex:1">
                        <label>Bắt đầu (Cũ: <fmt:formatDate value="${schedule.startTime}" pattern="dd/MM HH:mm"/>)</label>
                        <input type="datetime-local" name="startTime" required>
                    </div>
                    
                    <div class="form-group" style="flex:1">
                        <label>Kết thúc (Cũ: <fmt:formatDate value="${schedule.endTime}" pattern="dd/MM HH:mm"/>)</label>
                        <input type="datetime-local" name="endTime" required>
                    </div>
                </div>

                <div class="form-group">
                    <label>Mô tả</label>
                    <textarea name="description" rows="4">${schedule.description}</textarea>
                </div>

                <div style="margin-top: 30px; display: flex; gap: 10px;">
                    <a href="${pageContext.request.contextPath}/schedule?action=view" class="btn-cancel" style="text-decoration: none; text-align: center;">Hủy</a>
                    <button type="submit" class="btn-submit" style="width: 100%;">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>