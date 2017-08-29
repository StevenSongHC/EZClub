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
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/edit-activity.css">
<title>编辑【${activity.title} - ${activity.club.cnName}】 | EZ CLub</title>
</head>
<body>
<div id="main" class="row">
	<div class="col-md-2">
		<img src="<%=basepath%>/${activity.club.badge}" class="club-badge">
		<a href="<%=basepath%>/" class="club-cn-name">${activity.club.cnName}</a>
	</div>
	<div class="col-md-6">
		<c:choose>
			<c:when test="${activity.club.manager.user.id == sessionScope.USER_SESSION.id}">
				<input type="text" class="form-control" id="activity-title" value="${activity.title}">
			</c:when>
			<c:otherwise>
				<h3>${activity.title}</h3>
			</c:otherwise>
		</c:choose>
		<div id="workbench">
			<div class="header">
				<span class="member-label">
					<img src="<%=basepath%>/${clubMember.user.photo}" />
					<span class="name">${clubMember.name}</span>
				</span>
				<span class="date"></span>
			</div>
			<div id="displaying-stage">
				<c:forEach items="${myContentList}" var="content">
					<c:if test="${content.type == 1}">
						<div class="content" con-id="${content.id}">${content.content}</div>
					</c:if>
					<c:if test="${content.type == 2}">
						<div class="content" con-id="${content.id}"><img src="<%=basepath%>/${content.content}" /></div>
					</c:if>
					<c:if test="${content.type == 3}">
						<div class="content" con-id="${content.id}"><video src="<%=basepath%>/${content.content}" controls="controls">你的浏览器不支持视频标签</video></div>
					</c:if>
				</c:forEach>
			</div>
			<div id="editing-stage">
				<div class="content">
					<div class="row">
						<div class="col-md-8 col-md-offset-1">
							<textarea class="form-control" id="text-content" rows="3"></textarea>
						</div>
						<div class="col-md-3">
							<button class="btn btn-default btn-lg submit-text-btn" onclick="javascript:submitTextContent()">发布</button>
						</div>
					</div>
				</div>
				<div class="content">
					<form action="<%=basepath%>/activity/create_rich_content" id="image-content" method="post" enctype="multipart/form-data">
						<input type="file" name="imageFile" id="image">
						<input type="hidden" name="type" value=2>
						<input type="hidden" name="activityId" value="${activity.id}">
						<input type="hidden" name="fileType">
						<input type="submit" value="上传图片" class="btn btn-default">
						<p class="help-block">jpg, png, gif 格式</p>
					</form>
					<div class="progress">
						<div id="image-progress-bar" class="progress-bar" role="progressbar" style="width: 0%">
							<span></span>
						</div>
					</div>
				</div>
				<div class="content">
					<form action="<%=basepath%>/activity/create_rich_content" id="video-content" method="post" enctype="multipart/form-data">
						<input type="file" name="videoFile" id="video">
						<input type="hidden" name="type" value=3>
						<input type="hidden" name="activityId" value="${activity.id}">
						<input type="hidden" name="fileType">
						<input type="submit" value="上传视频" class="btn btn-default">
						<p class="help-block">mp4, avi, flv 格式</p>
					</form>
					<div class="progress">
						<div id="video-progress-bar" class="progress-bar" role="progressbar" style="width: 0%">
							<span></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<hr>
		<c:choose>
			<c:when test="${activity.club.manager.user.id == sessionScope.USER_SESSION.id}">
				<div class="manager-content" style="display: none;"></div>
				<c:forEach items="${contentList}" var="content">
					<c:if test="${content.type == 1}">
						<div class="manager-content" con-id="${content.id}">
							<div class="header">
								<span class="member-label">
									<img src="<%=basepath%>/${content.creator.user.photo}" />
									<span class="name">${content.creator.name}</span>
								</span>
								<span class="date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${content.submitDate}"/></span>
							</div>
							<div class="content">${content.content}</div>
						</div>
					</c:if>
					<c:if test="${content.type == 2}">
						<div class="manager-content" con-id="${content.id}">
							<div class="header">
								<span class="member-label">
									<img src="<%=basepath%>/${content.creator.user.photo}" />
									<span class="name">${content.creator.name}</span>
								</span>
								<span class="date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${content.submitDate}"/></span>
							</div>
							<div class="content"><img src="<%=basepath%>/${content.content}" /></div>
						</div>
					</c:if>
					<c:if test="${content.type == 3}">
						<div class="manager-content" con-id="${content.id}">
							<div class="header">
								<span class="member-label">
									<img src="<%=basepath%>/${content.creator.user.photo}" />
									<span class="name">${content.creator.name}</span>
								</span>
								<span class="date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${content.submitDate}"/></span>
							</div>
							<div class="content"><video src="<%=basepath%>/${content.content}" controls="controls">你的浏览器不支持视频标签</video></div>
						</div>
					</c:if>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<h2>预览</h2>
				<div id="activity-preview">
					<c:forEach items="${contentList}" var="content">
						<c:if test="${content.type == 1}">
							<div class="content" con-id="${content.id}">${content.content}</div>
						</c:if>
						<c:if test="${content.type == 2}">
							<div class="content" con-id="${content.id}"><img src="<%=basepath%>/${content.content}" /></div>
						</c:if>
						<c:if test="${content.type == 3}">
							<div class="content" con-id="${content.id}"><video src="<%=basepath%>/${content.content}" controls="controls">你的浏览器不支持视频标签</video></div>
						</c:if>
					</c:forEach>
				</div>
			</c:otherwise>
		</c:choose>
		<c:if test="${activity.club.manager.user.id == sessionScope.USER_SESSION.id}">
			<hr>
			<button class="btn btn-default btn-lg" id="btn-publish" onclick="javascript:publishActivity()">发布</button>
		</c:if>
	</div>
	<div class="col-md-4">
		<div class="chat-room">
			<h4><b>在线编辑<span id="editor-amount">${fn:length(activity.editors)}</span></b></h4>
			<ul id="editor-list">
				<c:forEach items="${activity.editors}" var="editor">
					<li data-cmid="${editor.id}"<c:if test="${editor.id == clubMember.id}"> class="self"</c:if>>${editor.name}（${editor.contact}）</li>
				</c:forEach>
			</ul>
			<div class="chat-container">
				<div id="chat-box"></div>
				<textarea rows="2" id="chat-content" class="form-control"></textarea>
				<button class="btn btn-default btn-sm" onclick="javascript:sendChat()">发送</button>
			</div>
		</div>
	</div>
