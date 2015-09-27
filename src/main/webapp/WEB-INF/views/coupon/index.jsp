<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ctx}/static/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${ctx}/static/jquery/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/static/styles/main.css" id="swicth-style"/>
<script type="text/javascript">  
	function fixWidth(percent)  
	{  
	    return (document.body.clientWidth-200) * percent ; //这里你可以自己做调整  
	}  

    $(document).ready(function(){
    	$('#dg').datagrid({ 
    		url:'${ctx}/m/order/query', 
    		method:'GET',
    		pagination:true,//分页控件 
    		columns:[[ 
    		{field:'id',title:'ID',width:fixWidth(0.06)}, 
    		{field:'orderNo',title:'订单号',width:fixWidth(0.18)}, 
    		{field:'productName',title:'油品',width:fixWidth(0.06),align:'right'},
    		{field:'price',title:'单价',width:fixWidth(0.06),align:'right'},
    		{field:'num',title:'数量',width:fixWidth(0.06),align:'right'},
    		{field:'status',title:'状态',width:fixWidth(0.06),align:'right'},
    		{field:'amount',title:'总价',width:fixWidth(0.08),align:'right'},
    		{field:'userName',title:'用户名',width:fixWidth(0.12),align:'right'},
    		{field:'osName',title:'加油站',width:fixWidth(0.12),align:'right'},
    		{field:'updateTime',title:'更新时间',width:fixWidth(0.1),align:'right'},
    		{field:'createTime',title:'创建时间',width:fixWidth(0.1),align:'right'},
    		{field:'op',title:'操作',width:100,align:'right'} 
    		]] 
    	}); 
    });   
</script>
</head>

<body>
	<div id="contentWrap">
		<div class="" style="">
			<div id="coupon_query_id">
				<table>
					<tr>
						<td><span>ID:</span><input type="text" style="width: 120px"></td>
						<td><span>名称:</span><input type="text" style="width: 120px"></td>
					</tr>
					<tr>
						<td colspan="2"><button type="button">查询</button></td>
					</tr>
				</table>
			</div>
		</div>
		<div class="pageColumn" style="margin-top: 50px">
		    <table id="dg"></table>
		</div>
	</div>
</body>
</html>