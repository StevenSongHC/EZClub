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
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/user-info-add-and-set.css">
<link rel="stylesheet" type="text/css" href="<%=basepath%>/css/bootstrap-datepicker.css">
<title>注册 | EZ Club</title>
</head>
<body>
<div id="main">
	<div id="index-sticker" class="rounded-border">
		<a href="<%=basepath%>"><img alt="EZClub" src="<%=basepath%>/images/site/logo.jpg"></a>
	</div>
	<h1>EZ Club 注册答卷</h1>
	<h3>答题卡</h3>
	<br>
	<br>
	<br>
	<a href="<%=basepath%>/login" class="link">我之前已经答过试卷了</a>
	<div class="user-account">
		<div class="label unselectable">登陆邮箱</div>
		<span class="alert" id="fail-email">请输入有效未使用邮箱</span>
		<input type="text" id="email">
	</div>
	<div class="user-account">
		<div class="label unselectable">昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称</div>
		<span class="alert" id="fail-nickname">请输入尚未使用的昵称</span>
		<input type="text" id="nickname" maxlength="20">
	</div>
	<div class="user-account">
		<div class="label unselectable">登陆密码</div>
		<span class="alert" id="fail-password">请输入密码长度至少6&nbsp;</span>
		<input type="password" id="password">
	</div>
	<div class="user-account">
		<div class="label unselectable">重输密码</div>
		<span class="alert" id="fail-repeat-password">请输入与上一致的密码</span>
		<input type="password" id="repeat-password">
	</div>
	<div id="user-more">
		<table class="table table-bordered" id="info-marker">
			<thead>
				<tr>
					<th>个人信息</th>
					<th>一</th>
					<th>二</th>
					<th>三</th>
					<th>四</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th scope="row">得分情况</th>
					<td><span class="glyphicon glyphicon-ok" id="sex-info"></span></td>
					<td><span class="glyphicon glyphicon-ok" id="birth-info"></span></td>
					<td><span class="glyphicon glyphicon-ok" id="position-info"></span></td>
					<td><span class="glyphicon glyphicon-ok" id="interest-info"></span></td>
				</tr>
			</tbody>
		</table>
		<hr>
		<div id="load-more">
			<span class="unselectable">点击加载个人信息试题</span>
		</div>
		<div id="user-info-block">
			<div class="user-info">
				1.你的性别是
				<select id="sex">
					<option value="0" selected="selected">神秘</option>
					<option value="1">男</option>
					<option value="2">女</option>
				</select>
				。
			</div>
			<div class="user-info">
				2.你的生日是在
				<input id="birth" class="span2" size="16" type="text" data-date="1990-01-01" data-date-formate="yyyy-mm-dd" readOnly>
				。（<a href="javascript:clearBirth()">点此清空</a>）
			</div>
			<div class="user-info">
				3.你的大学是？（<a href="<%=basepath%>/create_college" target="_blank">没有？点此添加）</a>
				<br>
				<select id="province">
					<option value="0" selected="selected">省份</option>
					<c:forEach items="${provinceList}" var="province">
						<option value="${province.id}">${province.cnName}</option>
					</c:forEach>
				</select>
				<select id="city" disabled="disabled">
					<option value="0" selected="selected">城市</option>
				</select>
				<select id="college" disabled="disabled">
					<option value="0" selected="selected">大学</option>
				</select>
			</div>
			<div class="user-info">
				4.你有什么兴趣爱好？
				<br>
				<span class="add-interest glyphicon glyphicon-plus" onclick="javascript:addInterest()"></span>
			</div>
		</div>
		<hr>
		<button id="submit-join" class="btn btn-default btn-lg" onclick="javascript:submitJoin()">交卷</button>
	</div>
</div>

<jsp:include page="include/include_js.jsp" flush="true"/>
<script type="text/javascript"	src="<%=basepath%>/js/bootstrap-datepicker.js"></script>
<script type="text/javascript">
var g_interestCount = 0;
(function() {
	// enable bootstrap-datepicker 
	$("#birth").datepicker()
		.on("changeDate", function(e) {
			// birth info hint display
			if (e.date.valueOf() !== "") {
				$("#birth-info").show(200);
			}
		});
	
	// bind "ENTER" keypressed event
	$(".user-account").keydown(function(e) {
		if (e.keyCode == 13) {
			submitJoin();
		}
	});
	
	$("#load-more").click(function(e) {
		$(this).slideUp();
		$("#user-info-block").slideDown();
	});
	
	// sex info info display
	$("#sex").change(function(e) {
		if ($(this).val() !== "0") {
			$("#sex-info").show(200);
		}
		else {
			$("#sex-info").hide();
		}
	});
	
	//college info hint display
	$("#college").change(function(e) {
		if ($(this).val() !== "0") {
			$("#position-info").show(200);
		}
		else {
			$("#position-info").hide();
		}
	});
	
	// load position data
	$("#province").change(function(e) {
		if ($(this).val() != "0") {
			$("#city option:not(:first)").remove();
			$("#college").attr("disabled", "disabled").val("0");
			loadCityListByProvince($(this).val());
		}
		else {
			$("#city").attr("disabled", "disabled").val("0");
			$("#college").attr("disabled", "disabled").val("0");
			$("#position-info").hide();
		}
	});
	$("#city").change(function(e) {
		if ($(this).val() != "0") {
			$("#college option:not(:first)").remove();
			loadCollegeListByCity($(this).val());
		}
		else {
			$("#college").attr("disabled", "disabled").val("0");
			$("#position-info").hide();
		}
	});
	$("#college").change(function(e) {
		if ($(this).val() != "0") {
			$("#position-info").show(200);
		}
		else {
			$("#position-info").hide();
		}
	});
	
})();