</div>

<jsp:include page="../include/include_navbar.jsp" flush="true"/>
<script type="text/javascript"	src="<%=basepath%>/js/jquery.form.min.js"></script>
<script type="text/javascript">
stompClient.connect({}, function(frame) {
	stompClient.subscribe("topic/edit_activity/${activity.id}", function(data) {
		switch (JSON.parse(data.body).type) {
			// new editor entered
			case 1:
				$("#editor-list").append("<li data-cmid='" + JSON.parse(data.body).editorId + "'>" + JSON.parse(data.body).editorName + "（" + JSON.parse(data.body).editorContact + "）</li>");
				$("#editor-amount").html(JSON.parse(data.body).editorAmount);
				break;
			// an editor leaves
			case 2:
				$("#editor-list>li[data-cmid='" + JSON.parse(data.body).editorId + "']").remove();
				$("#editor-amount").html(JSON.parse(data.body).editorAmount);
				break;
			// display message
			case 3:
				var newMessage = "<div class='item'>" + JSON.parse(data.body).editorName + "：" + JSON.parse(data.body).message + "</div>";
				$("#chat-box").prepend(newMessage);
				break;
			<c:choose>
				<c:when test="${activity.club.manager.user.id == sessionScope.USER_SESSION.id}">
					// displaying new text content
					case 4:
						var newContent = "<div class='manager-content' con-id='" + JSON.parse(data.body).contentId + "'>";
						newContent += "<div class='header'><span class='member-label'>";
						newContent += "<img src='<%=basepath%>/" + JSON.parse(data.body).editorPhoto + "'/>";
						newContent += "<span class='name'>" + JSON.parse(data.body).editorName + "<span>";
						newContent += "<span class='date'>" + JSON.parse(data.body).editingDate + "<span>";
						newContent += "</span></div>";
						newContent += "<div class='content'>" + JSON.parse(data.body).content + "</div>";
						newContent += "</div>";
						$(".manager-content:last").after(newContent);
						break;
					// displaying new image content
					case 5:
						var newContent = "<div class='manager-content' con-id='" + JSON.parse(data.body).contentId + "'>";
						newContent += "<div class='header'><span class='member-label'>";
						newContent += "<img src='<%=basepath%>/" + JSON.parse(data.body).editorPhoto + "'/>";
						newContent += "<span class='name'>" + JSON.parse(data.body).editorName + "<span>";
						newContent += "<span class='date'>" + JSON.parse(data.body).editingDate + "<span>";
						newContent += "</span></div>";
						newContent += "<div class='content'><img src='<%=basepath%>/" + JSON.parse(data.body).content + "'/></div>";
						newContent += "</div>";
						$(".manager-content:last").after(newContent);
						break;
					// displaying new video content
					case 6:
						var newContent = "<div class='manager-content' con-id='" + JSON.parse(data.body).contentId + "'>";
						newContent += "<div class='header'><span class='member-label'>";
						newContent += "<img src='<%=basepath%>/" + JSON.parse(data.body).editorPhoto + "'/>";
						newContent += "<span class='name'>" + JSON.parse(data.body).editorName + "<span>";
						newContent += "<span class='date'>" + JSON.parse(data.body).editingDate + "<span>";
						newContent += "</span></div>";
						newContent += "<div class='content'><video src='<%=basepath%>/" + JSON.parse(data.body).content + "' controls='controls'>你的浏览器不支持视频标签</video></div>";
						newContent += "</div>";
						$(".manager-content:last").after(newContent);
						break;
				</c:when>
				<c:otherwise>
					case 4:
						var newContent = "<div class='content' con-id='" + JSON.parse(data.body).contentId + "'>";
						newContent += JSON.parse(data.body).content;
						newContent += "</div>";
						$("#activity-preview").append(newContent);
						break;
					case 5:
						var newContent = "<div class='content' con-id='" + JSON.parse(data.body).contentId + "'>";
						newContent += "<img src='<%=basepath%>/" + JSON.parse(data.body).content + "'/>";
						newContent += "</div>";
						$("#activity-preview").append(newContent);
						break;
					case 6:
						var newContent = "<div class='content' con-id='" + JSON.parse(data.body).contentId + "'>";
						newContent += "<video src='<%=basepath%>/" + JSON.parse(data.body).content + "' controls='controls'>你的浏览器不支持视频标签</video>";
						newContent += "</div>";
						$("#activity-preview").append(newContent);
						break;
				</c:otherwise>
			</c:choose>
			// shuting editing function down
			case 7:
				$("#workbench").remove();
				alert("活动发布成功，所有编辑入口已关闭，即将转到活动主页面");
				window.location.reload();
				break;
			default:
				alert("通信代码未定义");
		}
	});
});
function sendChat() {
	var chatContent = $("#chat-content").val();
	if (chatContent.length == 0) {
		return;
	}
	stompClient.send("ez/chat.send", {}, JSON.stringify({"editorName": "${clubMember.name}", "message": chatContent, "activityId": "${activity.id}"}));
	$("#chat-content").val("").focus();
}
(function() {
	window.onbeforeunload = function() {
		exitEditing();
	};
	
	$("#image-content").ajaxForm({
		beforeSerialize: function() {
			$("#image-progress-bar").html(0).width(0);
			var filePath = $("#image").val();
			var fileType = filePath.substring(filePath.lastIndexOf("."), filePath.length).toLowerCase();
			if(fileType != ".jpg" && fileType != ".png" && fileType != ".gif") {
				alert("请选择正确的图片格式进行上传");
				return false;
			}
			$("#image-content input[name='fileType']").val(fileType);
			$("#image-progress-bar").addClass("active");
		},
		uploadProgress: function(event, position, total, percentComplete) {
			var percentVal = percentComplete + "%";
			$("#image-progress-bar").html(percentVal).width(percentVal);
		},
		success: function(data) {
			switch (data.code) {
				case -2:
					alert("不是该社团成员");
					window.location.reload();
					break;
				case -1:
					alert("清先登录");
					window.location.reload();
					break;
				case 0:
					alert("活动数据数据出错");
					window.location.reload();
					break;
				case 1:
					var newContent = "<div class='content' con-id='" + data.contentId + "'>";
					newContent += "<img src='<%=basepath%>/" + data.content + "'/>";
					newContent += "</div>";
					$("#displaying-stage").append(newContent);
					$("#workbench .date").html(data.editingDate);
					$("#image-content").resetForm();
					break;
				default:
					alert("服务器出错");
			}
			$("#image-progress-bar").removeClass("active");
		}
	});
	$("#video-content").ajaxForm({
		beforeSerialize: function() {
			$("#video-progress-bar").html(0).width(0);
			var filePath = $("#video").val();
			var fileType = filePath.substring(filePath.lastIndexOf("."), filePath.length).toLowerCase();
			if(fileType != ".mp4" && fileType != ".avi" && fileType != ".flv") {
				alert("请选择正确的视频格式进行上传");
				return false;
			}
			$("#video-content input[name='fileType']").val(fileType);
			$("#video-progress-bar").addClass("active");
		},
		uploadProgress: function(event, position, total, percentComplete) {
			var percentVal = percentComplete + "%";
			$("#video-progress-bar").html(percentVal).width(percentVal);
		},
		success: function(data) {
			switch (data.code) {
				case -2:
					alert("不是该社团成员");
					window.location.reload();
					break;
				case -1:
					alert("清先登录");
					window.location.reload();
					break;
				case 0:
					alert("活动数据数据出错");
					window.location.reload();
					break;
				case 1:
					var newContent = "<div class='content' con-id='" + data.contentId + "'>";
					newContent += "<video src='<%=basepath%>/" + data.content + "' controls='controls'>你的浏览器不支持视频标签</video>";
					newContent += "</div>";
					$("#displaying-stage").append(newContent);
					$("#workbench .date").html(data.editingDate);
					$("#video-content").resetForm();
					break;
				default:
					alert("服务器出错");
			}
			$("#video-progress-bar").removeClass("active");
		}
	});
})();
function exitEditing() {
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/activity/exit_editing",
		dataType: "JSON",
		data: {
			activityId: "${activity.id}",
			clubMemberId: "${clubMember.id}"
		}
	});
}
function submitTextContent() {
	var content = $("#text-content").val();
	if (content.length == 0) {
		return;
	}
	$.ajax( {
		async: true,
		type: "POST",
		url: "<%=basepath%>/activity/create_text_content",
		dataType: "JSON",
		data: {
			activityId: "${activity.id}",
			clubMemberId: "${clubMember.id}",
			content: content
		}
	}).done(function(data) {
		switch (data.code) {
			case -2:
				alert("不是该社团成员");
				window.location.reload();
				break;
			case -1:
				alert("清先登录");
				window.location.reload();
				break;
			case 0:
				alert("活动数据数据出错");
				window.location.reload();
				break;
			case 1:
				var newContent = "<div class='content' con-id='" + data.contentId + "'>";
				newContent += data.content;
				newContent += "</div>";
				$("#displaying-stage").append(newContent);
				$("#workbench .date").html(data.editingDate);
				$("#text-content").val("");
				break;
			default:
				alert("操作失败");
		}
	}).fail(function() {
		alert("服务器出错");
	});
}
<c:if test="${activity.club.manager.user.id == sessionScope.USER_SESSION.id}">
	function publishActivity() {
		var title = $("#activity-title").val().trim();
		if (title.length == 0) {
			alert("活动名称不能为空");
			$("#activity-title").focus();
			return;
		}
		$.ajax( {
			async: true,
			type: "POST",
			url: "<%=basepath%>/activity/publish",
			dataType: "JSON",
			data: {
				activityId: "${activity.id}",
				title: title
			}
		}).done(function(data) {
			switch (data.code) {
				case -3:
					alert("内容空白的活动是不允许发布的");
					break;
				case -2:
					alert("无权限");
					window.location.reload();
					break;
				case -1:
					alert("清先登录");
					window.location.reload();
					break;
				case 0:
					alert("活动数据数据出错");
					window.location.reload();
					break;
				case 1:
					
					break;
				default:
					alert("操作失败");
			}
		}).fail(function() {
			alert("服务器出错");
		});
	}
</c:if>
</script>
</body>
</html>