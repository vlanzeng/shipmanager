<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<link href="${ctx}/static/bootstrap/2.3.2/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<html>
<head>
	<title>用户管理</title>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>用户名</th><th>船舶名称</th><th>船舶编号</th><th>手机号</th><th>注册时间</tr></thead>
		<tbody>
		<c:forEach items="${users}" var="user">
			<tr>
				<td>${user.username}</td>
				<td>${user.shipname}</td>
				<td>${user.shipno}</td>
				<td>${user.phone}</td>
				<td>
					<fmt:formatDate value="${user.registerDate}" pattern="yyyy年MM月dd日  HH时mm分ss秒" />
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
