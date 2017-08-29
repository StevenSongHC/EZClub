<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
String basepath = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="../include/include_style.jsp" flush="true"/>
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/user-mystuff.css">
<title>我的订阅 | EZ Club</title>
</head>
<body>
<div id="main">
	<div class="row">
		<div class="col-md-3">
			<ul id="club-list">
				<c:forEach items="${mySubscriptionList}" var="mySub">
					<li class="overflow-ellipsis <c:if test="${mySub.club.id == subscription.club.id}"> active</c:if>">
						<a title="${mySub.club.cnName}" href="<%=basepath%>/user/subscription/${mySub.club.id}"><c:if test="${not empty mySub.unreadActivities}"><span class="badge">${fn:length(mySub.unreadActivities)}</span>&nbsp;</c:if>${mySub.club.cnName}</a>
					</li>
				</c:forEach>
			</ul>
		</div>
		<div class="col-md-5">
			<div class="tab-content" id="activity-list">
				<div class="tab-pane fade<c:if test="${not empty unreadActivityList}"> in active</c:if>" id="new">
					<c:forEach items="${unreadActivityList}" var="activity">
						<div class="activity-item">
							<div class="update-date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${activity.updateDate}"/></div>
							<a href="<%=basepath%>/activity/${activity.id}" class="title" target="_blank">${activity.title}</a>
						</div>
					</c:forEach>
				</div>
				<div class="tab-pane fade<c:if test="${empty unreadActivityList}"> in active</c:if>" id="all">
					<c:forEach items="${allActivityList}" var="activity">
						<div class="activity-item">
							<div class="update-date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${activity.updateDate}"/></div>
							<a href="<%=basepath%>/activity/${activity.id}" class="title" target="_blank">${activity.title}</a>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
		<div class="col-md-2">
			<ul class="nav nav-pills nav-stacked">
				<li role="presentation"<c:if test="${not empty unreadActivityList}"> class="active"</c:if>><a href="#new" data-toggle="tab">新发布</a></li>
				<li role="presentation"<c:if test="${empty unreadActivityList}"> class="active"</c:if>><a href="#all" data-toggle="tab">全部</a></li>
			</ul>
		</div>
	</div>
</div>

<jsp:include page="../include/include_navbar.jsp" flush="true"/>
<script type="text/javascript">
(function() {
	$("#my-subscription").addClass("active");
})();
</script>
</body>
</html>