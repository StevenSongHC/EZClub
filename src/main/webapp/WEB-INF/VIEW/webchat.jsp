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
<title>Room #${roomId} - ${username}</title>
</head>
<body>
<div id="display" style="width: 500px; height: 300px; background-color: yellow;"></div>
<input type="text" id="message">
<button onclick="javascript:sendMessage()">send</button>

<script src="<%=basepath%>/js/sockjs-1.0.3.min.js"></script>
<script src="<%=basepath%>/js/stomp.min.js"></script>
<script type="text/javascript">
var display = document.getElementById("display");
var websocket = new SockJS("<%=basepath%>/websocket");
var stompClient = Stomp.over(websocket);
stompClient.connect({}, function(frame) {
	stompClient.subscribe("topic/receiver/${roomId}", function(data) {
		var newMessage = document.createElement("div");
		newMessage.appendChild(document.createTextNode(JSON.parse(data.body).username + "：" + JSON.parse(data.body).message));
		display.appendChild(newMessage);
	});
});

function sendMessage() {
	var sendMessage = document.getElementById("message");
	var message = sendMessage.value;
	stompClient.send("ez/webchat.send", {}, JSON.stringify({"username": '${username}', "message": message, "roomId": ${roomId}}));
	sendMessage.value = "";
}
</script>
</body>
</html>