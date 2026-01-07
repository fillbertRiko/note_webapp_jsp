<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý Lịch trình</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        /* CSS GỐC GIỮ NGUYÊN */
        .schedule-container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        .schedule-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; color: white; }
        .schedule-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; }
        
        .schedule-card {
            background: white; border-radius: 15px; padding: 20px; position: relative;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1); border-left: 5px solid #ccc; transition: transform 0.2s;
        }
        .schedule-card:hover { transform: translateY(-5px); }
        
        .priority-HIGH { border-left-color: #ff4d4d; }
        .priority-MEDIUM { border-left-color: #ffca28; }
        .priority-LOW { border-left-color: #4ade80; }

        .time-badge { background: #f0f2f5; color: #555; font-size: 12px; padding: 5px 10px; border-radius: 20px; display: inline-block; margin-bottom: 10px; font-weight: bold; }
        .schedule-title { font-size: 18px; font-weight: bold; margin-bottom: 10px; color: #333; }
        .schedule-desc { color: #666; font-size: 14px; margin-bottom: 15px; min-height: 40px;}
        .schedule-meta { font-size: 13px; color: #888; display: flex; gap: 15px; }
        
        .card-actions { position: absolute; top: 15px; right: 15px; display: flex; gap: 10px; }
        .btn-icon { border: none; background: none; cursor: pointer; font-size: 14px; transition: 0.2s; }
        .btn-edit-sch { color: #007bff; }
        .btn-del-sch { color: #dc3545; }
        
        .form-row { display: flex; gap: 15px; margin-bottom: 15px; }
        .form-col { flex: 1; }
        .form-col label { display: block; margin-bottom: 5px; color: #555; font-weight: 600; font-size: 13px;}
        .form-col input, .form-col select, .form-col textarea { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px; }
        
        /* [MỚI] Style cho thanh điều hướng trên cùng */
        .nav-top-bar {
            padding: 20px 0;
            margin-bottom: 10px;
        }
    </style>
</head>
<body class="dashboard-body" style="overflow-y: auto;">
    
    <div class="schedule-container">
        
        <div class="nav-top-bar">
            <a href="${pageContext.request.contextPath}/dashboard-note" class="btn" style="background: rgba(255,255,255,0.2); color: white; padding: 10px 20px; border-radius: 20px; text-decoration: none; font-size: 14px; display: inline-flex; align-items: center; gap: 5px;">
                <i class="fa-solid fa-arrow-left"></i> Quay lại Note
            </a>
        </div>

        <div class="schedule-header">
            <div>
                <h2 style="margin: 0; font-size: 28px;">Lịch trình làm việc</h2>
                <p style="margin: 5px 0 0; opacity: 0.9;">Quản lý thời gian hiệu quả</p>
            </div>
            <button class="btn-create" onclick="toggleModal('addScheduleModal', true)">
                <i class="fa-solid fa-plus"></i> Thêm lịch mới
            </button>
        </div>

        <c:if test="${not empty param.msg}">
            <div class="alert alert-success" style="background: #d4edda; color: #155724; padding: 15px; border-radius: 5px; margin-bottom: 20px;">
                <i class="fa-solid fa-check-circle"></i> Thao tác thành công!
            </div>
        </c:if>
        
        <div class="schedule-grid">
            <c:if test="${empty scheduleList}">
                <div style="grid-column: 1/-1; text-align: center; color: white; padding: 50px;">
                    <i class="fa-solid fa-calendar-xmark" style="font-size: 50px; opacity: 0.5; margin-bottom: 15px;"></i>
                    <p>Chưa có lịch trình nào.</p>
                </div>
            </c:if>

            <c:forEach var="item" items="${scheduleList}">
                <div class="schedule-card priority-${item.priority}">
                    
                    <div class="card-actions">
                        <a href="${pageContext.request.contextPath}/schedule?action=edit&id=${item.id}" class="btn-icon btn-edit-sch" title="Sửa">
                            <i class="fa-solid fa-pen"></i>
                        </a>
                        
                        <form action="${pageContext.request.contextPath}/schedule" method="POST" style="display:inline;" onsubmit="return confirm('Xóa lịch này?');">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${item.id}">
                            <button type="submit" class="btn-icon btn-del-sch" title="Xóa">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </form>
                    </div>

                    <div class="time-badge">
                        <i class="fa-regular fa-clock"></i> 
                        <fmt:formatDate value="${item.startTime}" pattern="HH:mm dd/MM"/> 
                        - 
                        <fmt:formatDate value="${item.endTime}" pattern="HH:mm dd/MM"/>
                    </div>
                    
                    <h3 class="schedule-title">${item.subject}</h3>
                    <p class="schedule-desc">${item.description}</p>
                    
                    <div class="schedule-meta">
                        <span><i class="fa-solid fa-location-dot"></i> ${item.location}</span>
                        <span>
                            <c:choose>
                                <c:when test="${item.priority == 'HIGH'}"><b style="color:#ff4d4d">Cao</b></c:when>
                                <c:when test="${item.priority == 'MEDIUM'}"><b style="color:#ffca28">Bình thường</b></c:when>
                                <c:otherwise>Thấp</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <div id="addScheduleModal" class="modal">
        <div class="modal-overlay" onclick="toggleModal('addScheduleModal', false)"></div>
        <form class="modal-content animate" action="${pageContext.request.contextPath}/schedule" method="POST">
            <input type="hidden" name="action" value="create">
            <div class="modal-header">
                <h3>Tạo lịch trình mới</h3>
                <span class="close-btn" onclick="toggleModal('addScheduleModal', false)">&times;</span>
            </div>
            <div class="modal-body">
                <div class="form-row">
                    <div class="form-col">
                        <label>Tiêu đề</label>
                        <input type="text" name="subject" required placeholder="Họp nhóm, Deadline...">
                    </div>
                    <div class="form-col">
                        <label>Độ ưu tiên</label>
                        <select name="priority">
                            <option value="LOW">Thấp</option>
                            <option value="MEDIUM" selected>Bình thường</option>
                            <option value="HIGH">Cao (Gấp)</option>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-col">
                        <label>Bắt đầu</label>
                        <input type="datetime-local" name="startTime" required>
                    </div>
                    <div class="form-col">
                        <label>Kết thúc</label>
                        <input type="datetime-local" name="endTime" required>
                    </div>
                </div>
                <div class="form-col" style="margin-bottom: 15px;">
                    <label>Địa điểm</label>
                    <input type="text" name="location" placeholder="Online, Phòng 202...">
                </div>
                <div class="form-col">
                    <label>Mô tả chi tiết</label>
                    <textarea name="description" rows="3"></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="toggleModal('addScheduleModal', false)">Hủy</button>
                <button type="submit" class="btn-submit">Lưu lịch</button>
            </div>
        </form>
    </div>

    <script src="${pageContext.request.contextPath}/script/script.js"></script>
</body>
</html>