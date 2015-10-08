<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript"
	src="${ctx}/static/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript"
	src="${ctx}/static/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${ctx}/static/jquery/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css"
	href="${ctx}/static/styles/main.css" id="swicth-style" />
</head>

<body>
	<div id="contentWrap">
		<div id="coupon_query_id">
			<table>
				<tr style="height: 40px;">
					<td width="100px">
						<select name="select" id="o_w_status" style="width: 100px">
							<option value="-1" selected="selected">发布日期</option>
							<option value="1">时间倒序</option>
							<option value="2">时间正序</option>
						</select>
					</td>
					<td width="100px">
						<select name="select" id="o_w_status" style="width: 100px">
							<option value="-1" selected="selected">选择城市</option>
							<option value="1">上海</option>
							<option value="2">南京</option>
						</select>
					</td>
					<td width="100px">
						<input type="text" placeholder="发布人手机号"/>
					</td>
					<td>
						<div class="control">
            				<div class="search-box">
               				 	<input type="text" placeholder="搜索...."/>
                					<a href="javascript:void(0);">查询</a>
            				</div>
           				 	<a href="${ctx}/info/add" class="release-box">发布资讯</a>
       				 	</div>
       				 </td>
				</tr>
			</table>
		</div>
		<div class="pageColumn" style="margin-top: 50px">
			<table id="dg"></table>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$('#dg').datagrid({
			url : '${ctx}/m/info/list',
			method : 'GET',
			queryParams : {
				'infoType' : 1
			},
			fitCloumns : true,
			nowrap : true,
			singleSelect : true,
			pagination : true,//分页控件 
			columns : [ [ {
				field : 'id',
				title : 'ID',
				width : 50
			}, {
				field : 'title',
				title : '标题',
				width : fixWidth(0.15),
				align : 'right'
			}, {
				field : 'desc',
				title : '详情',
				width : fixWidth(0.1),
				align : 'right'
			}, {
				field : 'linkman',
				title : '联系人',
				width : fixWidth(0.08),
				align : 'right'
			}, {
				field : 'photo',
				title : '图片',
				width : fixWidth(0.1),
				align : 'right'
			}, {
				field : 'city',
				title : '城市',
				width : fixWidth(0.1),
				align : 'right'
			}, {
				field : 'phone',
				title : '联系电话',
				width : fixWidth(0.15),
				align : 'right'
			}, {
				field : 'createTime',
				title : '创建时间',
				width : fixWidth(0.15),
				align : 'right'
			}, {
				field : 'op',
				title : '操作',
				width : 155,
				formatter : function(value, rowData, rowIndex) {
				}
			} ] ]
		});
	});
</script>
</html>