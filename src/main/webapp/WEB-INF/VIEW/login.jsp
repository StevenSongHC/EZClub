<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
String basepath = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="include/include_style.jsp" flush="true"/>
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/login.css">
<title>登陆 | EZ Club</title>
</head>
<body>
<div id="main">
	<div id="index-sticker" class="rounded-border">
		<a href="<%=basepath%>"><img alt="EZClub" src="<%=basepath%>/images/site/logo.jpg"></a>
	</div>
	<h1>EZ Club 登陆答卷</h1>
	<h3>答题卡</h3>
	<br>
	<br>
	<br>
	<a href="<%=basepath%>/join" class="link">我还没答过试卷</a>
	<form id="login-form" action="javascript:submitLogin()">
		<div class="login-label unselectable">登&nbsp;&nbsp;录&nbsp;&nbsp;邮&nbsp;&nbsp;箱</div>
		<input type="text" class="login-data" id="email">
		<div class="login-label unselectable">登&nbsp;&nbsp;陆&nbsp;&nbsp;口&nbsp;&nbsp;令</div>
		<input type="password" class="login-data" id="password">
		<div class="check-option">
			<label>
				<input type="checkbox" name="rememberme" id="rememberme">&nbsp;老师请记住我！
			</label>
		</div>
		<button type="submit" id="submit-login" class="btn btn-default btn-lg">交卷</button>
		<div class="alert alert-warning" id="empty">请输入登陆邮箱和密码</div>
		<div class="alert alert-danger" id="wrong">邮箱或密码错误</div>
		<div class="alert alert-danger" id="fail">服务器出错</div>
	</form>
</div>

<jsp:include page="include/include_js.jsp" flush="true"/>
<script type="text/javascript">
(function() {
	$("#login-form").keydown(function(e) {
		if (e.keyCode == 13) {
			submitLogin();
		}
	});
})();
function submitLogin() {
	$(".alert").hide();
	var email = $("#email").val().trim();
	var password = $("#password").val();
	var rememberme = $("#rememberme:checked").val();
	if (email.length === 0 || password.length === 0) {
		$("#empty").show();
		return;
	}
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/login.do",
		dataType: "JSON",
		data: {
			email: email,
			password: password,
			rememberme: rememberme
		}
	}).done(function(data) {
		switch (data.code) {
			case -1:
				$("#wrong").show();
				break;
			case 1:
				window.location.href = "<%=basepath%>";
				break;
			default:
				$("#fail").show();
		}
	}).fail(function() {
		$("#fail").show();
	}).error(function (XMLHttpRequest, textStatus, errorThrown) {
		$("#fail").show();
	});
}
</script>
</body>
</html>