<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<section>
		<div class="">
			<h2>Change password</h2>
			<form action="${pageContext.request.contextPath }/auth/resetPassword" method="POST">
				<div>
					<label for="username">User name</label>
					<input type="text" name="username" id="username" placeholder="Your username" required>
				</div>
				<div>
					<label for="password">Password</label>
					<input type="password" name="password" id="password" placeholder="Your password" required>
				</div>
				<div>
					<label for="confirm-password">Confirm your password</label>
					<input type="password" name="confirmPassword" id="confirmPassword" placeholder="Confirm-yourpassword" required>
				</div>
				<button type="submit">Reset password</button>
				<a href="${pageContext.request.contextPath}/auth/login.jsp">Sign In</a>
			</form>
		</div>
	</section>
</body>
</html>