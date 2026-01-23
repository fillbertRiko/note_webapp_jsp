<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/style/style.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
	<div class="profile-wrapper">
		<div class="profile-card">
			<div class="profile-header">
				<div class="profile-avatar-large">
					<img
						src="https://ui-avatars.com/api/?name=${wallOwner.fullname}&background=random&size=256"
						alt="Avatar">
				</div>
			</div>

			<div class="profile-body">
				<c:if test="${isMyWall}">
					<button class="btn-edit-profile"
						onclick="toggleModal('editProfileModal', true)">
						<i class="fa-solid fa-pen-to-square"></i> Cập nhật thông tin
					</button>
				</c:if>

				<h1 class="profile-name">${wallOwner.fullname}</h1>
				<p class="profile-email">
					<i class="fa-solid fa-envelope"></i> ${wallOwner.email}
				</p>

				<hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;">

				<div class="info-grid">
					<div class="info-item">
						<label>Mã thành viên</label>
						<div class="info-value">
							<c:set var="idLen" value="${fn:length(wallOwner.id)}" />

							<span title="ID đầy đủ: ${wallOwner.id}"
								style="font-family: monospace; background: #eee; padding: 2px 6px; border-radius: 4px;">
								#...${fn:substring(wallOwner.id, idLen - 6, idLen)} </span>
						</div>
					</div>

					<div class="info-item">
						<label>Vai trò</label>
						<div class="info-value">
							<span
								style="background: #e3f2fd; color: #0d47a1; padding: 2px 8px; border-radius: 4px; font-size: 12px;">Thành
								viên chính thức</span>
						</div>
					</div>

					<div class="info-item">
						<label>Ngày tham gia</label>
						<div class="info-value">
							<c:if test="${not empty wallOwner.createdAt}">
								<fmt:formatDate value="${wallOwner.createdAt}"
									pattern="dd/MM/yyyy HH:mm" />
							</c:if>
						</div>
					</div>

					<div class="info-item">
						<label>Trạng thái</label>
						<div class="info-value" style="color: green;">
							<i class="fa-solid fa-circle" style="font-size: 10px;"></i> Đang
							hoạt động
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<c:if test="${isMyWall}">
		<div id="editProfileModal" class="modal">
			<div class="modal-overlay"
				onclick="toggleModal('editProfileModal', false)"></div>

			<form class="modal-content animate"
				action="${pageContext.request.contextPath}/dashboard-note"
				method="POST">
				<input type="hidden" name="action" value="update-profile"> <input
					type="hidden" name="_id" value="${wallOwner.id}">

				<div class="modal-header">
					<h3>Cập nhật thông tin</h3>
					<span class="close-btn"
						onclick="toggleModal('editProfileModal', false)">&times;</span>
				</div>

				<div class="modal-body">
					<div class="form-group">
						<label>Họ và tên</label> <input type="text" name="fullname"
							value="${wallOwner.fullname}" required>
					</div>

					<div class="form-group">
						<label>Email (Không thể thay đổi)</label> <input type="email"
							value="${wallOwner.email}" disabled
							style="background: #f9f9f9; color: #999;">
					</div>

					<div class="form-group">
						<label>Mật khẩu mới (Để trống nếu không đổi)</label> <input
							type="password" name="newPassword"
							placeholder="Nhập mật khẩu mới...">
					</div>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn-cancel"
						onclick="toggleModal('editProfileModal', false)">Hủy</button>
					<button type="submit" class="btn-submit">Lưu thay đổi</button>
				</div>
			</form>
		</div>
	</c:if>

	<script>
		const contextPath = "${pageContext.request.contextPath}";
		const serverOwnerId = "${not empty ownerId ? ownerId : currentUser.id}";
	</script>

	<script type="text/javascript"
		src="${pageContext.request.contextPath}/script/script.js"></script>
</body>
</html>