function validateEmail(email) {
	var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
	return reg.test(email);
}
function isEmailExisted(email) {
	var isExisted = true;
	$.ajax( {
		async: false,
		type: "POST",
		url: "<%=basepath%>/ajax/is_email_existed",
		dataType: "JSON",
		data: {
			email: email
		}
	}).done(function(data) {
		if (data.code === 0) {
			isExisted = false;
		}
	}).fail(function() {
		alert("与服务器通信失败");
	}).error(function (XMLHttpRequest, textStatus, errorThrown) {
		alert("服务器错误");
	});
	return isExisted;
}
function isNicknameExisted(nickname) {
	var isExisted = true;
	$.ajax( {
		async: false,
		type: "POST",
		url: "<%=basepath%>/ajax/is_nickname_existed",
		dataType: "JSON",
		data: {
			nickname: nickname
		}
	}).done(function(data) {
		if (data.code === 0) {
			isExisted = false;
		}
	}).fail(function() {
		alert("与服务器通信失败");
	}).error(function (XMLHttpRequest, textStatus, errorThrown) {
		alert("服务器错误");
	});
	return isExisted;
}

function clearBirth() {
	$("#birth").val("");
	$("#birth-info").hide();
}
function addInterest() {
	var newInterest = "<input type='text' class='interest-item'>";
	newInterest += "<span class='delete-interest glyphicon glyphicon-minus' onclick='javascript:deleteInterest(this)'></span>"
	$(".add-interest").before(newInterest);
	++g_interestCount;
	// interest info hint display
	$("#interest-info").show(200);
}
function deleteInterest(e) {
	$(e).prev().remove();
	$(e).remove();
	if (--g_interestCount <= 0) {
		$("#interest-info").hide();
	}
}

function submitJoin() {
	// hide all alert message
	$(".alert").css("visibility", "hidden");
	// required info
	var email = $("#email").val().trim();
	var nickname = $("#nickname").val().trim();
	var password = $("#password").val();
	var repeatPassword = $("#repeat-password").val();
	// extra info
	var sex = $("#sex").val();
	var birth = $("#birth").val();
	var province = $("#province").val();
	var city = $("#city").val();
	var college = $("#college").val();
	// parse interests into string array
	var interArr = new Array();
	$(".interest-item").each(function(i, e) {
		var inter = $(this).val().trim();
		if (inter != null) {
			// delete repeated interest item
			if (interArr.indexOf(inter) == -1)
				interArr.push(inter);
			else {
				$(this).prev().remove();
				$(this).remove();
			}
		}
		else {
			$(this).prev().remove();
			$(this).remove();
		}
	});
	
	// check email
	if (email.length > 0 && validateEmail(email) && !isEmailExisted(email)) {
		// check nickname
		if (nickname.length > 0 && !isNicknameExisted(nickname)) {
			// check password
			if (password.length >= 6) {
				// check repeat password
				if (repeatPassword === password) {
					$.ajax( {
						async: true,
						type: "POST",
						url: "<%=basepath%>/join.do",
						dataType: "JSON",
						data: {
							email: email,
							nickname: nickname,
							password: password,
							sex: sex,
							birth: birth,
							provinceId: province,
							cityId: city,
							collegeId: college,
							"interests[]": interArr
						}
					}).done(function(data) {
						if (data.code === 1) {
							window.location.href = "<%=basepath%>";
						}
						else {
							alert("注册失败");
						}
					}).fail(function() {
						alert("与服务器通信失败");
					}).error(function (XMLHttpRequest, textStatus, errorThrown) {
						alert("服务器错误");
					});
				}
				else {
					$("#fail-repeat-password").css("visibility", "visible");
					$("#repeat-password").focus();
				}
			}
			else {
				$("#fail-password").css("visibility", "visible");
				$("#password").focus();
			}
		}
		else {
			$("#fail-nickname").css("visibility", "visible");
			$("#nickname").focus();
		}
	}
	else {
		$("#fail-email").css("visibility", "visible");
		$("#email").focus();
	}
}
function loadCityListByProvince(province) {
	$.ajax( {
		async: true,
		url: "<%=basepath%>/ajax/load_city_list_by_province",
		type: "POST",
		dataType: "JSON",
		data: {
			province: province
		}
	}).done(function(data) {
		$.each(data.list, function(i, ct) {
			$("#city").append("<option value='" + ct.id + "'>" + ct.cnName + "</option>");
		});
		$("#city").removeAttr("disabled");
	}).fail(function() {
		alert("加载城市列表数据出错");
	});
}
function loadCollegeListByCity(city) {
	$.ajax( {
		async: true,
		url: "<%=basepath%>/ajax/load_college_list_by_city",
		type: "POST",
		dataType: "JSON",
		data: {
			city: city
		}
	}).done(function(data) {
		$.each(data.list, function(i, clg) {
			$("#college").append("<option value='" + clg.id + "'>" + clg.cnName + "</option>");
		});
		$("#college").removeAttr("disabled");
	}).fail(function() {
		alert("加载城市列表数据出错");
	});
}
</script>
</body>
</html>