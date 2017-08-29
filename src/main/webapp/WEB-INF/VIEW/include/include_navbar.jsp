<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
String basepath = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/navbar.css">
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/jquery.mCustomScrollbar.min.css">
<div id="banner">
	<nav id="nav1" class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="<%=basepath%>">
					<img alt="EZ Club's logo" src="<%=basepath%>/images/site/logo.jpg">
				</a>
			</div>
			<div class="collapse navbar-collapse">
				<ul id="nav-link" class="nav navbar-nav">
					<li id="index"><a href="<%=basepath%>">首页</a></li>
					<li id="discover"><a href="<%=basepath%>">发现</a></li>
					<li id="tag"><a href="<%=basepath%>">标签</a></li>
					<li id="news"><a href="<%=basepath%>">最新活动</a></li>
					<li id="star"><a href="<%=basepath%>">今日社团</a></li>
				</ul>
				<form class="navbar-form navbar-left" role="search">
					<div class="form-group">
						<input id="search" type="text" class="form-control" placeholder="Search">
					</div>
					<button type="submit" class="btn btn-default">搜索</button>
				</form>
				<ul id="user-hint" class="nav navbar-nav navbar-right">
					<c:choose>
						<c:when test="${not empty sessionScope.USER_SESSION}">
							<li><a href="javascript:void(0)" style="text-decoration: none; cursor: default;">${sessionScope.USER_SESSION.nickname}</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="<%=basepath%>/login">登陆</a></li>
							<li><a href="<%=basepath%>/join">注册</a></li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>
		</div>
	</nav>
	<c:choose>
		<c:when test="${sessionScope.USER_SESSION.isAdmin == 2779}">
			<nav id="admin-nav" class="navbar navbar-default">
				<div class="collapse navbar-collapse">
					<ul id="admin-link" class="nav navbar-nav">
						<li id="admin-college">
							<a href="<%=basepath%>/admin/college">学校<span class="badge" id="new-college-notice"<c:if test="${requestScope.newCollegeCount != 0}"> style="visibility: visible;"</c:if>>${requestScope.newCollegeCount}</span></a>
						</li>
						<li id="admin-club">
							<a href="<%=basepath%>/admin/club">社团<span class="badge" id="new-club-notice"<c:if test="${requestScope.newClubCount != 0}"> style="visibility: visible;"</c:if>>${requestScope.newClubCount}</span></a>
						</li>
						<li id="admin-activity">
							<a href="<%=basepath%>/admin/activity">活动</a>
						</li>
						<li id="admin-user">
							<a href="<%=basepath%>/admin/user">用户</a>
						</li>
					</ul>
				</div>
			</nav>
		</c:when>
		<c:otherwise>
			<nav id="nav2" class="navbar navbar-default">
				<h2>EZ Club - 轻松玩转社团</h2>
			</nav>
		</c:otherwise>
	</c:choose>
</div>
<div id="user-panel">
	<c:choose>
		<c:when test="${not empty sessionScope.USER_SESSION}">
			<a href="<%=basepath%>/u/${sessionScope.USER_SESSION.id}">
				<img alt="我的头像" src="<%=basepath%>/${sessionScope.USER_SESSION.photo}">
			</a>
			
			<span class="badge" id="my-subscription-notice"<c:if test="${requestScope.unreadActivityCount != 0}"> style="visibility: visible;"</c:if>>${requestScope.unreadActivityCount}</span>
			<div class="panel-item" id="my-subscription">
				<a href="<%=basepath%>/user/subscription">我的<br>关注</a>
			</div>
		
			<div class="panel-item" id="my-club">
				<a href="<%=basepath%>/user/club">我的<br>社团</a>
			</div>
		
			<span class="badge" id="my-message-notice"<c:if test="${requestScope.unreadMessageCount != 0}"> style="visibility: visible;"</c:if>>${requestScope.unreadMessageCount}</span>
			<div class="panel-item" id="my-message">
				<a href="<%=basepath%>/user/message">我的<br>消息</a>
			</div>
		
			<div class="panel-item" id="setting">
				<a href="<%=basepath%>/user/setting">设置</a>
			</div>
		
			<div class="panel-item" id="logout">
				<a href="javascript:logout()">退出</a>
			</div>
		</c:when>
		<c:otherwise>
			<!-- unlogin user -->
		</c:otherwise>
	</c:choose>
</div>

<jsp:include page="include_js.jsp" flush="true"/>
<script src="<%=basepath%>/js/jquery.mousewheel.min.js"></script>
<script src="<%=basepath%>/js/jquery.mCustomScrollbar.min.js"></script>
<c:if test="${not empty sessionScope.USER_SESSION}">
	<script src="<%=basepath%>/js/sockjs-1.0.3.min.js"></script>
	<script src="<%=basepath%>/js/stomp.min.js"></script>
	<script type="text/javascript">
	var websocket = new SockJS("<%=basepath%>/websocket");
	var stompClient = Stomp.over(websocket);
	stompClient.connect({}, function(frame) {
		stompClient.subscribe("queue/message/${sessionScope.USER_SESSION.id}", function(data) {
			$("#my-message-notice").css("visibility", "visible").html(JSON.parse(data.body).unreadMessageCount);
		});
	});
	stompClient.connect({}, function(frame) {
		stompClient.subscribe("topic/subscription/${sessionScope.USER_SESSION.id}", function(data) {
			$("#my-subscription-notice").css("visibility", "visible").html(JSON.parse(data.body).unreadActivityCount);
		});
	});
	</script>
</c:if>
<script type="text/javascript">
(function() {
	$("#user-panel").mCustomScrollbar( {
		theme: "minimal-dark",
		axis: "y",
		scrollInertia: 0
	});
})();

function logout() {
	$.ajax( {
		async: false,
		url: "<%=basepath%>/logout",
		type: "POST",
		dataType: "JSON",
	}).done(function(json) {
	}).fail(function() {
		alert("退出失败");
	}).error(function (XMLHttpRequest, textStatus, errorThrown) {
		alert("服务器出错");
	});
	window.location.reload();
}
</script>