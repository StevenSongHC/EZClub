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
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/user-mystuff.css">
<title>我的消息 | EZ Club</title>
</head>
<body>
<div id="main">
	<div class="row">
		<div class="col-md-2">
			<ul class="nav nav-pills nav-stacked">
				<li role="presentation"<c:if test="${not empty unreadMessageList}"> class="active"</c:if>><a href="#unread" data-toggle="tab">未读</a></li>
				<li role="presentation"<c:if test="${empty unreadMessageList}"> class="active"</c:if>><a href="#read" data-toggle="tab">已读</a></li>
			</ul>
		</div>
		<div class="col-md-5 col-md-offset-1">
			<div class="tab-content">
				<div class="tab-pane fade<c:if test="${not empty unreadMessageList}"> in active</c:if>" id="unread">
					<c:forEach items="${unreadMessageList}" var="message">
						<div class="well message-item" data-msgid="${message.id}">
							<div class="header">
								<span class="sender">
									<c:choose>
										<c:when test="${message.sender != null}">
											${message.sender.nickname}
										</c:when>
										<c:otherwise>
											<i>系统</i>
										</c:otherwise>
									</c:choose>
								</span>
								<span class="date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${message.sentDate}"/></span>
							</div>
							<p>${message.content}</p>
							<hr>
							<div class="ope-block">
								<button class="btn btn-default btn-sm btn-set-read" onclick="javascript:setAsRead('${message.id}')">标记为已读</button>
								<button class="btn btn-danger btn-sm" onclick="javascript:deleteMessage('${message.id}')">删除</button>
							</div>
							<div style="clear: both;"></div>
						</div>
					</c:forEach>
				</div>
				<div class="tab-pane fade<c:if test="${empty unreadMessageList}"> in active</c:if>" id="read">
					<c:forEach items="${readMessageList}" var="message">
						<div class="well message-item" data-msgid="${message.id}">
							<div class="header">
								<span class="sender">
									<c:choose>
										<c:when test="${message.sender != null}">
											${message.sender.nickname}
										</c:when>
										<c:otherwise>
											<i>系统</i>
										</c:otherwise>
									</c:choose>
								</span>
								<span class="date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${message.sentDate}"/></span>
							</div>
							<p>${message.content}</p>
							<hr>
							<div class="ope-block">
								<button class="btn btn-danger btn-sm" onclick="javascript:deleteMessage('${message.id}')">删除</button>
							</div>
							<div style="clear: both;"></div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="../include/include_navbar.jsp" flush="true"/>
<script type="text/javascript">
(function() {
	$("#my-message").addClass("active");
})();
function setAsRead(messageId) {
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/user/read_message",
		dataType: "JSON",
		data: {
			messageId: messageId
		}
	}).done(function(data) {
		switch (data.code) {
			case -1:
				alert("非法操作");
				break;
			case 0:
				alert("信息数据出错");
				break;
			case 1:
				var ele = $(".message-item[data-msgid='" + messageId + "']");
				$(ele).find(".btn-set-read").remove();
				$("#read").prepend(ele);
				break;
			default:
				alert("服务器出错");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
function deleteMessage(messageId) {
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/user/delete_message",
		dataType: "JSON",
		data: {
			messageId: messageId
		}
	}).done(function(data) {
		switch (data.code) {
			case -1:
				alert("非法操作");
				break;
			case 0:
				alert("信息数据出错");
				break;
			case 1:
				$(".message-item[data-msgid='" + messageId + "']").remove();
				break;
			default:
				alert("服务器出错");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
</script>
</body>
</html>