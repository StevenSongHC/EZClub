<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
String basepath = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../include/include_style.jsp" flush="true"/>
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/activity-style.css">
<title>${activity.title} - ${activity.club.cnName}</title>
</head>
<body>
<div id="main" class="row">
	<div class="col-md-3">
		<img src="<%=basepath%>/${activity.club.badge}" class="club-badge">
		<div class="club-cn-name overflow-ellipsis">
			<a href="<%=basepath%>/c/${activity.club.id}" title="${activity.club.cnName}">${activity.club.cnName}</a>
		</div>
		<div id="btn-group">
			<c:choose>
				<c:when test="${isSubscribed}">
					<a id="btn-unsubscribe" class="btn btn-default btn-sm" href="javascript:unsubscribe('${activity.club.id}')">已关注</a>
				</c:when>
				<c:otherwise>
					<a id="btn-subscribe" class="btn btn-primary btn-sm" href="javascript:subscribe('${activity.club.id}')">关注</a>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="position">
			<span>${activity.club.college.city.province.cnName}</span>
			<span>${activity.club.college.city.cnName}</span>
			<span>${activity.club.college.cnName}</span>
		</div>
	</div>
	<div class="col-md-7">
		<div id="activity-container">
			<div class="header">
				<span id="activity-title">${activity.title}</span>
				<span id="activity-update-date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${activity.updateDate}"/></span>
			</div>
			<div class="body">
				<c:forEach items="${contentList}" var="content">
					<c:if test="${content.type == 1}">
						<div class="content">${content.content}</div>
					</c:if>
					<c:if test="${content.type == 2}">
						<div class="content"><img src="<%=basepath%>/${content.content}" /></div>
					</c:if>
					<c:if test="${content.type == 3}">
						<div class="content"><video src="<%=basepath%>/${content.content}" controls="controls">你的浏览器不支持视频标签</video></div>
					</c:if>
				</c:forEach>
			</div>
		</div>
		<div id="comment-container">
			<div class="header">评论</div>
			<div class="list">
				<c:forEach items="${commentList}" var="comment">
					<div class="item">
						<div class="row">
							<div class="col-md-2">
								<img src="<%=basepath%>/${comment.user.photo}"/>
							</div>
							<div class="col-md-8">
								<div class="info">${comment.user.nickname}&nbsp;发表于&nbsp;<small><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${comment.submitDate}"/></small></div>
								<div class="content">${comment.content}</div>
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
			<div class="edit">
				<c:choose>
					<c:when test="${not empty sessionScope.USER_SESSION}">
						<div class="row">
							<div class="col-md-2">
								<img src="<%=basepath%>/${sessionScope.USER_SESSION.photo}"/>
							</div>
							<div class="col-md-8">
								<textarea class="form-control" id="comment-content" placeholder="输入评论"></textarea>
								<button class="btn btn-default" onclick="javascript:submitComment('${activity.id}')">发布</button>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<div class="well">
							<p>请先<a href="<%=basepath%>/login">登陆</a>再发表评论</p>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</div>

<jsp:include page="../include/include_navbar.jsp" flush="true"/>
<script type="text/javascript"	src="<%=basepath%>/js/bootstrap-my-pagination.js"></script>
<script type="text/javascript">
(function() {
	$("a#btn-unsubscribe").hover(function() {
		$(this).text("取消关注");
	}, function() {
		$(this).text("已关注");
	});
	$("#comment-container .list").pagination( {
		itemClass: "item",
		pageSize: 5,
		style: 3
	});
}());
function subscribe(clubId) {
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/club/subscribe",
		dataType: "JSON",
		data: {
			clubId: clubId
		}
	}).done(function(data) {
		switch (data.code) {
			case -2:
				alert("你已订阅该社团");
				break;
			case -1:
				alert("清先登录");
				break;
			case 0:
				alert("社团数据数据出错");
				window.location.reload();
				break;
			case 1:
				var btn = "<a id=\"btn-unsubscribe\" class=\"btn btn-default btn-sm\" href=\"javascript:unsubscribe('" + clubId + "')\">已关注</a>";;
				$("#btn-group").html(btn);

				$("a#btn-unsubscribe").hover(function() {
					$(this).text("取消关注");
				}, function() {
					$(this).text("已关注");
				});
				break;
			default:
				alert("操作失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
function unsubscribe(clubId) {
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/club/unsubscribe",
		dataType: "JSON",
		data: {
			clubId: clubId
		}
	}).done(function(data) {
		switch (data.code) {
			case -2:
				alert("你并未订阅该社团");
				break;
			case -1:
				alert("清先登录");
				break;
			case 0:
				alert("社团数据数据出错");
				window.location.reload();
				break;
			case 1:
				var btn = "<a id=\"btn-subscribe\" class=\"btn btn-primary btn-sm\" href=\"javascript:subscribe('" + clubId + "')\">关注</a>";
				$("#btn-group").html(btn);
				break;
			default:
				alert("操作失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
function submitComment(activityId) {
	var content = $("#comment-content").val().trim();
	if (content.length < 5) {
		alert("评论至少为5个字符");
		return;
	}
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/activity/submit_comment",
		dataType: "JSON",
		data: {
			activityId: activityId,
			content: content
		}
	}).done(function(data) {
		switch (data.code) {
			case -1:
				alert("清先登录");
				break;
			case 0:
				alert("社团数据数据出错");
				window.location.reload();
				break;
			case 1:
				window.location.reload();
				break;
			default:
				alert("操作失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
</script>
</body>
</html>