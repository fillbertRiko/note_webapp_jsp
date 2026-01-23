<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:if test="${empty listPosts}">
	<p style="color: white; text-align: center; grid-column: 1/-1;">Chưa
		có ghi chú nào.</p>
</c:if>

<c:forEach var="post" items="${listPosts}">
	<div class="note-card">
		<div class="note-header">
			<span class="note-date"> <fmt:formatDate
					value="${post.timeCreate}" pattern="dd/MM/yyyy HH:mm" />
			</span> <span class="note-privacy" title="${post.accessLevelId}"> <c:choose>
					<c:when test="${post.accessLevelId == 'PUBLIC'}">
						<i class="fa-solid fa-globe"></i>
					</c:when>
					<c:when test="${post.accessLevelId == 'PRIVATE'}">
						<i class="fa-solid fa-lock"></i>
					</c:when>
					<c:otherwise>
						<i class="fa-solid fa-user-group"></i>
					</c:otherwise>
				</c:choose>
			</span>
		</div>

		<h3 class="note-title">
			<c:out value="${post.title}" />
		</h3>
		<p class="note-excerpt">
			<c:out value="${post.content}" />
		</p>

		<div class="note-footer">
			<span class="tag">Topic: ${post.topicId}</span>

			<div class="note-actions">
				<c:if test="${post.userId == currentUser.id}">

					<button type="button" title="Sửa" class="btn-edit"
						data-id="${post.id}" data-topic="${post.topicId}"
						data-access="${post.accessLevelId}"
						data-comment="${post.numberAllowComment}"
						data-viewers="<c:forEach var='v' items='${post.allowViewer}' varStatus='loop'>${v}${!loop.last ? ',' : ''}</c:forEach>"
						onclick="openEditModal(this)">

						<i class="fa-solid fa-pen"></i> <span class="hidden-data title"
							style="display: none;"><c:out value="${post.title}" /></span> <span
							class="hidden-data content" style="display: none;"><c:out
								value="${post.content}" /></span>
					</button>

					<form action="${pageContext.request.contextPath}/dashboard-note"
						method="POST" style="display: inline;">
						<input type="hidden" name="action" value="delete"> <input
							type="hidden" name="_id" value="${post.id}">
						<button type="submit" title="Xóa"
							onclick="return confirm('Bạn có chắc muốn xóa?')">
							<i class="fa-solid fa-trash"></i>
						</button>
					</form>

				</c:if>
			</div>
		</div>
	</div>
</c:forEach>